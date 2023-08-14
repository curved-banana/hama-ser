package likelion.hamahama.coupon.service;

import likelion.hamahama.brand.entity.Brand;
import likelion.hamahama.brand.repository.BrandRepository;
import likelion.hamahama.comment.entity.Comment;
import likelion.hamahama.comment.repository.CommentRepository;
import likelion.hamahama.coupon.dto.CouponDetailDto;
import likelion.hamahama.coupon.dto.CouponDto;
import likelion.hamahama.coupon.entity.Coupon;
import likelion.hamahama.coupon.entity.enums.Category;
import likelion.hamahama.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final BrandRepository brandRepository;
    private final CommentRepository commentRepository;

    // 브랜드 ID 기반으로 브랜드 찾기
    public Brand findBrandByName(String theName) {
        return brandRepository.findByBrandName(theName);
    }

    // 모든 쿠폰 가져오기
    public List<Coupon> findAll_coupon() {
        return couponRepository.findAll();
    }

    // 카테고리 기반으로 모든 쿠폰 찾기
//    public List<Coupon> findAll_coupon_category(Category theCategory) {
//        return couponRepository.findAllByCategory(theCategory);
//    }

    // 쿠폰 ID 기반으로 쿠폰 찾기
    public Optional<Coupon> findCouponById(Long theId) {
        return couponRepository.findById(theId);
    }

    //====================================

    // DTO를 받아와서 쿠폰 저장
    @Transactional
    public void saveCoupon(CouponDto couponDto){
        Brand tempBrand = findBrandByName(couponDto.getBrandName());

        Coupon tempCoupon = new Coupon(couponDto.getCouponName(),
                couponDto.getCategory(),
                couponDto.getCouponCode(),
                couponDto.getCouponUrl(),
                couponDto.getStartDate(),
                couponDto.getEndDate(),
                couponDto.getDescription(),
                couponDto.getLikeCount());

        tempCoupon.setBrand(tempBrand);

        couponRepository.save(tempCoupon);
    }

    // DTO를 받아와서 쿠폰 수정
    @Transactional
    public void updateCoupon(CouponDto couponDto, long couponId){

        Optional<Coupon> tempCoupon = couponRepository.findById(couponId);

        Brand tempBrand = findBrandByName(couponDto.getBrandName());

        tempCoupon.get().setCouponName(couponDto.getCouponName());
        tempCoupon.get().setCouponCode(couponDto.getCouponCode());
        tempCoupon.get().setCouponUrl(couponDto.getCouponUrl());
        tempCoupon.get().setStartDate(couponDto.getStartDate());
        tempCoupon.get().setEndDate(couponDto.getEndDate());
        tempCoupon.get().setDescription(couponDto.getDescription());
        tempCoupon.get().setLikeCount(couponDto.getLikeCount());
        tempCoupon.get().setBrand(tempBrand);

        couponRepository.save(tempCoupon.get());

    }
    // ID 기반으로 쿠폰 삭제
    @Transactional
    public void deleteById(long theId) {
        couponRepository.deleteById(theId);
    }


   // ==============================================
    // 쿠폰 리스트 처리
    public Page<Coupon> couponList(Pageable pageable){
        return couponRepository.findAll(pageable);
    }
    // 제목에서 키워드 검색 후 결과 리스트 출력
    public Page<Coupon> couponSearchList(String searchKeyWord, Pageable pageable){
        return couponRepository.findByCouponNameContaining(searchKeyWord,pageable);
    }

    //========== 브랜드별 쿠폰 리스트 출력
//    public Page<Coupon> findCouponByBrand(Brand brand, Pageable pageable){
//        return couponRepository.findByBrand(brand,pageable);
//    }
    @Transactional
    public Page<CouponDetailDto> findCouponByBrand(long brandId, Pageable pageable){
        Page<Coupon> brandCoupons = couponRepository.findAllByBrand(brandId, pageable);
        return (Page<CouponDetailDto>) brandCoupons.stream().map(c -> new CouponDetailDto(c));
    }

    /** 메인페이지에서 보이는 인기순/신규순 쿠폰들  */
    /** 수정 완료 */
    private static final int PAGE_COUPON_COUNTING = 6;
    private static final int FIRST_PAGE = 0;

    public Page<CouponDetailDto> couponListBy (Pageable pageable, String orderCriteria){
        pageable = PageRequest.of(FIRST_PAGE, PAGE_COUPON_COUNTING, Sort.by(Sort.Direction.DESC, orderCriteria));
        Page<Coupon> couponPage = couponRepository.findAll(pageable);
        Page<CouponDetailDto> couponDetailDtos = couponPage.map((c -> new CouponDetailDto(c)));
        return couponDetailDtos;
    }





}



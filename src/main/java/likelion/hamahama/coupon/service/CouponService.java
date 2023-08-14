package likelion.hamahama.coupon.service;

import likelion.hamahama.brand.entity.Brand;
import likelion.hamahama.brand.repository.BrandRepository;
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

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final BrandRepository brandRepository;

    // 브랜드 ID 기반으로 브랜드 찾기
    public Brand findBrandByName(String theName) {
        return brandRepository.findByBrandName(theName);
    }

    // 모든 쿠폰 가져오기
    public List<Coupon> findAll_coupon() {
        return couponRepository.findAll();
    }

    // 카테고리 기반으로 모든 쿠폰 찾기
    public List<Coupon> findAll_coupon_category(Category theCategory) {
        return couponRepository.findAllByCategory(theCategory);
    }

    // 쿠폰 ID 기반으로 쿠폰 찾기
    public Coupon findCouponById(long theId) {
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

        Coupon tempCoupon = couponRepository.findById(couponId);

        Brand tempBrand = findBrandByName(couponDto.getBrandName());

        tempCoupon.setCouponName(couponDto.getCouponName());
        tempCoupon.setCouponCode(couponDto.getCouponCode());
        tempCoupon.setCouponUrl(couponDto.getCouponUrl());
        tempCoupon.setStartDate(couponDto.getStartDate());
        tempCoupon.setEndDate(couponDto.getEndDate());
        tempCoupon.setDescription(couponDto.getDescription());
        tempCoupon.setLikeCount(couponDto.getLikeCount());
        tempCoupon.setBrand(tempBrand);

        couponRepository.save(tempCoupon);

    }
    // ID 기반으로 쿠폰 삭제
    @Transactional
    public void deleteById(long theId) {
        couponRepository.deleteById(theId);
    }


   // ==============================================
    // 게시글 리스트 처리
    public Page<Coupon> couponList(Pageable pageable){
        return couponRepository.findAll(pageable);
    }
    // 제목에서 키워드 검색 후 결과 리스트 출력
    public Page<Coupon> couponSearchList(String searchKeyWord, Pageable pageable){
        return couponRepository.findByCouponNameContaining(searchKeyWord,pageable);
    }


    private static final int PAGE_COUPON_COUNT = 9;

    //컨트롤러에서 넘겨받은 카테고리 및 정렬 기준으로 게시물 페이징 객체 반환
   //@Override
    public Page<CouponDetailDto> getCouponList(Pageable pageable, int pageNo, Category category, String orderCriteria) {
        pageable = PageRequest.of(pageNo, PAGE_COUPON_COUNT, Sort.by(Sort.Direction.DESC, orderCriteria));
        Page<Coupon> couponPage = couponRepository.findByCategory(category, pageable);
        Page<CouponDetailDto> result = couponPage.map((p -> new CouponDetailDto(p)));
        return result;
    }


    /** 메인페이지에서 보이는 인기순/신규순 쿠폰들 */
    private static final int PAGE_COUPON_COUNTING = 6;

    public Page<CouponDetailDto> couponListBy (Pageable pageable, int pageNo, String orderCriteria){
        pageable = PageRequest.of(pageNo, PAGE_COUPON_COUNTING, Sort.by(Sort.Direction.DESC, orderCriteria));
        Page<Coupon> couponPage = couponRepository.findAll(pageable);
        Page<CouponDetailDto> couponDetailDtos = couponPage.map((p -> new CouponDetailDto(p)));
        return couponDetailDtos;
    }
}



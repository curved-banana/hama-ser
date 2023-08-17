package likelion.hamahama.coupon.service;

import likelion.hamahama.brand.entity.Brand;
import likelion.hamahama.brand.repository.BrandRepository;
import likelion.hamahama.comment.entity.Comment;
import likelion.hamahama.comment.entity.Evaluation;
import likelion.hamahama.comment.repository.CommentRepository;
import likelion.hamahama.comment.repository.EvaluationRepository;
import likelion.hamahama.coupon.dto.CouponDetailDto;
import likelion.hamahama.coupon.dto.CouponDto;
import likelion.hamahama.coupon.entity.Coupon;
import likelion.hamahama.coupon.entity.CouponLike;
import likelion.hamahama.coupon.entity.enums.Category;
import likelion.hamahama.coupon.repository.CouponLikeRepository;
import likelion.hamahama.coupon.repository.CouponRepository;
import likelion.hamahama.user.entity.User;
import likelion.hamahama.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final BrandRepository brandRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CouponLikeRepository couponLikeRepository;
    private final EvaluationRepository evaluationRepository;
    // 브랜드 ID 기반으로 브랜드 찾기(사용x)
//    public Brand findBrandByName(String theName) {
//        return brandRepository.findByBrandName(theName);
//    }

    // 모든 쿠폰 가져오기 (사용 x)
    public List<Coupon> findAll_coupon() {
        return couponRepository.findAll();
    }

    // 카테고리 기반으로 모든 쿠폰 찾기
//    public List<Coupon> findAll_coupon_category(Category theCategory) {
//        return couponRepository.findAllByCategory(theCategory);
//    }

    // 쿠폰 ID 기반으로 쿠폰 찾기 = 쿠폰 상세페이지
    public CouponDetailDto findCouponById(Long couponId) {

        Optional<Coupon> coupon = couponRepository.findById(couponId);
        List<Evaluation> evaluations = evaluationRepository.findByCoupon(coupon.get());
        List<Boolean> likeCounts = new ArrayList<>();
        List<Boolean> dislikeCounts = new ArrayList<>();
        int likeCount = 0;
        int dislikeCount = 0;
        evaluations.forEach(evaluation -> {
            likeCounts.add(evaluation.getSatisfied());
            dislikeCounts.add(evaluation.getUnsatisfied());
        });

        for(int i=0; i<likeCounts.size(); i++){
            if(likeCounts.get(i)){
                likeCount++;
            }
        }

        for(int i=0; i<dislikeCounts.size(); i++){
            if(dislikeCounts.get(i)){
                dislikeCount++;
            }
        }


        CouponDetailDto responseCoupon = new CouponDetailDto(coupon.get());
        responseCoupon.setLikeCount(likeCount);
        responseCoupon.setDislikeCount(dislikeCount);
        return responseCoupon;
    }

    //====================================

    // DTO를 받아와서 쿠폰 저장
    @Transactional
    public void saveCoupon(CouponDetailDto couponDetailDto){
        Optional<User> user = userRepository.findByEmail(couponDetailDto.getEmail());
        Brand tempBrand = brandRepository.findByBrandName(couponDetailDto.getBrandName());


        Coupon coupon = Coupon.builder()
                .couponName(couponDetailDto.getCouponName())
                .couponCode(couponDetailDto.getCouponCode())
                .couponUrl(couponDetailDto.getCouponUrl())
                .startDate(couponDetailDto.getStartDate())
                .endDate(couponDetailDto.getEndDate())
                .description(couponDetailDto.getDescription())
                .brand(tempBrand)
                .user(user.get())
                .build();


        couponRepository.save(coupon);
    }

    // DTO를 받아와서 쿠폰 수정
    @Transactional
    public void updateCoupon(CouponDetailDto couponDetailDto, long couponId){

        Optional<Coupon> tempCoupon = couponRepository.findById(couponId);

        Brand tempBrand = brandRepository.findByBrandName(couponDetailDto.getBrandName());

        tempCoupon.get().setCouponName(couponDetailDto.getCouponName());
        tempCoupon.get().setCouponCode(couponDetailDto.getCouponCode());
        tempCoupon.get().setCouponUrl(couponDetailDto.getCouponUrl());
        tempCoupon.get().setStartDate(couponDetailDto.getStartDate());
        tempCoupon.get().setEndDate(couponDetailDto.getEndDate());
        tempCoupon.get().setDescription(couponDetailDto.getDescription());
        tempCoupon.get().setPopularity(couponDetailDto.getPopularity());
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
//    public List<Coupon> couponList(Pageable pageable){
//        return couponRepository.findAll(pageable);
//    }
    // 제목에서 키워드 검색 후 결과 리스트 출력
    public List<Coupon> couponSearchList(String searchKeyWord){
        return couponRepository.findByCouponNameContaining(searchKeyWord);
    }

    //========== 브랜드별 쿠폰 리스트 출력
//    public Page<Coupon> findCouponByBrand(Brand brand, Pageable pageable){
//        return couponRepository.findByBrand(brand,pageable);
//    }
    @Transactional
    public List<CouponDto> findCouponByBrand(long brandId){
        List<Coupon> brandCoupons = couponRepository.findByBrandId(brandId);
        List<CouponDto> couponList = new ArrayList<>();
        brandCoupons.forEach(coupon -> {
            couponList.add(CouponDto.builder()
                    .couponId(coupon.getId())
                    .couponName(coupon.getCouponName())
                    .startDate(coupon.getStartDate())
                    .endDate(coupon.getEndDate())
                    .brandImgUrl(coupon.getBrand().getBrandImgUrl())
                    .build());
        });
        return couponList;
    }

    /** 메인페이지에서 보이는 인기순/신규순 쿠폰들  */
    /** 수정 완료 */
    private static final int PAGE_COUPON_COUNTING = 6;
    private static final int FIRST_PAGE = 0;

    public List<CouponDto> couponListBy (String orderCriteria){
        List<Coupon> orderCoupons = couponRepository.findAll(Sort.by(Sort.Direction.DESC, orderCriteria));
//        pageable = PageRequest.of(FIRST_PAGE, PAGE_COUPON_COUNTING, Sort.by(Sort.Direction.DESC, orderCriteria));
//        Page<Coupon> couponPage = couponRepository.findAll(pageable);

        List<CouponDto> coupons = new ArrayList<>();
        orderCoupons.forEach(coupon -> {
            coupons.add(CouponDto.builder()
                    .couponId(coupon.getId())
                    .couponName(coupon.getCouponName())
                    .startDate(coupon.getStartDate())
                    .endDate(coupon.getEndDate())
                    .brandImgUrl(coupon.getBrand().getBrandImgUrl())
                    .build());
        });
        return coupons;
    }


    // 내가 등록한 쿠폰
    @Transactional
    public List<CouponDetailDto> getMyCoupon(String email) {

        Optional<User> user = userRepository.findByEmail(email);

        List<Coupon> mycoupons = couponRepository.findAllByUser(user.get().getId());

        List<CouponDetailDto> mycouponsDto = mycoupons.stream()
                .map( c-> new CouponDetailDto(c)).collect(Collectors.toList());
        return mycouponsDto;
    }





}



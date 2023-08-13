package likelion.hamahama.coupon.controller;

import likelion.hamahama.common.CreateResponseMessage;
import likelion.hamahama.coupon.dto.CouponDetailDto;
import likelion.hamahama.coupon.dto.CouponDto;
import likelion.hamahama.coupon.entity.Coupon;
import likelion.hamahama.coupon.entity.CouponLike;
import likelion.hamahama.coupon.entity.enums.Category;
import likelion.hamahama.coupon.repository.CouponLikeRepository;
import likelion.hamahama.coupon.repository.CouponRepository;
import likelion.hamahama.coupon.service.CouponLikeService;
import likelion.hamahama.coupon.service.CouponService;
import likelion.hamahama.user.repository.UserRepository;
import likelion.hamahama.user.service.UserService;
import lombok.RequiredArgsConstructor;
import likelion.hamahama.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CouponController {
    private final CouponLikeService couponLikeService;
    private final CouponLikeRepository couponLikeRepository;
    private final CouponRepository couponRepository;
    private final CouponService couponService;
    private final UserService userService;
    private final UserRepository userRepository;

//    // 카테고리 별 쿠폰 조회
//    @GetMapping("/coupons/{category}")
//    public List<Coupon> findAll_coupon_category(@PathVariable Category category){
//        return couponService.findAll_coupon_category(category);
//    }

    // 쿠폰 ID 기반으로 단일 쿠폰 조회 = 쿠폰 상세 페이지
    @GetMapping("/couponDetail/{couponId}")
    public Coupon findCouponByName(@PathVariable long couponId) {

        return couponService.findCouponById(couponId);
    }

    //================================

    // 쿠폰 등록
    @PostMapping("/coupons")
    public void addCoupon(@RequestBody CouponDto theCoupon) {
        theCoupon.setCouponId(0);
        couponService.saveCoupon(theCoupon);
    }

    // 쿠폰 수정
    @PutMapping("/couponDetail/{couponId}")
    public void updateCoupon(@PathVariable long couponId, @RequestBody CouponDto theCouponDTO) {

        couponService.updateCoupon(theCouponDTO, couponId);

    }

    // 쿠폰 삭제
    @DeleteMapping("/couponDetail/{couponId}")
    public String deleteCoupon(@PathVariable long couponId) {
        Coupon tempCoupon = couponService.findCouponById(couponId);

        if (tempCoupon == null) {
            throw new RuntimeException("쿠폰이 발견 되지 않았습니다 - " + couponId);
        }
        couponService.deleteById(couponId);

        return "쿠폰이 삭제 되었습니다 - " + couponId;
    }

    //========= 쿠폰 즐겨찾기(이미 즐겨찾기 달려있다면 즐겨찾기 취소)=============
    @PostMapping("/coupon/{couponId}/like")
    public CreateResponseMessage likeCoupon(@PathVariable("userId") Long userId, @PathVariable("couponId") Long couponId) {
        Coupon coupon = couponRepository.findById(couponId).get();
        User user = userRepository.findById(userId).get();
        //User user = userService.findUser(email);
        CouponLike couponLike = couponLikeRepository.findOneByUserAndCoupon(user, coupon);
        if (couponLike == null) {
            couponLikeService.createCouponLike(user, coupon);
        } else couponLikeService.deleteCouponLike(user, coupon);
        return new CreateResponseMessage((long) 200, "좋아요 성공");
    }

    // ========= 브랜드에 해당되는 쿠폰 리스트 ====================
    @GetMapping("/coupon/{brandId}")
    public Page<CouponDetailDto> couponByBrand(@PathVariable("brandId") Long brandId, Pageable pageable) {
        return couponService.findCouponByBrand(brandId, pageable);
    }


    // =============== 키워드 검색하여 쿠폰 이름 해당할 시 list 출력 [검색 화면 생각 ] =================
    @GetMapping("/coupon/search/list")
    public Page<Coupon> couponList(Pageable pageable, String searchKeyword) {
        return searchKeyword == null ? couponService.couponList(pageable) :
                couponService.couponSearchList(searchKeyword, pageable);
    }

    // 메인 페이지에서 보이는 쿠폰들 orderby = 최신순(createdDate), 인기순(likeCount)
    @GetMapping("/main/coupons")
    public Page<CouponDetailDto> couponListBy(@RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                              @RequestParam(required = false, defaultValue = "id", value = "orderby") String orderCriteria,
                                              Pageable pageable) {
        return couponService.couponListBy(pageable, pageNo, orderCriteria);
    }


//    // 쿠폰 게시글 정렬 ( 인기순 / 최신순 )
//    // ======= 글 전체를 카테고리별/페이징별/정렬기준별 조회하는 컨트롤러
//    @GetMapping("/{category}")
//    public Page<CouponDetailDto> searchByCategory(@PathVariable(required = false) String category,
//                                                  @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
//                                                  @RequestParam(required = false, defaultValue = "id", value = "orderby") String orderCriteria,
//                                                  Pageable pageable) {
//        return couponService.getCouponList(pageable, pageNo, Category.valueOf(category), orderCriteria);
//    }




}
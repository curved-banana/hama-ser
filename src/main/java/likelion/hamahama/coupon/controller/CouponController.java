package likelion.hamahama.coupon.controller;

import likelion.hamahama.comment.entity.Comment;
import likelion.hamahama.comment.service.CommentService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    private final CommentService commentService;

//    // 카테고리 별 쿠폰 조회
//    @GetMapping("/coupons/{category}")
//    public List<Coupon> findAll_coupon_category(@PathVariable Category category){
//        return couponService.findAll_coupon_category(category);
//    }

    // 쿠폰 ID 기반으로 단일 쿠폰 조회 = 쿠폰 상세 페이지
    @GetMapping("/coupon/{couponId}")
    public Optional<Coupon> findCouponByName(@PathVariable String couponId) {
        Long requestCouponId = Long.valueOf(couponId);

        return couponService.findCouponById(requestCouponId);
    }

    //================================

    // 쿠폰 등록
    @PostMapping("/coupon/create")
    public void addCoupon(@RequestBody CouponDto theCoupon) {
        theCoupon.setCouponId(0);
        couponService.saveCoupon(theCoupon);
    }

    // 쿠폰 수정
    @PutMapping("/coupon/{couponId}/update")
    public void updateCoupon(@PathVariable String couponId, @RequestBody CouponDto theCouponDTO) {
        Long coupon_id = Long.valueOf(couponId);
        couponService.updateCoupon(theCouponDTO, coupon_id);

    }

    // 쿠폰 삭제
    @GetMapping("/coupon/{couponId}/delete")
    public String deleteCoupon(@PathVariable String couponId){
        Long coupon_id = Long.valueOf(couponId);

        Optional<Coupon> tempCoupon = couponService.findCouponById(coupon_id);


        if (tempCoupon == null) {
            throw new RuntimeException("쿠폰이 발견 되지 않았습니다 - " + couponId);
        }
        couponService.deleteById(coupon_id);

        return "쿠폰이 삭제 되었습니다 - " + couponId;
    }

    //========= 쿠폰 즐겨찾기(이미 즐겨찾기 달려있다면 즐겨찾기 취소)=============
    @PostMapping("/coupon/{userId}/{couponId}/like")
    public CreateResponseMessage likeCoupon(@PathVariable("userId") String userId, @PathVariable("couponId") String couponId) {
        Long user_id = Long.valueOf(userId);
        Long coupon_id = Long.valueOf(userId);
        Optional<Coupon> coupon = couponRepository.findById(coupon_id);
        User user = userRepository.findById(user_id).get();
        //User user = userService.findUser(email);
        CouponLike couponLike = couponLikeRepository.findOneByUserAndCoupon(user, coupon.get());
        if (couponLike == null) {
            couponLikeService.createCouponLike(user, coupon.get());
        } else couponLikeService.deleteCouponLike(user, coupon.get());
        return new CreateResponseMessage((long) 200, "좋아요 성공");
    }

    // ========= 브랜드에 해당되는 쿠폰 리스트 ====================
    @GetMapping("/coupon/{brandId}/list")
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
    @GetMapping("/coupon/main")
    public Page<CouponDetailDto> couponListBy(@RequestParam(required = false, defaultValue = "id", value = "orderby") String orderCriteria,
                                              Pageable pageable) {
        return couponService.couponListBy(pageable, orderCriteria);
    }
    // 키워드 검색으로 쿠폰 값 출력 - 한 쿠폰에 대한 댓글 목록들은 commentController에
    @GetMapping("/coupon/keyword/")
    public ResponseEntity<Page<Coupon>> getCouponsByKeyword(
            @RequestParam String keyword,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Coupon> coupons = couponService.couponSearchList(keyword, pageable);
        return ResponseEntity.ok(coupons);
    }






}
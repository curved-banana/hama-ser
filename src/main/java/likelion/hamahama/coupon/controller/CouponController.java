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

    // 쿠폰 ID 기반으로 단일 쿠폰 조회 = 쿠폰 상세 페이지 (성공)
    @GetMapping("/coupon/{couponId}")
    public CouponDetailDto findCouponByName(@PathVariable Long couponId) {

        return couponService.findCouponById(couponId);
    }

    // 쿠폰 등록 (성공)
    @PostMapping("/coupon/create")
    public void addCoupon(@RequestBody CouponDetailDto theCoupon) {
        couponService.saveCoupon(theCoupon);
    }

    // 쿠폰 수정 (성공)
    @PutMapping("/coupon/{couponId}/update")
    public void updateCoupon(@PathVariable Long couponId, @RequestBody CouponDetailDto theCouponDTO) {

        couponService.updateCoupon(theCouponDTO, couponId);

    }

    // 쿠폰 삭제
//    @GetMapping("/coupon/{couponId}/delete")
//    public String deleteCoupon(@PathVariable Long couponId){
//
//        Optional<Coupon> tempCoupon = couponService.findCouponById(couponId);
//
//
//        if (tempCoupon == null) {
//            throw new RuntimeException("쿠폰이 발견 되지 않았습니다 - " + couponId);
//        }
//        couponService.deleteById(couponId);
//
//        return "쿠폰이 삭제 되었습니다 - " + couponId;
//    }

    //========= 쿠폰 즐겨찾기(이미 즐겨찾기 달려있다면 즐겨찾기 취소)============= (성공)
    @PostMapping("/coupon/{email}/{couponId}/like")
    public CreateResponseMessage likeCoupon(@PathVariable("email") String email, @PathVariable("couponId") Long couponId) {
        Optional<Coupon> coupon = couponRepository.findById(couponId);
        Optional<User> user = userRepository.findByEmail(email);
        //User user = userService.findUser(email);
        CouponLike couponLike = couponLikeRepository.findOneByUserAndCoupon(user.get(), coupon.get());
        if (couponLike == null) {
            couponLikeService.createCouponLike(user.get(), coupon.get());
        } else couponLikeService.deleteCouponLike(user.get(), coupon.get());
        return new CreateResponseMessage((long) 200, "좋아요 성공");
    }

    // ========= 브랜드에 해당되는 쿠폰 리스트 ==================== 성공
    @GetMapping("/coupon/{brandId}/list")
    public List<CouponDto> couponByBrand(@PathVariable("brandId") Long brandId) {
        return couponService.findCouponByBrand(brandId);
    }


    // =============== 키워드 검색하여 쿠폰 이름 해당할 시 list 출력 [검색 화면 생각 ] ================= 사용x
//    @GetMapping("/coupon/search/list")
//    public Page<Coupon> couponList(Pageable pageable, String searchKeyword) {
//        return searchKeyword == null ? couponService.couponList(pageable) :
//                couponService.couponSearchList(searchKeyword, pageable);
//    }

    // 메인 페이지에서 보이는 쿠폰들 orderby = 최신순(createdDate), 인기순(likeCount)
    @GetMapping("/coupon/main")
    public List<CouponDto> couponListBy(@RequestParam(required = false, defaultValue = "id", value = "orderby") String orderCriteria) {
        return couponService.couponListBy(orderCriteria);
    }
    // 키워드 검색으로 쿠폰 값 출력 - 한 쿠폰에 대한 댓글 목록들은 commentController에
    @GetMapping("/coupon")
    public ResponseEntity<List<Coupon>> getCouponsByKeyword(
            @RequestParam String keyword) {
        List<Coupon> coupons = couponService.couponSearchList(keyword);
        return ResponseEntity.ok(coupons);
    }






}
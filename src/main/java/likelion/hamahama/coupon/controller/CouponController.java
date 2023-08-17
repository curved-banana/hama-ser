package likelion.hamahama.coupon.controller;

import likelion.hamahama.comment.service.CommentService;
import likelion.hamahama.common.CreateResponseMessage;
import likelion.hamahama.coupon.dto.CouponDto;
import likelion.hamahama.coupon.dto.CouponDetailDto;
import likelion.hamahama.coupon.entity.Coupon;
import likelion.hamahama.coupon.entity.CouponLike;
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
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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

//    // 카테고리 별 쿠폰 조회(사용 x)
//    @GetMapping("/coupons/{category}")
//    public List<Coupon> findAll_coupon_category(@PathVariable Category category){
//        return couponService.findAll_coupon_category(category);
//    }

    // 쿠폰 ID 기반으로 단일 쿠폰 조회 = 쿠폰 상세 페이지
    @GetMapping("/coupon/{couponId}")
    public CouponDetailDto findCouponByName(@PathVariable Long couponId) {
        Long requestCouponId = Long.valueOf(couponId);

        return couponService.findCouponById(couponId);
    }


    // 전체 쿠폰 조회 사용 x
    @GetMapping("/coupons")
    public List<Coupon> findAll(){
        return couponRepository.findAll();
    }

    //================================

    // 쿠폰 등록
    @PostMapping("/coupon/create")
    public void addCoupon(@RequestBody CouponDetailDto theCoupon) {
        couponService.saveCoupon(theCoupon);
    }

    // 쿠폰 수정
    @PutMapping("/coupon/{couponId}/update")
    public void updateCoupon(@PathVariable String couponId, @RequestBody CouponDetailDto theCouponDetailDTO) {
        Long coupon_id = Long.valueOf(couponId);
        //couponService.updateCoupon(theCouponDetailDTO, coupon_id);

    }

//     //쿠폰 삭제
//    @GetMapping("/coupon/{couponId}/delete")
//    public String deleteCoupon(@PathVariable String couponId){
//        Long coupon_id = Long.valueOf(couponId);
//
//        Optional<Coupon> tempCoupon = couponService.findCouponById(coupon_id);
//
//
//        if (tempCoupon == null) {
//            throw new RuntimeException("쿠폰이 발견 되지 않았습니다 - " + couponId);
//        }
//        couponService.deleteById(coupon_id);
//
//        return "쿠폰이 삭제 되었습니다 - " + couponId;
//    }

    //========= 쿠폰 즐겨찾기(이미 즐겨찾기 달려있다면 즐겨찾기 취소)=============
    @PostMapping("/coupon/{couponId}/like")
    public CreateResponseMessage likeCoupon(@PathVariable("userId") Long userId, @PathVariable("couponId") Long couponId) {
        Optional<Coupon> coupon = couponRepository.findById(couponId);
        User user = userRepository.findById(userId).get();
        //User user = userService.findUser(email);
        CouponLike couponLike = couponLikeRepository.findOneByUserAndCoupon(user, coupon.get());
        if (couponLike == null) {
            couponLikeService.createCouponLike(user, coupon.get());
        } else couponLikeService.deleteCouponLike(user, coupon.get());
        return new CreateResponseMessage((long) 200, "좋아요 성공");
    }

    // ========= 브랜드에 해당되는 쿠폰 리스트 ====================
    @GetMapping("/coupon/{brandId}/list")
    public List<CouponDto> couponByBrand(@PathVariable("brandId") Long brandId) {
        return couponService.findCouponByBrand(brandId);
    }


    // =============== 키워드 검색하여 쿠폰 이름 해당할 시 list 출력 [검색 화면 생각 ] =================
    @PostMapping("/coupon/search/list")
    public Page<Coupon> couponList(@RequestBody Pageable pageable, String searchKeyword) {
        return searchKeyword == null ? couponService.couponList(pageable) :
                couponService.couponSearchList(searchKeyword, pageable);
    }

    // 메인 페이지에서 보이는 쿠폰들 orderby = 최신순(createdDate), 인기순(likeCount)
    @GetMapping("/coupon/main")
    public List<CouponDto> couponListBy(@RequestParam(required = false, defaultValue = "id", value = "orderby") String orderCriteria) {
        return couponService.couponListBy(orderCriteria);
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
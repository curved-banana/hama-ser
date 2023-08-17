package likelion.hamahama.user.controller;


import likelion.hamahama.brand.dto.BrandDto;
import likelion.hamahama.brand.entity.Brand;
import likelion.hamahama.brand.entity.BrandLike;
import likelion.hamahama.brand.repository.BrandLikeRepsitory;
import likelion.hamahama.brand.repository.BrandRepository;
import likelion.hamahama.brand.service.BrandService;
import likelion.hamahama.comment.dto.CommentDto;
import likelion.hamahama.comment.entity.Comment;
import likelion.hamahama.comment.repository.CommentRepository;
import likelion.hamahama.comment.service.CommentService;
import likelion.hamahama.coupon.dto.CouponDetailDto;
import likelion.hamahama.common.CreateResponseMessage;
import likelion.hamahama.coupon.dto.CouponDto;
import likelion.hamahama.coupon.entity.Coupon;
import likelion.hamahama.coupon.service.CouponLikeService;
import likelion.hamahama.coupon.service.CouponService;
import likelion.hamahama.user.entity.User;
import likelion.hamahama.user.repository.UserRepository;
import likelion.hamahama.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MyPageController {
    private final UserService userService;
    private final CouponLikeService couponLikeService;
    private final BrandService brandService;
    private final CommentService commentService;
    private final CouponService couponService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    // 사용한 쿠폰


    // 즐겨찾기한 쿠폰
    @GetMapping("/mypage/likeCoupon")
    public Page<CouponDetailDto> getMyLikedCoupons(User user, Pageable pageable){
       String email = user.getEmail();
       Long user_id = userService.findUserOne(email).getId();

       Page<CouponDetailDto> likedCoupons = couponLikeService.getLikedCoupon(user_id,pageable);
       return likedCoupons;

    }

    // 즐겨찾기한 브랜드들 
    @GetMapping("/mypage/likeBrand")
    public Page<BrandDto> getMyLikedBrands(User user, Pageable pageable){
        String email = user.getEmail();
        Long user_id = userService.findUserOne(email).getId();

        Page<BrandDto> likedBrands = brandService.getLikedBrand(user_id, pageable);

        return likedBrands;
    }

    // 내가 작성한 댓글 불러오기
    @GetMapping("/mypage/createComment")
    public ResponseEntity<List<CommentDto>> getAllComments(@RequestParam String email){
        return new ResponseEntity<>(
                commentService.getAllComments(email), HttpStatus.OK);
    }

    // 내가 등록한 쿠폰 불러오기
    @GetMapping("/mypage/createCoupon")
    public ResponseEntity<List<Coupon>> getMyCoupons(@RequestParam String email){
        return new ResponseEntity<>(
                couponService.getMyCoupon(email), HttpStatus.OK);
    }

    //사용한 쿠폰들
    @GetMapping("/mypage/{email}/usedCoupons")
    public List<CommentDto> getUsedCoupons(@PathVariable String email){
        Optional<User> user = userRepository.findByEmail(email);
        Optional<List<Comment>> commentEntity = commentRepository.findByUser(user.get());

        List<CommentDto> responseCoupons = new ArrayList<>();
        commentEntity.get().forEach(comment -> {
            responseCoupons.add(CommentDto
                    .builder()
                            .brandName(comment.getCoupon().getBrand().getBrandName())
                            .couponName(comment.getCoupon().getCouponName())
                            .comment(comment.getComment())
                    .build());
        });

        return responseCoupons;
    }
}
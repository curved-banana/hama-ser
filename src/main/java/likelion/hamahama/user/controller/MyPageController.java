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
import likelion.hamahama.common.CreateResponseMessage;
import likelion.hamahama.coupon.dto.CouponDetailDto;
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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MyPageController {
    private final UserService userService;
    private final CouponLikeService couponLikeService;
    private final BrandService brandService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private final CouponService couponService;
    private final BrandLikeRepsitory brandLikeRepsitory;
    private final BrandRepository brandRepository;


     //즐겨찾기한 쿠폰
    @GetMapping("/mypage/likeCoupon")
    public List<CouponDto> getMyLikedCoupons(@RequestParam String email){
        Optional<User> user = userRepository.findByEmail(email);
        List<CouponDto> likedCoupons = couponLikeService.getLikedCoupon(email);

        return likedCoupons;

    }
    /** 즐겨찾기한 브랜드 */
    @GetMapping("/mypage/likeBrand")
    public List<BrandDto> getMyLikedBrands(@RequestParam String email){
        Optional<User> user = userRepository.findByEmail(email);
        List<BrandDto> likedBrands = brandService.getLikedBrand(email);
        return likedBrands;
    }

    // ============== 브랜드 즐겨찾기 (마이페이지) ================
    @GetMapping("/mypage/{email}/{brandId}/edit")
    public CreateResponseMessage likeBrand(@PathVariable("email") String email, @PathVariable("brandId") String brandId){
        Long brand_id = Long.valueOf(brandId);
        Optional<Brand> brand = brandRepository.findById(brand_id);
        Optional<User> user = userRepository.findByEmail(email);
        BrandLike brandLike = brandLikeRepsitory.findOneByUserAndBrand(user.get(), brand.get());
        if(brandLike == null ){
            brandService.createBrandFavorite(user.get(), brand.get());
        }else brandService.deleteBrandFavorite(user.get(), brand.get());
        return new CreateResponseMessage((long) 200, "좋아요 성공");
    }

    // 내가 작성한 댓글 불러오기
    @GetMapping("/mypage/createComment")
    public ResponseEntity<List<CommentDto>> getAllComments(@RequestParam String email){
        return new ResponseEntity<>(
                commentService.getAllComments(email), HttpStatus.OK);
    }

    // 내가 등록한 쿠폰 불러오기
    @GetMapping("/mypage/createCoupon")
    public ResponseEntity<List<CouponDetailDto>> getMyCoupons(@RequestParam String email){
        return new ResponseEntity<>(
                couponService.getMyCoupon(email), HttpStatus.OK);
    }

    //사용한 쿠폰들
    @GetMapping("/mypage/{email}/usedCoupons")
    public List<CommentDto> getUsedCoupons(@PathVariable String email){
        Optional<User> user = userRepository.findByEmail(email);
        Optional<List<Comment>> commentEntity = commentRepository.findByUserId(user.get().getId());

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
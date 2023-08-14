package likelion.hamahama.user.controller;

import likelion.hamahama.brand.dto.BrandDto;
import likelion.hamahama.brand.service.BrandService;
import likelion.hamahama.coupon.dto.CouponDetailDto;
import likelion.hamahama.coupon.service.CouponLikeService;
import likelion.hamahama.user.entity.User;
import likelion.hamahama.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MyPageController {
    private final UserService userService;
    private final CouponLikeService couponLikeService;
    private final BrandService brandService;


    // 즐겨찾기한 쿠폰
    @GetMapping("/mypage/likeCoupon")
    public Page<CouponDetailDto> getMyLikedCoupons(User user, Pageable pageable){
       String email = user.getEmail();
       Long user_id = userService.findUserOne(email).getId();

       Page<CouponDetailDto> likedCoupons = couponLikeService.getLikedCoupon(user_id,pageable);
       return likedCoupons;

    }

    // 즐겨찾기한 브랜드
    @GetMapping("/mypage/likeBrand")
    public Page<BrandDto> getMyLikedBrands(User user, Pageable pageable){
        String email = user.getEmail();
        Long user_id = userService.findUserOne(email).getId();

        Page<BrandDto> likedBrands = brandService.getLikedBrand(user_id, pageable);

        return likedBrands;
    }
}
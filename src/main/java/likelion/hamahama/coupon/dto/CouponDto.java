package likelion.hamahama.coupon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import likelion.hamahama.coupon.entity.Coupon;
import likelion.hamahama.brand.entity.Brand;
import likelion.hamahama.coupon.entity.enums.Category;
import likelion.hamahama.user.entity.User;
import lombok.*;

import java.time.LocalDate;

// 쿠폰 목록에서 보여지는것들 및 쿠폰 등록 요청
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponDto {
    private long couponId;
    private String brandName;
    private String brandImgUrl;
    private String couponName;
    private String startDate;
    private String endDate;
    private String email;

//    public CouponDto(long couponId, Category category, String couponName, String couponCode, String couponUrl, String startDate, String endDate, String description, int popularity, User user) {
//        this.couponId = couponId;
//        this.couponName = couponName;
//        this.startDate = startDate;
//        this.endDate = endDate;
//        this.email = email;
//    }
//
//    // 쿠폰 정보 조회시, 쿠폰을 쿠폰DTO로 변환하여 반환하기 위해 사용하는것
//    public CouponDto(Coupon coupon) {
//        this.couponId = coupon.getId();
//        this.category = coupon.getCategory();
//        this.brandName = coupon.getBrand().getBrandName();
//        this.couponName = coupon.getCouponName();
//        this.couponCode = coupon.getCouponCode();
//        this.couponUrl = coupon.getCouponUrl();
//        this.startDate = coupon.getStartDate();
//        this.endDate = coupon.getEndDate();
//        this.description = coupon.getDescription();
//        this.popularity = coupon.getPopularity();
//        this.user = coupon.getUser();
//    }
}

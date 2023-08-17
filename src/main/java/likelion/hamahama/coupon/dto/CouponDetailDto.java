package likelion.hamahama.coupon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import likelion.hamahama.coupon.entity.Coupon;
import likelion.hamahama.coupon.entity.enums.Category;
import likelion.hamahama.user.entity.User;
import lombok.*;

import java.time.LocalDate;

// 쿠폰 등록 및 쿠폰 상세 정보
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponDetailDto {
    private long couponId;
    private String brandName;
    private String brandImgUrl;
    private String couponName;
    private String couponCode;
    private String couponUrl;
    private String startDate;
    private String endDate;
    private String description;
    private int popularity;
    private int likeCount;
    private int dislikeCount;
    private String email;


    // 쿠폰 정보 조회시, 쿠폰을 쿠폰DTO로 변환하여 반환하기 위해 사용하는것
    public CouponDetailDto(Coupon coupon) {
        this.couponId = coupon.getCouponId();
        this.brandName = coupon.getBrand().getBrandName();
        this.couponName = coupon.getCouponName();
        this.couponCode = coupon.getCouponCode();
        this.couponUrl = coupon.getCouponUrl();
        this.startDate = coupon.getStartDate();
        this.endDate = coupon.getEndDate();
        this.description = coupon.getDescription();
        this.popularity = coupon.getPopularity();
    }
}

package likelion.hamahama.coupon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import likelion.hamahama.brand.entity.Brand;
import likelion.hamahama.coupon.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// 쿠폰 상세 정보
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponDetailDto {
    private Long couponId;
    private String couponName;
    private String couponCode;
    private String couponUrl;
    private String brandName;
    private String brandImgUrl;
    private String description;
    private int popularity;
    private String startDate;
    private String endDate;
    private String email;
    private int likeCount;
    private int dislikeCount;

    // private Boolean isLiked;  즐겨찾기된 쿠폰
    // private int likeCount;

    public CouponDetailDto(Coupon coupon){
        this.couponId = coupon.getId();
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

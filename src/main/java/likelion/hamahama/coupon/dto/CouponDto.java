package likelion.hamahama.coupon.dto;

import likelion.hamahama.coupon.entity.Coupon;
import likelion.hamahama.brand.entity.Brand;
import likelion.hamahama.coupon.entity.enums.Category;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

// 쿠폰 등록 및 쿠폰 상세 정보
@Data
@Getter
@Setter
public class CouponDto {
    private long couponId;
    private Category category;
    private String brandName;
    private String couponName;
    private String couponCode;
    private String couponUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private int likeCount;

    public CouponDto(long couponId, Category category, String couponName, String couponCode, String couponUrl, LocalDate startDate, LocalDate endDate, String description, int likeCount) {
        this.couponId = couponId;
        this.category = category;
        this.couponName = couponName;
        this.couponCode = couponCode;
        this.couponUrl = couponUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.likeCount = likeCount;
    }

    // 쿠폰 정보 조회시, 쿠폰을 쿠폰DTO로 변환하여 반환하기 위해 사용하는것
    public CouponDto(Coupon coupon) {
        this.couponId = coupon.getId();
        this.category = coupon.getCategory();
        this.brandName = coupon.getBrand().getBrandName();
        this.couponName = coupon.getCouponName();
        this.couponCode = coupon.getCouponCode();
        this.couponUrl = coupon.getCouponUrl();
        this.startDate = coupon.getStartDate();
        this.endDate = coupon.getEndDate();
        this.description = coupon.getDescription();
        this.likeCount = coupon.getLikeCount();
    }
}

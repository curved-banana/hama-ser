package likelion.hamahama.coupon.dto;

import likelion.hamahama.brand.entity.Brand;
import likelion.hamahama.coupon.entity.Coupon;
import lombok.Data;

import java.time.LocalDate;

// 쿠폰 목록 출력 시 보이는 쿠폰 정보들
@Data
public class CouponDetailDto {
    private Long couponId;
    private String couponName;
    private Brand brand;
    private LocalDate startDate;
    private LocalDate endDate;

    // private Boolean isLiked;  즐겨찾기된 쿠폰
    // private int likeCount;

    public CouponDetailDto(Coupon coupon){
        this.couponId = coupon.getId();
        this.couponName= coupon.getCouponName();
        this.brand = coupon.getBrand();
        this.startDate = coupon.getStartDate();
        this.endDate = coupon.getEndDate();
    }
}

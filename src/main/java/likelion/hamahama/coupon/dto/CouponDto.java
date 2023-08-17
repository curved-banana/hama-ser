package likelion.hamahama.coupon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import likelion.hamahama.brand.entity.Brand;
import likelion.hamahama.coupon.entity.enums.Category;
import likelion.hamahama.user.entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import likelion.hamahama.coupon.entity.Coupon;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

// 쿠폰 목록 출력 시 보이는 쿠폰 정보들
@Data
@Builder
public class CouponDto {
    private Long couponId;
    private String couponName;
    private String brandName;
    private String startDate;
    private String endDate;
    private String couponCode;
    private String brandImgUrl;
    private String email;

    public CouponDto(long couponId, String couponName, String couponCode, String startDate, String endDate) {
        this.couponId = couponId;
        this.couponName = couponName;
        this.couponCode = couponCode;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}



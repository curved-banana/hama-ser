package likelion.hamahama.comment.dto;

import likelion.hamahama.brand.entity.Brand;
import likelion.hamahama.comment.entity.Comment;
import likelion.hamahama.coupon.entity.Coupon;
import likelion.hamahama.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.security.DenyAll;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CommentRequestDto {

   private String nickname;
    private String brandName;
    private String couponName;
    private String comment;

    public CommentRequestDto(Brand brand, Coupon coupon, Comment comment){
        this.brandName = brand.getBrandName();
        this.couponName = coupon.getCouponName();
        this.comment = comment.getComment();
    }

}

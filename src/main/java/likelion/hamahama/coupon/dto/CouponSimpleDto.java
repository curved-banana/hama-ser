package likelion.hamahama.coupon.dto;

import likelion.hamahama.coupon.entity.Coupon;
import likelion.hamahama.coupon.entity.CouponLike;
import likelion.hamahama.user.entity.User;
import lombok.Data;

@Data
public class CouponSimpleDto {
    private Long couponId;
    private String couponName;
    private String couponUrl;
    private String description;
    private Boolean isLiked; // 즐겨찾기
    private int likeCount;


    public CouponSimpleDto(Coupon coupon, User user){
        this.couponId = coupon.getId();
        this.couponName= coupon.getCouponName();
        this.couponUrl = coupon.getCouponUrl();
        this.description = coupon.getDescription();
        this.isLiked = false;
        for(CouponLike couponLike:coupon.getLikeUsers()){
            if(couponLike.getUser().getId() == user.getId()){
                this.isLiked = true;
                break;
            }
        }
        this.likeCount = coupon.getLikeUsers().size();
    }
}

package likelion.hamahama.coupon.entity;

import likelion.hamahama.user.entity.User;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@Table(name="favorites_table")
public class CouponLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="coupon_id", referencedColumnName = "coupon_id")
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName = "user_id")
    private User user;


   /**생성자*/

//    public CouponLike(User user, Coupon coupon){
//        this.user = user;
//        this.coupon = coupon;
//        user.getLikeCoupons().add(this);
//        coupon.getLikeUsers().add(this);
//    }
//    public void dislike(){
//        this.user.getLikeCoupons().remove(this);
//        this.user = null;
//        this.coupon.getLikeUsers().remove(this);
//        this.coupon = null;
//    }
}

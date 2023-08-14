package likelion.hamahama.coupon.entity;

import likelion.hamahama.user.entity.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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


    // ===쿠폰 사용여부 상태 ==
    // 쿠폰 만족 = 0 /1
    private boolean satisfied;

    // 쿠폰 불만족 = 0 /1
    private boolean unsatisfied;

   /**생성자*/

    public CouponLike(User user, Coupon coupon){
        this.user = user;
        this.coupon = coupon;
        user.getLikeCoupons().add(this);
        coupon.getLikeUsers().add(this);
    }
    public void dislike(){
        this.user.getLikeCoupons().remove(this);
        this.user = null;
        this.coupon.getLikeUsers().remove(this);
        this.coupon = null;
    }
}

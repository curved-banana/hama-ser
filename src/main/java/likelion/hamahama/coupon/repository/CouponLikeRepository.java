package likelion.hamahama.coupon.repository;
import likelion.hamahama.coupon.entity.Coupon;
import likelion.hamahama.user.entity.User;
import likelion.hamahama.coupon.entity.CouponLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CouponLikeRepository extends JpaRepository< CouponLike, Long> {

    List<CouponLike> findByUser(User user);
    CouponLike findOneByUserAndCoupon(User user, Coupon coupon);

    @Modifying
    @Query("update Coupon c set c.likeCount = c.likeCount + 1 where c.id = :coupon_id")
    void increaseLikeCount(@Param("coupon_id") Long couponId);

    @Modifying
    @Query("update Coupon c set c.likeCount = c.likeCount - 1 where c.id = :coupon_id")
    void decreaseLikeCount(@Param("coupon_id") Long couponId);
}

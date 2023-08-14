package likelion.hamahama.comment.repository;

import likelion.hamahama.comment.entity.Comment;
import likelion.hamahama.coupon.entity.Coupon;
import likelion.hamahama.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByUserIdAndCouponId(Long userId, Long couponId);

    Optional<List<Comment>> findByUserId(Long userId);

    Optional<List<Comment>> findByCouponId(Long couponId);

}

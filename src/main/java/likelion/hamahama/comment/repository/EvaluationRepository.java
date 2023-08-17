package likelion.hamahama.comment.repository;

import likelion.hamahama.comment.entity.Evaluation;
import likelion.hamahama.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    List<Evaluation> findByCoupon(Coupon coupon);
}

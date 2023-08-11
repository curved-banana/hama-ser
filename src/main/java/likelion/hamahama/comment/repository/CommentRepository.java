package likelion.hamahama.comment.repository;

import likelion.hamahama.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByTwoIds(Long userId, Long couponId);

    boolean existByIds(String userId, String couponId);
}

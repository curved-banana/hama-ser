package likelion.hamahama.comment.service;

import likelion.hamahama.comment.controller.CommentController;
import likelion.hamahama.comment.dto.CommentResponse;
import likelion.hamahama.comment.entity.Comment;
import likelion.hamahama.coupon.entity.Coupon;
import likelion.hamahama.coupon.repository.CouponRepository;
import likelion.hamahama.user.entity.User;
import likelion.hamahama.comment.repository.CommentRepository;
import likelion.hamahama.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private CommentRepository commentRepository;

    public void saveComment(String email, String couponName, String comment){

        Optional<User> user = userRepository.findByEmail(email);

        Optional<Coupon> coupon = couponRepository.findByCouponNameContaining(couponName);

        Comment new_comment = Comment
                .builder()
                .user(user.get())
                .coupon(coupon.get())
                .build();
    }

    public CommentResponse getComment(String email, String couponName){

        Optional<User> user = userRepository.findByEmail(email);

        Optional<Coupon> coupon = couponRepository.findByCouponNameContaining(couponName);

        Optional<Comment> commentEntity = commentRepository.findByTwoIds(user.get().getId(), coupon.get().getId());

        CommentResponse response = CommentResponse
                .builder()
                .userEmail(user.get().getEmail())
                .couponName(coupon.get().getCouponName())
                .comment(commentEntity.get().getComment())
                .build();

        return response;
    }



    public void updateComment(String email, String couponName, String comment){

        Optional<User> user = userRepository.findByEmail(email);

        Optional<Coupon> coupon = couponRepository.findByCouponNameContaining(couponName);

        Optional<Comment> commentEntity = commentRepository.findByTwoIds(user.get().getId(), coupon.get().getId());

        if(commentEntity.isPresent()){
            commentEntity.get().setComment(comment);

            commentRepository.save(commentEntity.get());
        }

    }

    public void deleteComment(String email, String couponName, String comment){

        Optional<User> user = userRepository.findByEmail(email);

        Optional<Coupon> coupon = couponRepository.findByCouponNameContaining(couponName);

        Optional<Comment> commentEntity = commentRepository.findByTwoIds(user.get().getId(), coupon.get().getId());

        if(commentEntity.isPresent()){
            commentRepository.delete(commentEntity.get());
        }

    }
}

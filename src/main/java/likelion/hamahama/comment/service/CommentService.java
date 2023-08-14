package likelion.hamahama.comment.service;

import likelion.hamahama.comment.dto.CommentRequest;
import likelion.hamahama.comment.entity.Comment;
import likelion.hamahama.coupon.entity.Coupon;
import likelion.hamahama.coupon.entity.CouponLike;
import likelion.hamahama.coupon.repository.CouponLikeRepository;
import likelion.hamahama.coupon.repository.CouponRepository;
import likelion.hamahama.user.entity.User;
import likelion.hamahama.comment.repository.CommentRepository;
import likelion.hamahama.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CouponLikeRepository couponLikeRepository;

    public void saveComment(CommentRequest request){

        Optional<User> user = userRepository.findByEmail(request.getUserEmail());

        Optional<Coupon> coupon = couponRepository.findById(request.getCouponId());

        Optional<CouponLike> couponLikeEntity = couponLikeRepository.findByUserIdAndCouponId(user.get().getId(), coupon.get().getId());

        Comment new_comment = Comment
                .builder()
                .user(user.get())
                .coupon(coupon.get())
                .build();

        commentRepository.save(new_comment);

        couponLikeEntity.get().setSatisfied(request.isSatisfied());
        couponLikeEntity.get().setUnsatisfied(request.isUnsatisfied());

        couponLikeRepository.save(couponLikeEntity.get());
    }

    //유저가 작성한 쿠폰의 댓글 조회
    public CommentRequest getComment(String email, Long couponId){

        Optional<User> user = userRepository.findByEmail(email);

        Optional<Coupon> coupon = couponRepository.findById(couponId);

        Optional<Comment> commentEntity = commentRepository.findByUserIdAndCouponId(user.get().getId(), coupon.get().getId());

        CommentRequest response = CommentRequest
                .builder()
                .userEmail(user.get().getEmail())
                .couponName(coupon.get().getCouponName())
                .comment(commentEntity.get().getComment())
                .build();

        return response;
    }


    //유저가 작성한 모든 댓글 조회
    public List<CommentRequest> getAllComments(String email){

        Optional<User> user = userRepository.findByEmail(email);
        Optional<List<Comment>> comments = commentRepository.findByUserId(user.get().getId());

        List<String> commentList = new ArrayList<>();
        comments.get().forEach(comment -> {
            commentList.add(comment.getComment());
        });

        List<String> brandList = new ArrayList<>();
        comments.get().forEach(comment -> {
            brandList.add(comment.getCoupon().getBrand().getBrandName());
        });

        List<String> couponList = new ArrayList<>();
        comments.get().forEach(comment -> {
            couponList.add(comment.getCoupon().getCouponName());
        });

        List<CommentRequest> response = new ArrayList<>();

        for(int i= commentList.size(); i>0; i--){
            response.add(CommentRequest
                    .builder()
                            .comment(commentList.get(i-1))
                            .brandName(brandList.get(i-1))
                            .couponName(couponList.get(i-1))
                    .build());
        }

        return response;
    }

    //쿠폰명에 해당하는 댓글 목록 조회(닉네임, 쿠폰명, 댓글)
    public List<CommentRequest> getAllCommentsByCouponId(Long couponId){

        List<String> commentList = new ArrayList<>();
        List<String> nicknameList = new ArrayList<>();
        List<String> couponList = new ArrayList<>();

        if(couponRepository.existsById(couponId)){
            Optional<List<Comment>> commentsEntity = commentRepository.findByCouponId(couponId);

            commentsEntity.get().forEach(comment -> {
                commentList.add(comment.getComment());
                couponList.add(comment.getCoupon().getCouponName());
                nicknameList.add(comment.getUser().getNickname());
            });


        }

        List<CommentRequest> response = new ArrayList<>();

        for(int i = commentList.size(); i>0; i--){
            response.add(CommentRequest
                    .builder()
                            .couponName(couponList.get(i-1))
                            .comment(commentList.get(i-1))
                            .nickname(nicknameList.get(i-1))
                    .build());
        }

        return response;
    }


    public void updateComment(String email, String couponName, String comment){

        Optional<User> user = userRepository.findByEmail(email);

        Optional<Coupon> coupon = couponRepository.findByCouponNameContaining(couponName);

        Optional<Comment> commentEntity = commentRepository.findByUserIdAndCouponId(user.get().getId(), coupon.get().getId());

        if(commentEntity.isPresent()){
            commentEntity.get().setComment(comment);

            commentRepository.save(commentEntity.get());
        }

    }

    public void deleteComment(String email, String couponName, String comment){

        Optional<User> user = userRepository.findByEmail(email);

        Optional<Coupon> coupon = couponRepository.findByCouponNameContaining(couponName);

        Optional<Comment> commentEntity = commentRepository.findByUserIdAndCouponId(user.get().getId(), coupon.get().getId());

        if(commentEntity.isPresent()){
            commentRepository.delete(commentEntity.get());
        }

    }
}

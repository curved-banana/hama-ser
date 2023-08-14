package likelion.hamahama.comment.service;

import likelion.hamahama.comment.dto.CommentRequestDto;
import likelion.hamahama.comment.dto.CommentResponse;
import likelion.hamahama.comment.entity.Comment;
import likelion.hamahama.coupon.entity.Coupon;
import likelion.hamahama.coupon.repository.CouponRepository;
import likelion.hamahama.user.entity.User;
import likelion.hamahama.comment.repository.CommentRepository;
import likelion.hamahama.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

        Optional<Coupon> coupon = couponRepository.findByCouponName(couponName);

        Comment new_comment = Comment
                .builder()
                .user(user.get())
                .coupon(coupon.get())
                .build();
    }

    public CommentResponse getComment(String email, String couponName){

        Optional<User> user = userRepository.findByEmail(email);

        Optional<Coupon> coupon = couponRepository.findByCouponNameContaining(couponName);

        Optional<Comment> commentEntity = commentRepository.findByUserIdAndCouponId(user.get().getId(), coupon.get().getId());

        CommentResponse response = CommentResponse
                .builder()
                .userEmail(user.get().getEmail())
                .couponName(coupon.get().getCouponName())
                .comment(commentEntity.get().getComment())
                .build();

        return response;
    }

    private static final int PAGE_COMMNET_COUNTING = 6;
    private static final int FIRST_PAGE = 0;
    @Transactional
    public Page<CommentRequestDto> commentListBy(Pageable pageable,String orderCriteria){
        pageable = PageRequest.of(FIRST_PAGE, PAGE_COMMNET_COUNTING, Sort.by(Sort.Direction.DESC, orderCriteria));
        Page<Comment> commentPage = commentRepository.findAll(pageable);
        Page<CommentRequestDto> commentRequestDtos =
                commentPage.map((c-> new CommentRequestDto(c.getCoupon().getBrand(), c.getCoupon(),c)));
        return commentRequestDtos;
    }

    @Transactional
    public Page<CommentRequestDto> findAllComment(Pageable pageable){
        Page<Comment> comments = commentRepository.findAll(pageable);
        Page<CommentRequestDto> result =
                comments.map(c -> new CommentRequestDto(c.getCoupon().getBrand(),c.getCoupon(),c));
        return result;
    }
    // 검색 결과 후 해당하는 쿠폰 아이디를 받아 쿠폰에 달린 댓글들 불러오기
    @Transactional
    public Page<CommentRequestDto> findAllComment(Long couponId,Pageable pageable){
        Page<Comment> findComment = commentRepository.findByCouponId(couponId);
        Page<CommentRequestDto> findResult =
                findComment.map(c -> new CommentRequestDto(c.getCoupon().getBrand(),c.getCoupon(),c));
        return findResult;
    }
//    @Transactional
//    public List<Comment> findCommentsByCouponId(Long couponId){
//        List<Comment> findComment = commentRepository.findByCouponId(couponId);
//        return commentRepository.findByCouponId(couponId);
//    }



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

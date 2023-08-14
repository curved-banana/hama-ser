package likelion.hamahama.comment.controller;

<<<<<<< Updated upstream
import likelion.hamahama.comment.dto.CommentRequestDto;
import likelion.hamahama.comment.dto.CommentResponse;
=======
import likelion.hamahama.comment.dto.CommentRequest;
>>>>>>> Stashed changes
import likelion.hamahama.comment.entity.Comment;
import likelion.hamahama.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

<<<<<<< Updated upstream
=======
import java.nio.file.Path;
>>>>>>> Stashed changes
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentService commentService;

<<<<<<< Updated upstream
    @PostMapping("/create")
    public void saveComment(@RequestBody CommentResponse request){
        commentService.saveComment(request.getUserEmail(), request.getCouponName(), request.getComment());
    }

    @GetMapping("/get")
    public ResponseEntity<CommentResponse> getComment(@RequestBody CommentResponse request){
=======
    @PostMapping("/comment/create")
    public void saveComment(@RequestBody CommentRequest request){

        commentService.saveComment(request);

    }

    @PostMapping("/comment")
    public ResponseEntity<CommentRequest> getComment(@RequestBody CommentRequest request){

>>>>>>> Stashed changes
        return new ResponseEntity<>(
                commentService.getComment(request.getUserEmail(), request.getCouponId()), HttpStatus.OK);
    }
<<<<<<< Updated upstream
    // 메인페이지에서 보이는 댓글들 orderby = createDate
    @GetMapping("/main")
    public Page<CommentRequestDto> commentListBy(@RequestParam(required = false, defaultValue = "id", value = "orderby") String orderCriteria,
                                                 Pageable pageable) {
        return commentService.commentListBy(pageable,orderCriteria);
    }

    @GetMapping("/list")
    public Page<CommentRequestDto> commentList(Pageable pageable){
        return commentService.findAllComment(pageable);
=======

    @GetMapping("/comments")
    public ResponseEntity<List<CommentRequest>> getAllComments(@RequestParam String email){
        return new ResponseEntity<>(
                commentService.getAllComments(email), HttpStatus.OK);
    }

    @GetMapping("/comments/{couponId}")
    public ResponseEntity<List<CommentRequest>> getAllCommentsByCouponId(@PathVariable String couponId){
        Long request = Long.valueOf(couponId);
        return new ResponseEntity<>(commentService.getAllCommentsByCouponId(request), HttpStatus.OK);
    }

    @PostMapping("comment/update")
    public void updateComment(@RequestBody CommentRequest request){
        commentService.updateComment(request.getUserEmail(), request.getCouponName(), request.getComment());
    }

    @GetMapping("comment/delete")
    public void deleteComment(@RequestBody CommentRequest request){
        commentService.deleteComment(request.getUserEmail(), request.getCouponName(), request.getComment());
>>>>>>> Stashed changes
    }

    // 한 쿠폰에 대한 댓글 목록들
    @GetMapping("/coupon/comments")
    public Page<CommentRequestDto> getCommentsByCouponId(@RequestParam Long couponId, Pageable pageable){
        Page<CommentRequestDto> comments = commentService.findAllComment(couponId,pageable);
        return comments;
    }

//    @PostMapping("/update")
//    public void updateComment(@RequestBody CommentResponse request){
//        commentService.updateComment(request.getUserEmail(), request.getCouponName(), request.getComment());
//    }
//
//    @GetMapping("/delete")
//    public void deleteComment(@RequestBody CommentResponse request){
//        commentService.deleteComment(request.getUserEmail(), request.getCouponName(), request.getComment());
//    }
}

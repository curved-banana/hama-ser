package likelion.hamahama.comment.controller;

import likelion.hamahama.comment.dto.CommentRequestDto;
import likelion.hamahama.comment.dto.CommentDto;

import likelion.hamahama.comment.repository.CommentRepository;
import likelion.hamahama.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentService commentService;

    private final CommentRepository commentRepository;


    //댓글 생성

    @PostMapping("/comment/create")
    public void saveComment(@RequestBody CommentDto request){

        commentService.saveComment(request);

    }

    //유저가 작성한 쿠폰 하나의 댓글조회
    @PostMapping("/comment")
    public ResponseEntity<CommentRequestDto> getComment(@RequestBody CommentDto request){


        return new ResponseEntity<>(
                commentService.getComment(request.getUserEmail(), request.getCouponId()), HttpStatus.OK);
    }

    // 메인페이지에서 보이는 댓글들 orderby = createDate
    @GetMapping("comment/main")
    public Page<CommentRequestDto> commentListBy(@RequestParam(required = false, defaultValue = "id", value = "orderby") String orderCriteria,
                                                 Pageable pageable) {
        return commentService.commentListBy(pageable,orderCriteria);
    }


    @GetMapping("comment/list")
    public Page<CommentRequestDto> commentList(Pageable pageable){
            return commentService.findAllComment(pageable);
    }

    @GetMapping("/comments")
    public ResponseEntity<List<CommentDto>> getAllComments(@RequestParam String email){
        return new ResponseEntity<>(
                commentService.getAllComments(email), HttpStatus.OK);
    }

    //한 쿠폰에 해당하는 댓글목록
    @GetMapping("/comments/{couponId}")
    public ResponseEntity<List<CommentDto>> getAllCommentsByCouponId(@PathVariable String couponId){
        Long request = Long.valueOf(couponId);
        return new ResponseEntity<>(commentService.getAllCommentsByCouponId(request), HttpStatus.OK);
    }

    //댓글 수정
    @PostMapping("comment/update")
    public void updateComment(@RequestBody CommentDto request){
        commentService.updateComment(request.getUserEmail(), request.getCouponName(), request.getComment());
    }

    @GetMapping("comment/delete")
    public void deleteComment(@RequestBody CommentDto request){
        commentService.deleteComment(request.getUserEmail(), request.getCouponName(), request.getComment());
    }

    // 한 쿠폰에 대한 댓글 목록들
//    @GetMapping("/coupon/comments")
//    public Page<CommentRequestDto> getCommentsByCouponId(@RequestParam Long couponId, Pageable pageable){
//        Page<CommentRequestDto> comments = commentService.findAllComment(couponId,pageable);
//        return comments;
//    }

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

package likelion.hamahama.comment.controller;

import likelion.hamahama.comment.dto.CommentRequest;
import likelion.hamahama.comment.entity.Comment;
import likelion.hamahama.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/comment/create")
    public void saveComment(@RequestBody CommentRequest request){

        commentService.saveComment(request);

    }

    @GetMapping("/comment")
    public ResponseEntity<CommentRequest> getComment(@RequestBody CommentRequest request){

        return new ResponseEntity<>(
                commentService.getComment(request.getUserEmail(), request.getCouponId()), HttpStatus.OK);
    }

    @GetMapping("/comments/{email}")
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
    }
}

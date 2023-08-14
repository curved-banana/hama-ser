package likelion.hamahama.comment.controller;

import likelion.hamahama.comment.dto.CommentResponse;
import likelion.hamahama.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/save")
    public void saveComment(@RequestBody CommentResponse request){

        commentService.saveComment(request.getUserEmail(), request.getCouponName(), request.getComment());

    }

    @GetMapping("/get")
    public ResponseEntity<CommentResponse> getComment(@RequestBody CommentResponse request){

        return new ResponseEntity<>(
                commentService.getComment(request.getUserEmail(), request.getCouponName()), HttpStatus.OK);
    }

    @PostMapping("/update")
    public void updateComment(@RequestBody CommentResponse request){
        commentService.updateComment(request.getUserEmail(), request.getCouponName(), request.getComment());
    }

    @GetMapping("/delete")
    public void deleteComment(@RequestBody CommentResponse request){
        commentService.deleteComment(request.getUserEmail(), request.getCouponName(), request.getComment());
    }
}

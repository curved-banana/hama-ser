package likelion.hamahama.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private String userEmail;
    private String nickname;
    private Long couponId;
    private String couponName;
    private String brandName;
    private String comment;
    private boolean satisfied;
    private boolean unsatisfied;

}

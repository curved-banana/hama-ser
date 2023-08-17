package likelion.hamahama.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignRequest {
    private Long id;
    private String nickname;
    private String email;
    private String password;
    private String fcmToken;
    private Boolean fcmStatus;
}

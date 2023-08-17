package likelion.hamahama.user.dto;

//import com.rsa101.domain.Authority;
import likelion.hamahama.user.entity.Role;
import likelion.hamahama.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignResponse {
    private Long id;
    private String nickname;
    private String password;
    private String email;
    private Role role;
    //private List<String> role;
    private String accessToken;
    private String refreshToken;
    private Boolean fcmStatus;
    private Boolean status;
    private String code;


    public SignResponse(User user){
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
}

package likelion.hamahama.user.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import likelion.hamahama.user.config.auth.JwtProvider;
import likelion.hamahama.user.dto.FcmRequest;
import likelion.hamahama.user.entity.User;
import likelion.hamahama.user.dto.SignRequest;
import likelion.hamahama.user.dto.SignResponse;
import likelion.hamahama.user.repository.UserRepository;
import likelion.hamahama.user.service.*;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final JwtProvider jwtProvider;
    private final LoginService loginService;
    private final UserService userService;
    private final KakaoLoginService kakaoLoginService;
    private final RegisterMail registerMail;
    private final UserRepository userRepository;
    private final HttpSession httpSession;
    private final FCMService fcmService;

    @GetMapping("/login/oauth2/code/kakao")
    public RedirectView kakaoCallback(@RequestParam(value="code")String code, HttpServletResponse response) throws Exception {

        String kakaoAccessToken = kakaoLoginService.getKakaoAccessToken(code, response);
        Map<String, Object> tokenAll =  kakaoLoginService.createKakaoUser(kakaoAccessToken, response);
        String accessToken = (String) tokenAll.get("accessToken");
        String refreshToken = (String) tokenAll.get("refreshToken");

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://localhost:3008?accessToken=" + accessToken + "&refreshToken=" + refreshToken);

        //return new ResponseEntity<>(kakaoLoginService.createKakaoUser(tokenDTO.getAccessToken(), tokenDTO.getRefreshToken()), HttpStatus.OK);
        //return new ResponseEntity<>(kakaoLoginService.createKakaoUser(tokenDTO.getAccessToken(), tokenDTO.getRefreshToken()), HttpStatus.OK)
        return redirectView;
    }
    @PostMapping("/register")
    public ResponseEntity<Boolean> signup(@RequestBody SignRequest signRequest) throws Exception {
        return new ResponseEntity<>(loginService.register(signRequest), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<SignResponse> signin(@RequestBody SignRequest request, HttpServletResponse response) throws Exception {
        System.out.println(response);
        return new ResponseEntity<>(loginService.login(request, response), HttpStatus.OK);
    }

    @PostMapping("/reissue")
    public ResponseEntity<String> reissueAccessToken(@RequestHeader("refresh-token") String bearerToken){
        String new_accessToken = loginService.reissueAccessToken(bearerToken);
        return new ResponseEntity<>(new_accessToken, HttpStatus.OK);
    }

    //사용자 기기에 대한 access token을 회원 db에 저장
    @PostMapping("user/saveFcmToken")
    public void getFcmToken(@RequestBody SignRequest request){
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if(user.isPresent()){
            user.get().setFcmToken(request.getFcmToken());
            user.get().setFcmStatus(request.getFcmStatus());
            userRepository.save(user.get());
        }
    }

    @PostMapping("user/send")
    public void test(@RequestBody FcmRequest request) throws IOException, FirebaseMessagingException {
        fcmService.sendMessageTo(request.getTopic(), request.getTitle(), request.getBody());
    }

//    @GetMapping("user/test")
//    public void test() throws FirebaseMessagingException {
//        fcmService.topicCreate();
//    }



    //회원가입 시 이메일 인증코드 받기
    @PostMapping("user/register/mailConfirm")
    public @ResponseBody String mailConfirm(@RequestBody SignRequest request) throws Exception{

        String code = registerMail.sendReceiveCodeMessage(request.getEmail());
        System.out.println("인증코드 : " + code);

        return code;
    }

    //이메일로 비밀번호 변경 url 받기
    @PostMapping("/user/resetPassword")
    public void resetPassword(@RequestBody SignRequest request) throws Exception{
        registerMail.sendPasswordResetUrl(request.getEmail());

    }

    //전체 회원 조회
    @GetMapping("/users")
    public ResponseEntity<List<User>> findAll(){

        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);

    }

    //회원 한명 조회
    @GetMapping("/user")
    public ResponseEntity<User> findOneUser(@RequestParam(value = "email") String email){
        return new ResponseEntity<>(userService.findUserOne(email), HttpStatus.OK);
    }

    //회원정보 수정
    @PostMapping("/user/{email}/update")
    public void updateUser(@PathVariable String email, @RequestBody SignRequest request){

        userService.updateUser(email, request);

    }

    //회원 삭제
    @GetMapping("/user/delete")
    public void delete(@RequestParam(value="email") String email){
        userService.deleteUser(email);
    }

}
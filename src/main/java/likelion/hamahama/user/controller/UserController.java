package likelion.hamahama.user.controller;

import likelion.hamahama.user.config.auth.JwtProvider;
import likelion.hamahama.user.entity.User;
import likelion.hamahama.user.dto.SignRequest;
import likelion.hamahama.user.dto.SignResponse;
import likelion.hamahama.user.repository.UserRepository;
import likelion.hamahama.user.service.KakaoLoginService;

import likelion.hamahama.user.service.LoginService;
import likelion.hamahama.user.service.RegisterMail;
import likelion.hamahama.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    @Autowired
    private KakaoLoginService kakaoLoginService;

    @Autowired
    private RegisterMail registerMail;

    @Autowired
    private UserRepository userRepository;

    private final HttpSession httpSession;


    @GetMapping("/hello-world")
    public @ResponseBody String hello(){

        return "Hello World!";
    }

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


    @PostMapping("/login/mailConfirm")
    public @ResponseBody String mailConfirm(@RequestParam(value = "email") String email) throws Exception{

        String code = registerMail.sendSimpleMessage(email);
        System.out.println("인증코드 : " + code);

        return code;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> findAll(){

        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);

    }

    @PostMapping("/user/{email}/update")
    public void updateUser(@PathVariable String email, @RequestBody SignRequest request){

        userService.updateUser(email, request);

    }

    @GetMapping("/user/delete")
    public void delete(@RequestParam(value="email") String email){
        userService.deleteUser(email);
    }

    @GetMapping("/fcm")
    public String getAccessToken() throws IOException {
        return null;
    }

}
package likelion.hamahama.user.config.oauth;

import likelion.hamahama.user.config.auth.JwtProvider;
import likelion.hamahama.user.entity.RefreshToken;
import likelion.hamahama.user.entity.Role;
import likelion.hamahama.user.entity.User;
import likelion.hamahama.user.repository.RefreshTokenRepository;
import likelion.hamahama.user.repository.UserRepository;
//import likelion.hamahama.user.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static likelion.hamahama.user.entity.Role.ROLE_USER;

@Component
@RequiredArgsConstructor
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

//    @Autowired
//    private AuthenticationManager authenticationManager;

//    @Autowired
//    private RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("request : " + request);
        System.out.println("response : " + response);
        System.out.println("authentication : " + authentication);
        System.out.println("SuccessHandler oAuth2User : " + oAuth2User.getAttributes().get("response"));

        User user = new User();
        String email = null;
        if(oAuth2User.getAttribute("email") != null){
            email = oAuth2User.getAttribute("email");
            user = userRepository.findByEmail(email).orElseThrow(()->
                    new BadCredentialsException("해당 유저가 존재하지 않습니다."));
        }else if(oAuth2User.getAttributes().get("response") != null){
            Map<String, Object> result = new LinkedHashMap<>();
            result = (Map<String, Object>) oAuth2User.getAttributes().get("response");
            email = result.get("email").toString();
            user = userRepository.findByEmail(email).orElseThrow(()->
                    new BadCredentialsException("해당 유저가 존재하지 않습니다."));
            System.out.println(email);
        }

        //authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(oAuth2User.getAttribute("email"), oAuth2User.getAttribute("password")));
        if (authentication.isAuthenticated()) {
            //RefreshToken refreshToken = refreshTokenService.createRefreshToken(request.getUsername());
            String accessToken = jwtProvider.createAccessToken(email, ROLE_USER);
            String refreshToken = jwtProvider.createRefreshToken(email, ROLE_USER);
            jwtProvider.setHeaderAccessToken(response, accessToken);
            jwtProvider.setHeaderRefreshToken(response, refreshToken);

            System.out.println("Access Token: " + accessToken);
            System.out.println("Refresh Token: " + refreshToken);
            RefreshToken tokenEntity = RefreshToken.builder()
                    .token(refreshToken)
                    .expiryDate(jwtProvider.getRefreshTokenExpTime(refreshToken))
                    .user(user)
                    .build();

            refreshTokenRepository.save(tokenEntity);

            response.sendRedirect(UriComponentsBuilder.fromUriString("http://localhost:3008")
                    .queryParam("accessToken", accessToken)
                    .queryParam("refreshToken", refreshToken)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString());

        } else {
            throw new UsernameNotFoundException("invalid user request!");
        }

    }

}

package likelion.hamahama.user.config.oauth;

import likelion.hamahama.user.config.auth.UserInfoUserDetails;
import likelion.hamahama.user.config.oauth.provider.GoogleUserInfo;
import likelion.hamahama.user.config.oauth.provider.KakaoUserInfo;
import likelion.hamahama.user.config.oauth.provider.NaverUserInfo;
import likelion.hamahama.user.config.oauth.provider.OAuth2UserInfo;
import likelion.hamahama.user.entity.User;
import likelion.hamahama.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest); // google의 회원 프로필 조회
        System.out.println(userRequest.getClientRegistration());
        System.out.println(userRequest.getAccessToken());
        System.out.println(super.loadUser(userRequest).getAttributes());


        // code를 통해 구성한 정보
        System.out.println("userRequest clientRegistration : " + userRequest.getClientRegistration());
        // token을 통해 응답받은 회원정보
        System.out.println("oAuth2User : " + oAuth2User);

        return processOAuth2User(userRequest, oAuth2User);
    }


    //구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수

    public OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인 요청~~");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            System.out.println("네이버 로그인 요청~~");
            oAuth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
            System.out.println(oAuth2UserInfo);

        } else if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            System.out.println("카카오 로그인 요청~~");
            oAuth2UserInfo = new KakaoUserInfo((Map) oAuth2User.getAttributes().get("kakao_account"));
            System.out.println(oAuth2UserInfo);
        } else {
            System.out.println("우리는 구글과 페이스북만 지원해요 ㅎㅎ");
        }

        System.out.println("name : " + oAuth2UserInfo.getName());
        System.out.println("email : " + oAuth2UserInfo.getEmail());
        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        if (userOptional.isPresent()) {
            user = userOptional.get();
            user.setEmail(oAuth2UserInfo.getEmail());
            userRepository.save(user);
        } else {
            user = User.builder()
                    .email(oAuth2UserInfo.getEmail())
                    .roles(roles)
                    .provider(oAuth2UserInfo.getProvider())
                    .build();

            userRepository.save(user);
        }


        return new UserInfoUserDetails(user, oAuth2User.getAttributes());
    }

}

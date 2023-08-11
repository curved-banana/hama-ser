package likelion.hamahama.user.config.oauth.provider;


import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo{

    private Map<String, Object> attributes;

    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

//    @Override
//    public String getProviderId() {
//        return (String) attributes.get("id");
//    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }

    @Override
    public String getEmail() {
//        Map<String, Object> result = (Map<String, Object>) oAuth2User.getAttributes().get("response");
//        String email = result.get("email").toString();
//        System.out.println("email : " + email);
        return attributes.get("email").toString();
    }

    @Override
    public String getProvider() {
        return "naver";
    }

}

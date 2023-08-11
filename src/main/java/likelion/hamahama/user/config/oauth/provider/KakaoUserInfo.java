package likelion.hamahama.user.config.oauth.provider;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }


//    @Override
//    public String getProviderId() {
//        return (String) attributes.get("id");
//    }

    @Override
    public String getName() {
        return attributes.get("nickname").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getProvider() {
        return "naver";
    }
}

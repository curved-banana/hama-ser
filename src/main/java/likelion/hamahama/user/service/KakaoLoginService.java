package likelion.hamahama.user.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import likelion.hamahama.user.config.auth.JwtProvider;
import likelion.hamahama.user.entity.RefreshToken;
import likelion.hamahama.user.entity.Role;
import likelion.hamahama.user.entity.User;
import likelion.hamahama.user.repository.RefreshTokenRepository;
import likelion.hamahama.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import static likelion.hamahama.user.entity.Role.ROLE_USER;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoLoginService {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository;


//    @Autowired
//    private RefreshTokenService refreshTokenService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private long refreshTokenValidTime = 50 * 60 * 1000L;

    public String getKakaoAccessToken (String code, HttpServletResponse response) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=f26e0636a3b7f31a8acf97ee86964823"); // TODO REST_API_KEY 입력
            sb.append("&redirect_uri=http://localhost:8088/api/login/oauth2/code/kakao"); // TODO 인가코드 받은 redirect_uri 입력
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            System.out.println("element : " + element);
            String accessToken = element.getAsJsonObject().get("access_token").getAsString();

            br.close();
            bw.close();

            return accessToken;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Map<String, Object> createKakaoUser(String accessToken, HttpServletResponse response) throws Exception {

        String reqURL = "https://kapi.kakao.com/v2/user/me";

        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + accessToken); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            JsonElement kakao_account = element.getAsJsonObject().get("kakao_account");
            JsonElement profile = kakao_account.getAsJsonObject().get("profile");

            System.out.println(kakao_account);
            System.out.println(profile);

            //int id = element.getAsJsonObject().get("id").getAsInt();
            String name = profile.getAsJsonObject().get("nickname").getAsString();
            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();
            String email = "";
            if(hasEmail){
                email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            }



            br.close();


            Optional<User> userEntity = userRepository.findByEmail(email);
            User user;
            List<String> roles = new ArrayList<>();
            roles.add("ROLE_USER");
            if(userEntity.isPresent()) {
                user = userEntity.get();
                user.setEmail(email);
                userRepository.save(user);
            }else{
                user = User.builder()
                        .email(email)
                        .role(ROLE_USER)
                        .provider("kakao")
                        .build();

                userRepository.save(user);
            }
            String access_token = jwtProvider.createAccessToken(email, ROLE_USER);
            String refresh_token = jwtProvider.createRefreshToken(email, ROLE_USER);

            jwtProvider.setHeaderAccessToken(response, access_token);
            jwtProvider.setHeaderRefreshToken(response, refresh_token);

            System.out.println("AccessToken : " + access_token);
            System.out.println("RefreshToken : " + refresh_token);

            Date date = new Date();
            RefreshToken new_refreshToken = RefreshToken.builder()
                    .user(user)
                    .token(refresh_token)
                    .expiryDate(jwtProvider.getRefreshTokenExpTime(refresh_token))//10
                    .build();
            refreshTokenRepository.save(new_refreshToken);

            System.out.println("name : " + name);
            System.out.println("email : " + email);

            Map<String, Object> tokenMap = new HashMap<>();
            tokenMap.put("accessToken", access_token);
            tokenMap.put("refreshToken", refresh_token);

            return tokenMap;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

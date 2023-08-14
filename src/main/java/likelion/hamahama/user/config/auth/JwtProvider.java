package likelion.hamahama.user.config.auth;

import likelion.hamahama.user.repository.RefreshTokenRepository;
import likelion.hamahama.user.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider {

    // 키
    @Value("${jwt.secret}")
    private String secretKey;

    // 어세스 토큰 유효시간 | 1시간
    private long accessTokenValidTime = 1 * 60 * 1000L; // 30 * 60 * 1000L;
    // 리프레시 토큰 유효시간 | 1일
    private long refreshTokenValidTime = 50 * 60 * 1000L;

    @Autowired
    private final UserInfoUserDetailsService userInfoUserDetailsService;
    @Autowired
    private final RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private final UserRepository userRepository;


    // 의존성 주입 후, 초기화를 수행
    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // Access Token 생성.
    public String createAccessToken(String email, List<String> roles){
        return this.createToken(email, roles, accessTokenValidTime);
    }
    // Refresh Token 생성.
    public String createRefreshToken(String email, List<String> roles) {
        return this.createToken(email, roles, refreshTokenValidTime);
    }

    // Create token
    public String createToken(String email,List<String> roles, long tokenValid) {
        Claims claims = Jwts.claims().setSubject(email); // claims 생성 및 payload 설정
        claims.put("roles", roles); // 권한 설정, key/ value 쌍으로 저장
        Date date = new Date();
        return Jwts.builder()
                .setClaims(claims) // 발행 유저 정보 저장
                .setIssuedAt(date) // 발행 시간 저장
                .setExpiration(new Date(date.getTime() + tokenValid)) // 토큰 유효 시간 저장
                .signWith(SignatureAlgorithm.HS256, secretKey) // 해싱 알고리즘 및 키 설정
                .compact(); // 생성
    }

    // JWT 에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userInfoUserDetailsService.loadUserByUsername(this.getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public Date getRefreshTokenExpTime(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration();
    }

    // Request의 Header에서 AccessToken 값을 가져옵니다. "authorization" : "token'
    public String resolveAccessToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("authorization");
        if(headerAuth != null && headerAuth.startsWith("Bearer ") )
            return headerAuth.substring(7);
        return null;
    }
    // Request의 Header에서 RefreshToken 값을 가져옵니다. "authorization" : "token'
    public String resolveRefreshToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("refreshToken");
        if(headerAuth != null && headerAuth.startsWith("Bearer ") )
            return headerAuth.substring(7);
        return null;
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            log.info(e.getMessage());
            return false;
        }
    }

    // 어세스 토큰 헤더 설정
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader("authorization", "Bearer "+ accessToken);
    }

    // 리프레시 토큰 헤더 설정
    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader("refreshToken", "Bearer "+ refreshToken);
    }

    public String resolveToken(String bearerToken){
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
    // RefreshToken 존재유무 확인
    public boolean existsRefreshToken(String refreshToken) {
        return refreshTokenRepository.existsByToken(refreshToken);
    }

    // Email로 권한 정보 가져오기
    public List<String> getRoles(String email) {
        return userRepository.findByEmail(email).get().getRoles();
    }

    public Claims parseClaimsFromRefreshToken(String jsonWebToken){
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jsonWebToken).getBody();
        } catch (SignatureException signatureException) {
            throw new SignatureException("signature key is different", signatureException);
        }
//        catch (ExpiredJwtException expiredJwtException) {
//            throw new ExpiredJwtException(Jwts.header(), claims,"expired token");
//        }
        catch (MalformedJwtException malformedJwtException) {
            throw new MalformedJwtException("malformed token");
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new IllegalArgumentException("using illegal argument like null", illegalArgumentException);
        }
        return claims;
    }
}

package likelion.hamahama.user.filter;

import likelion.hamahama.user.config.auth.JwtProvider;
import likelion.hamahama.user.config.auth.UserInfoUserDetails;
import likelion.hamahama.user.entity.User;
import likelion.hamahama.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private JwtProvider jwtProvider;

    @Autowired
    private UserInfoUserDetails userInfoUserDetails;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    public JwtAuthFilter(JwtProvider jwtTokenProvider) {
        this.jwtProvider = jwtTokenProvider;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("request: " + request);

        String token = "";
        if(jwtProvider.resolveAccessToken(request) != null){
            token = jwtProvider.resolveAccessToken(request);
        }else{
            token = jwtProvider.resolveRefreshToken(request);
        }


        System.out.println("token: " + token);
        if (token != null && jwtProvider.validateToken(token)) {
//            AbstractAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(user, token, userInfoUserDetails.getAuthorities());
//            authenticated.setDetails(new WebAuthenticationDetails(request));


            Authentication auth = jwtProvider.getAuthentication(token);
            System.out.println("auth " + auth);

            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }

}

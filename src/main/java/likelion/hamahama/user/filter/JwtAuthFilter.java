package likelion.hamahama.user.filter;

import likelion.hamahama.user.config.auth.JwtProvider;
import likelion.hamahama.user.config.auth.UserInfoUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private JwtProvider jwtProvider;

    @Autowired
    private UserInfoUserDetails userInfoUserDetails;

    @Autowired
    private UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtProvider jwtTokenProvider) {
        this.jwtProvider = jwtTokenProvider;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("request: " + request);

        String token = jwtProvider.resolveAccessToken(request);
        System.out.println("token: " + token);
        if (token != null && jwtProvider.validateToken(token)) {
            Authentication auth = jwtProvider.getAuthentication(token);

//            String email = jwtProvider.getUserEmail(token);
//
//            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
//            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                    userDetails, null, userDetails.getAuthorities());
//
//            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication((auth));
        }

        filterChain.doFilter(request, response);
    }

}

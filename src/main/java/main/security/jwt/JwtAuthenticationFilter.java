package main.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.security.userDetails.UserDetailsUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtilities jwtUtilities;
    private final UserDetailsUtils userDetails;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtUtilities.getToken(request);

        if (token != null && jwtUtilities.validateToken(token)) {
            String username = jwtUtilities.extractUsername(token);

            UserDetails userDetails = this.userDetails.loadUserByUsername(username);
            if (userDetails != null) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
                log.info("authenticated user :{}", username);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        }
        filterChain.doFilter(request, response);
    }

}
package com.bibek.backend.auth.security;

import com.bibek.backend.auth.helper.UserHelper;
import com.bibek.backend.auth.repositories.UserRepository;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

// Custom JWT filter that runs once per request to validate the token,
// authenticate the user, and set the SecurityContext if the token is valid.
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    // Extracts JWT from the request, validates it, and establishes authentication
    // in the SecurityContext before continuing the filter chain.
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        logger.info("Authorization header: "+ authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                if(!jwtService.isAccessToken(token)) {
                    filterChain.doFilter(request, response);
                    return;
                }

                Jws<Claims> parse = jwtService.parse(token);
                Claims payload = parse.getPayload();

                // we will get user id as in subject we have added userId
                String userId = payload.getSubject();
                UUID userUuid = UserHelper.parseUUID(userId);

                userRepository.findById(userUuid).ifPresent(user -> {
                    if (user.isEnable()) {
                        List<GrantedAuthority> authorities = user.getRoles() == null ? List.of() :
                                user.getRoles()
                                        .stream()
                                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                                        .collect(Collectors.toList());
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                    user.getEmail(),
                                    null,
                                    authorities
                        );

                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        if (SecurityContextHolder.getContext().getAuthentication() == null) {
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    }

                });


            } catch (ExpiredJwtException | MalformedJwtException e) {
                e.printStackTrace();
            } catch (JwtException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}

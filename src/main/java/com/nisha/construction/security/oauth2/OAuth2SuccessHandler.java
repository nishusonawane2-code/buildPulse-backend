package com.nisha.construction.security.oauth2;

import com.nisha.construction.security.entity.User;
import com.nisha.construction.security.jwt.JwtUtil;
import com.nisha.construction.security.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

        private final JwtUtil jwtUtil;
        private final UserRepository userRepository;

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                        Authentication authentication) throws IOException, ServletException {
                OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                String email = oAuth2User.getAttribute("email");

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new ServletException("User not found after OAuth2 login"));

                Map<String, Object> claims = new HashMap<>();
                claims.put("userId", user.getId());
                claims.put("role", user.getRole().name());

                String token = jwtUtil.generateToken(claims, user);

                String targetUrl = UriComponentsBuilder.fromUriString("https://buildplus.vercel.app/oauth2/redirect")
                                .queryParam("token", token)
                                .queryParam("role", user.getRole().name())
                                .build().toUriString();

                getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
}

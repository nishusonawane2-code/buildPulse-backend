package com.nisha.construction.security.oauth2;

import com.nisha.construction.security.entity.AuthProvider;
import com.nisha.construction.security.entity.Role;
import com.nisha.construction.security.entity.User;
import com.nisha.construction.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        return processOAuth2User(oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            // Update existing user info if needed
            user.setName(name);
        } else {
            user = registerNewUser(email, name);
        }

        userRepository.save(user);
        return oAuth2User; // We can wrap this in a custom OAuth2User if we need more data
    }

    private User registerNewUser(String email, String name) {
        return User.builder()
                .username(email) // Using email as username for social login
                .email(email)
                .name(name)
                .password("") // Social users don't have a direct password
                .role(Role.USER)
                .provider(AuthProvider.GOOGLE)
                .build();
    }
}

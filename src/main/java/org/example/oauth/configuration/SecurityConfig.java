package org.example.oauth.configuration;

import org.example.oauth.entity.User;
import org.example.oauth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Autowired
    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                    .mvcMatchers("/")
                        .permitAll()
                    .anyRequest()
                        .authenticated()
                    .and()
                .oauth2Login(oauth2 -> oauth2.userInfoEndpoint(userInfo ->
                        userInfo.userAuthoritiesMapper(userAuthoritiesMapper())));
    }

    private GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return authorities -> {
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            authorities.forEach(authority -> {
                if (authority instanceof OAuth2UserAuthority oAuth2UserAuthority) {
                    Map<String, Object> userAttributes = oAuth2UserAuthority.getAttributes();
                    String username = (String) userAttributes.get("name");
                    String email = (String) userAttributes.get("email");
                    Long id = (Long) userAttributes.get("id");
                    User user = userService.findById(id).orElseGet(() -> {
                        User createUser = User.builder()
                                .id(id)
                                .name(username)
                                .email(email)
                                .role("USER")
                                .build();
                        userService.save(createUser);
                        return createUser;
                    });
                    grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole()));
                }
            });
            return grantedAuthorities;
        };
    }
}

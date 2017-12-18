package org.nowherehub.mcr1;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class Mcr1SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Preferences > Editor > Code Style > Formatter Control
        // @formatter:off
        http
            .csrf().disable() //
            .authorizeRequests()
                .antMatchers(
                        "/favicon.ico",
                        "/swagger-ui.html",
                        "/swagger-resources/**",
                        "/v2/api-docs",
                        "/webjars/**"
                )
                .permitAll()
            .anyRequest()
                .authenticated()
            .and()
            .oauth2Login() //
                .authorizationEndpoint() //
                .and() //
                .userInfoEndpoint()
                    .userAuthoritiesMapper(this.userAuthoritiesMapper())
        ;
        // @formatter:on
    }

    private GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return (authorities) -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

            authorities.forEach(authority -> {
                if (OidcUserAuthority.class.isInstance(authority)) {
                    OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) authority;

                    OidcIdToken idToken = oidcUserAuthority.getIdToken();
                    OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();

                    // Map the claims found in idToken and/or userInfo
                    // to one or more GrantedAuthority's and add it to mappedAuthorities
                    Optional.ofNullable(idToken).ifPresent(token-> {
                        List<String> roles = (List<String>) token.getClaims().getOrDefault("authorities", new ArrayList<>(0));
                        mappedAuthorities.addAll(roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                    });
                } else if (OAuth2UserAuthority.class.isInstance(authority)) {
                    OAuth2UserAuthority oauth2UserAuthority = (OAuth2UserAuthority) authority;

                    List<String> clientRoles = (List<String>) oauth2UserAuthority.getAttributes().getOrDefault("authorities", new ArrayList<String>());
                    mappedAuthorities.addAll(clientRoles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                }
            });

            return mappedAuthorities;
        };
    }

}

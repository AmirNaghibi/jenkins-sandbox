package org.nowherehub.mcr1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;

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
                //.addFilterBefore(keycloakPreAuthActionsFilter(), LogoutFilter.class)
                .addFilterBefore(keycloakAuthenticationProcessingFilter(), BasicAuthenticationFilter.class)
                //.addFilterBefore(keycloakAuthenticatedActionsFilter(), BasicAuthenticationFilter.class)
                //.addFilterAfter(keycloakSecurityContextRequestFilter(), SecurityContextHolderAwareRequestFilter.class)
                .exceptionHandling()
//            .and()
//            .oauth2Login() //
//                .authorizationEndpoint() //
//                .and() //
//                .userInfoEndpoint()
//                    .userAuthoritiesMapper(this.userAuthoritiesMapper())
        ;
        // @formatter:on
    }



    @Bean
    protected KeycloakAuthenticationProcessingFilter keycloakAuthenticationProcessingFilter() throws Exception {
        KeycloakAuthenticationProcessingFilter filter = new KeycloakAuthenticationProcessingFilter(authenticationManagerBean());
        return filter;
    }




//    private GrantedAuthoritiesMapper userAuthoritiesMapper() {
//        return (authorities) -> {
//            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
//
//            authorities.forEach(authority -> {
//                if (OidcUserAuthority.class.isInstance(authority)) {
//                    OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) authority;
//
//                    OidcIdToken idToken = oidcUserAuthority.getIdToken();
//                    OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();
//
//                    // Map the claims found in idToken and/or userInfo
//                    // to one or more GrantedAuthority's and add it to mappedAuthorities
//                    Optional.ofNullable(idToken).ifPresent(token-> {
//                        List<String> roles = (List<String>) token.getClaims().getOrDefault("authorities", new ArrayList<>(0));
//                        mappedAuthorities.addAll(roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
//                    });
//                } else if (OAuth2UserAuthority.class.isInstance(authority)) {
//                    OAuth2UserAuthority oauth2UserAuthority = (OAuth2UserAuthority) authority;
//
//                    List<String> clientRoles = (List<String>) oauth2UserAuthority.getAttributes().getOrDefault("authorities", new ArrayList<String>());
//                    mappedAuthorities.addAll(clientRoles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
//                }
//            });
//
//            return mappedAuthorities;
//        };
//    }

}

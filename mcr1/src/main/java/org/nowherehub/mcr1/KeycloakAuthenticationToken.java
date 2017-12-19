package org.nowherehub.mcr1;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.Assert;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

public class KeycloakAuthenticationToken extends AbstractAuthenticationToken implements Authentication {

    private Principal principal;

    public KeycloakAuthenticationToken(Principal p, Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        principal = p;
        Assert.notNull(p, "KeycloakAccount cannot be null");


        this.setDetails(jwt);

        setAuthenticated(true);
    }


    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }


    public static KeycloakAuthenticationToken from(final Jwt jwt) {


        Principal princ = () -> (jwt.getClaimAsString("sub"));

        List<SimpleGrantedAuthority> roles = new HashSet<>(Optional.ofNullable(jwt.getClaimAsStringList("authorities")).orElse(new ArrayList<>(0))).stream()
                .map(SimpleGrantedAuthority::new).collect(Collectors.toList());


        return new KeycloakAuthenticationToken(princ, jwt, roles);
    }

}

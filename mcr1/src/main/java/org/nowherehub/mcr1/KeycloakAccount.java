package org.nowherehub.mcr1;

import java.security.Principal;
import java.util.Set;

public interface KeycloakAccount {
    Principal getPrincipal();

    Set<String> getRoles();
}
package aetat.six.web.security;

import org.springframework.security.core.GrantedAuthority;

public enum ApplicationRole implements GrantedAuthority {
    ROLE_EKSTERNBRUKER, ROLE_USER;

    @Override
    public String getAuthority() {
        return name();
    }

    @Override
    public String toString() {
        return name();
    }

}

package io.gateway.config;


import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {

        Map<String,Object> realm_access= source.getClaim("realm_access");
        List<GrantedAuthority> authorities = new ArrayList<>();
        if(realm_access!=null){
            List<String > roles=(List<String>)realm_access.get("roles");
            roles.stream().map(
                    role->new SimpleGrantedAuthority("ROLE_"+role.toUpperCase())
            ).forEach(authorities::add);
        }
       return authorities;

    }
}

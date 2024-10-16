package com.document.Documentweb.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private String signerKey = "3QCHBCeWYk5Nvf23KERk8Z45Bv3BHH3HZALSLWKq+gukKZP9ksb9/rvIVIUqqdFN";
    @Autowired
    private CustomeJwtDecoder jwtDecoder;

    private final String[] PUBLIC_ENDPOINT_POST = {"/users",
            "/auth/token",
            "/auth/introspect",
            "/auth/logout",
            "/auth/refresh",
            "/**"
    };
    private final String[] PUBLIC_ENDPOINT_GET = {
            "/**"
    };

    private final String[] PUBLIC_ENDPOINT_PUT = {
            "/**"
    };
    private final String[] PUBLIC_ENDPOINT_DELETE = {
            "/**"
    };


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request ->
                request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINT_POST).permitAll()
                        .requestMatchers(HttpMethod.GET, PUBLIC_ENDPOINT_GET).permitAll()
                        .requestMatchers(HttpMethod.PUT, PUBLIC_ENDPOINT_PUT).permitAll()
                        .requestMatchers(HttpMethod.DELETE, PUBLIC_ENDPOINT_DELETE).permitAll()
/*                        //phân quyền trên endpoints
/*                        //phân quyền trên endpoints
                        .requestMatchers(HttpMethod.GET, "/users").hasRole(Role.ADMIN.name())*/


                        .anyRequest().authenticated()
        );
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer ->
                        jwtConfigurer.decoder(jwtDecoder)//decoder how to validate jwt token
                        .jwtAuthenticationConverter(converter())
                        )
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
        );

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }
/*    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }*/

    @Bean
    JwtAuthenticationConverter converter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(converter);
        return jwtAuthenticationConverter;
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}

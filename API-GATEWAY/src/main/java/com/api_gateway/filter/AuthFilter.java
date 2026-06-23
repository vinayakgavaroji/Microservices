package com.api_gateway.filter;

import com.api_gateway.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private Validator validator;

    public AuthFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            if (validator.predicate.test(exchange.getRequest())) {

                String authHeader = exchange.getRequest()
                        .getHeaders()
                        .getFirst(HttpHeaders.AUTHORIZATION);

                if (authHeader == null) {
                    throw new BadRequestException("Authorization header is missing", HttpStatus.UNAUTHORIZED);
                }

                if (!authHeader.startsWith("Bearer ")) {
                    throw new BadRequestException("Invalid Authorization header format", HttpStatus.UNAUTHORIZED);
                }

                String token = authHeader.substring(7);

                try {
                    jwtUtil.validateToken(token);
                } catch (Exception e) {
                    throw new BadRequestException("Invalid token", HttpStatus.UNAUTHORIZED);
                }
            }

            return chain.filter(exchange);
        };
    }

    public static class Config{

    }

}

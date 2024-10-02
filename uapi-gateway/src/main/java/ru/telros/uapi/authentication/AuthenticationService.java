package ru.telros.uapi.authentication;

import ru.telros.uapi.authentication.dto.AccessTokenResponse;
import ru.telros.uapi.authentication.dto.JwtRequest;
import ru.telros.uapi.authentication.dto.JwtResponseFullDto;

public interface AuthenticationService {
    JwtResponseFullDto login(JwtRequest authenticationRequest);

    AccessTokenResponse getAccessToken(String refreshToken);

    JwtResponseFullDto refreshTokens(String refreshToken);

    JwtAuthentication getAuthenticationInfo();
}
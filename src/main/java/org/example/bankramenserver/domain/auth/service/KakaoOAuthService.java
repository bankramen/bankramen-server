package org.example.bankramenserver.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.bankramenserver.domain.auth.dto.KakaoTokenResponse;
import org.example.bankramenserver.domain.auth.dto.KakaoUserResponse;
import org.example.bankramenserver.domain.auth.exception.KaKaoTokenRequestFailedException;
import org.example.bankramenserver.domain.auth.exception.KaKaoUserInfoRequestFailedException;
import org.example.bankramenserver.domain.user.domain.User;
import org.example.bankramenserver.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoOAuthService {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.auth-url}")
    private String authUrl;

    @Value("${kakao.api-url}")
    private String apiUrl;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;
    private final UserService userService;
    private final JwtService jwtService;

    public String kakaoLogin(String code) {
        KakaoTokenResponse token = getAccessToken(code);
        KakaoUserResponse kakaoUser = getUserInfo(token.accessToken());

        User user = userService.saveOrUpdate(kakaoUser);
        return jwtService.generateToken(user.getId());
    }

    private KakaoTokenResponse getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type",    "authorization_code");
        params.add("client_id",     clientId);
        params.add("redirect_uri",  redirectUri);
        params.add("code",          code);
        params.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        KakaoTokenResponse response = restTemplate.postForObject(
                authUrl + "/oauth/token",
                request,
                KakaoTokenResponse.class
        );

        if (response == null) {
            throw new KaKaoUserInfoRequestFailedException();
        }
        return response;
    }

    private KakaoUserResponse getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        KakaoUserResponse response = restTemplate.exchange(
                apiUrl + "/v2/user/me",
                HttpMethod.GET,
                request,
                KakaoUserResponse.class
        ).getBody();

        if (response == null) {
            throw new KaKaoTokenRequestFailedException();
        }
        return response;
    }
}
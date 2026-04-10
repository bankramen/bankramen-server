package org.example.bankramenserver.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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

        try {
            KakaoTokenResponse response = restTemplate.postForObject(
                    authUrl + "/token",  // ← /oauth/token → /token 으로 수정
                    request,
                    KakaoTokenResponse.class
            );
            if (response == null) {
                throw KaKaoTokenRequestFailedException.EXCEPTION;
            }
            return response;
        } catch (KaKaoTokenRequestFailedException e) {
            throw e;
        } catch (Exception e) {
            log.error("카카오 토큰 요청 실패 상세: {}", e.getMessage());
            log.error("카카오 토큰 요청 실패 cause: {}", e.getCause());
            throw KaKaoTokenRequestFailedException.EXCEPTION;
        }
    }

    private KakaoUserResponse getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            KakaoUserResponse response = restTemplate.exchange(
                    apiUrl + "/v2/user/me",
                    HttpMethod.GET,
                    request,
                    KakaoUserResponse.class
            ).getBody();
            if (response == null) {
                throw KaKaoUserInfoRequestFailedException.EXCEPTION;
            }
            return response;
        } catch (KaKaoUserInfoRequestFailedException e) {
            throw e;
        } catch (Exception e) {
            log.error("카카오 사용자 정보 요청 실패: {}", e.getMessage());
            throw KaKaoUserInfoRequestFailedException.EXCEPTION;
        }
    }

    public String getLoginUrl(String state) {
        return authUrl + "/authorize"
                + "?response_type=code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&state=" + state;
    }
}
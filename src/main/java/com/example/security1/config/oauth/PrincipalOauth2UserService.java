package com.example.security1.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    // 구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        System.out.println("userRequest1 :" + userRequest.getClientRegistration());
        System.out.println("userRequest2 :" + userRequest.getAccessToken());
        System.out.println("userRequest2-1 :" + userRequest.getAccessToken().getTokenValue());
        System.out.println("userRequest3 :" + userRequest.getClientRegistration().getRegistrationId());
        System.out.println("userRequest4 :" + userRequest.getClientRegistration().getClientName());
        System.out.println("userRequest5 :" + super.loadUser(userRequest).getAttributes());

        return super.loadUser(userRequest);
    }
}

package com.example.security1.config.oauth;

import com.example.security1.config.auth.PrincipalDetails;
import com.example.security1.model.User;
import com.example.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    // 구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

//        System.out.println("userRequest1 :" + userRequest.getClientRegistration());
//        System.out.println("userRequest2 :" + userRequest.getAccessToken());
//        System.out.println("userRequest2-1 :" + userRequest.getAccessToken().getTokenValue());
//        System.out.println("userRequest3 :" + userRequest.getClientRegistration().getRegistrationId());
//        System.out.println("userRequest4 :" + userRequest.getClientRegistration().getClientName());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        //구글 로그인버튼 클릭 -> 구글로그인창 -> 로그인완료 -> code를 return(OAuth-Client 라이브러리) -> AccessToken요청
        // userRequest 정보 -> loadUser 함수 호출 -> 구글로부터 회원프로필 받음
//        System.out.println("userRequest5 :" + oAuth2User.getAttributes());

        // 회원가입을 강제로 진행해볼 예정
        String provider = userRequest.getClientRegistration().getClientId();    // google
        String providerId = oAuth2User.getAttribute("sub");               // sub 정보에 해당하는 내용 (=id 라고 봐도 무방. 겹치치않음)
        String username = provider + "_" + providerId;                          // google_123124123123 이런식
//        String password = bCryptPasswordEncoder.encode("겟인데어");
        String email = oAuth2User.getAttribute("email");         //
        String role = "ROLE_USER";

        // 중복된 유저가 있는지 확인(이미 회원가입이 되어있는지 중복체크)
        User userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            userEntity = User.builder()
                    .username(username)
//                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }

        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }

}

package com.example.security1.controller;

import com.example.security1.config.auth.PrincipalDetails;
import com.example.security1.model.User;
import com.example.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    /**
     * 로그인시 현재 로그인 정보 끌어오는 방법은 2가지가 있음. 1. 일반적인 로그인, 2.OAuth를 활용한 로그인(ex. 구글로그인 등)
     * 1. 일반 로그인시
     *  -> @AuthenticationPrincipal PrincipalDetails userDetails /// @AuthenticationPrincipal UserDetails userDetails
     *
     * 2. OAuth 로그인시
     *  -> @AuthenticationPrincipal OAuth2User oauth
     *
     * 위와같이 2가지 방법이 있어서 return 타입을 정해주기 어렵다. (둘다 사용할 경우)
     * 따라서 PrincipalDetails에 UserDetails, OAuth2User 를 implements하면 해결이 가능, 필요할때마다 두가지 경우를 사용이 가능
     *  -> @AuthenticationPrincipal PrincipalDetails principalDetails
     */


    /**
     * 로그인 정보 끌어오는 방법 !!!!!
     *
     * @param authentication
     * @return
     */
    @GetMapping("/test/login")
    public @ResponseBody
    String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails) {
        System.out.println("/test/login ==========");

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        // 현재 로그인 정보 끌어오는 방법 !!!
        System.out.println("authentication:" + authentication.getPrincipal());
        System.out.println("authentication:" + principalDetails.getUser());
        System.out.println("userDetails:" + userDetails.getUsername());
        System.out.println("userDetails:" + userDetails.getUser());

        return "세션정보확인하기";
    }

    /**
     * OAuth로 로그인 정보 끌어오는 방법 !!!
     * @param authentication
     * @return
     */
    @GetMapping("/test/oauth/login")
    public @ResponseBody
    String testOAuthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth) {
        System.out.println("/test/oauth/login ==========");

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // OAuth로 로그인 정보 끌어오는 방법 !!!
        System.out.println("authentication:" + oAuth2User.getAttributes());
        System.out.println("OAuth2User:" + oauth.getAttributes());

        return "OAuth 세션정보확인하기";
    }


    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }

    //OAuth 및 일반 로그인 다 가능
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("pricipalDetails : " + principalDetails.getUser());
        return "user";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public String manager() {
        return "manager";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public @ResponseBody
    String join(User user) {
        System.out.println(user);
        user.setRole("ROLE_USER");

        //비밀번호 암호화 하는 작업, 시큐리티
        //이렇게 하지 않을 경우 비밀번호가 암호화가 안되어있음. 시큐리티 사용 x
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);

        userRepository.save(user);

        return "join";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터정보";
    }

}

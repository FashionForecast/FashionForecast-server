package com.example.fashionforecastbackend.oauth2.service;

import com.example.fashionforecastbackend.member.domain.Member;
import com.example.fashionforecastbackend.member.domain.MemberJoinType;
import com.example.fashionforecastbackend.member.domain.MemberRole;
import com.example.fashionforecastbackend.member.domain.repository.MemberRepository;
import com.example.fashionforecastbackend.oauth2.dto.CustomOAuth2User;
import com.example.fashionforecastbackend.oauth2.dto.KakaoResponse;
import com.example.fashionforecastbackend.oauth2.dto.OAuth2Response;
import com.example.fashionforecastbackend.oauth2.dto.UserDTO;
import java.util.Objects;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    public CustomOAuth2UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);
        OAuth2Response oAuth2Response = null;
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        MemberJoinType joinType = null;
        //추가로 Oauth2가 확장될 수 있음
        if (Objects.equals(registrationId, "kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
            joinType = MemberJoinType.KAKAO;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String username = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();
        System.out.println(username);
        Member existData = memberRepository.findByUsername(username);

        if (existData == null) {

            existData = Member.builder()
                    .username(username)
                    .email(oAuth2Response.getEmail() != null ? oAuth2Response.getEmail() : null)
                    .nickname(oAuth2Response.getName() != null ? oAuth2Response.getName() : null)
                    .joinType(joinType)
                    .role(MemberRole.MEMBER)
                    .build();

            memberRepository.save(existData);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setName(oAuth2Response.getName());
            userDTO.setEmail(oAuth2Response.getEmail());
            userDTO.setAge(oAuth2Response.getAge());
            userDTO.setGender(oAuth2Response.getGender());
            userDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(userDTO);
        }
        else {
            System.out.println("Already exists!");
            existData.updateNickname(oAuth2Response.getName());
            existData.updateEmail(oAuth2Response.getEmail());
            memberRepository.save(existData);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(existData.getUsername());
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole("ROLE_USER");
            return new CustomOAuth2User(userDTO);
        }


    }
}

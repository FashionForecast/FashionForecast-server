package com.example.fashionforecastbackend.oauth2.service.Impl;

import com.example.fashionforecastbackend.member.domain.Member;
import com.example.fashionforecastbackend.member.domain.MemberJoinType;
import com.example.fashionforecastbackend.member.domain.MemberRole;
import com.example.fashionforecastbackend.member.service.Impl.MemberServiceImpl;
import com.example.fashionforecastbackend.oauth2.dto.CustomOAuth2User;
import com.example.fashionforecastbackend.oauth2.dto.KakaoResponse;
import com.example.fashionforecastbackend.oauth2.dto.OAuth2Response;
import com.example.fashionforecastbackend.oauth2.dto.UserDTO;
import com.example.fashionforecastbackend.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserServiceImpl extends DefaultOAuth2UserService implements CustomOAuth2UserService {

    private final MemberServiceImpl memberService;
    private static MemberJoinType joinType;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2Response oAuth2Response = getOAuth2Response(userRequest);
        String username = createUsername(oAuth2Response);

        Optional<Member> optionalMember = memberService.findByUsername(username);

        if (optionalMember.isEmpty()) {
            log.info("새로운 회원입니다. 회원 정보를 저장합니다. : {}", oAuth2Response);
            return registerNewUser(oAuth2Response, username);
        } else {
            log.info("이미 존재하는 회원입니다. 회원 정보를 업데이트합니다. : {}", oAuth2Response);
            return updateExistingUser(optionalMember.get(), oAuth2Response);
        }
    }

    // OAuth2Response 객체 생성 메서드
    private OAuth2Response getOAuth2Response(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        if (Objects.equals(registrationId, "kakao")) {
            joinType = MemberJoinType.KAKAO;
            return new KakaoResponse(oAuth2User.getAttributes());
        }

        throw new IllegalArgumentException("Unsupported OAuth2 provider: " + registrationId);
    }

    // Username 생성 메서드
    private String createUsername(OAuth2Response oAuth2Response) {
        return oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();
    }

    // 새로운 회원 등록 메서드
    private OAuth2User registerNewUser(OAuth2Response oAuth2Response, String username) {
        Member newMember = createNewMember(oAuth2Response, username);
        memberService.save(newMember);

        UserDTO userDTO = createUserDTO(newMember, oAuth2Response);
        return new CustomOAuth2User(userDTO);
    }

    // 기존 회원 업데이트 메서드
    private OAuth2User updateExistingUser(Member existingMember, OAuth2Response oAuth2Response) {
        existingMember.updateNickname(oAuth2Response.getName());
        existingMember.updateEmail(oAuth2Response.getEmail());

        memberService.save(existingMember);

        UserDTO userDTO = createUserDTO(existingMember, oAuth2Response);
        return new CustomOAuth2User(userDTO);
    }

    // 새로운 Member 객체 생성 메서드
    private Member createNewMember(OAuth2Response oAuth2Response, String username) {
        return Member.builder()
                .username(username)
                .email(oAuth2Response.getEmail())
                .nickname(oAuth2Response.getName())
                .joinType(joinType)
                .role(MemberRole.MEMBER)
                .build();
    }

    // UserDTO 생성 메서드
    private UserDTO createUserDTO(Member member, OAuth2Response oAuth2Response) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(member.getUsername());
        userDTO.setName(member.getNickname());
        userDTO.setEmail(member.getEmail());
        userDTO.setAge(oAuth2Response.getAge());
        userDTO.setGender(oAuth2Response.getGender());
        userDTO.setRole("ROLE_USER");
        return userDTO;
    }
}

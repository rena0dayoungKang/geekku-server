package com.kosta.geekku.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.kosta.geekku.config.auth.PrincipalDetails;
import com.kosta.geekku.entity.Role;
import com.kosta.geekku.entity.User;
import com.kosta.geekku.repository.UserRepository;
import com.kosta.geekku.util.ImageUtil;


@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("PrincipalOAuth2UserService===========================");

		System.out.println("getClientRegistration:" + userRequest.getClientRegistration());
		System.out.println("getAccessToken:" + userRequest.getAccessToken());
		System.out.println("getAdditionalParameters:" + userRequest.getAdditionalParameters());

		OAuth2User oAuth2User = super.loadUser(userRequest);
		System.out.println("oAuth2User:" + oAuth2User);
		System.out.println(oAuth2User.getAttributes());
		return processOAuth2User(userRequest, oAuth2User);
	}

	private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
		System.out.println("Service :  =======================================");
		OAuth2UserInfo oAuth2UserInfo = null;
		if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
			System.out.println("카카오 로그인");
			oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
		} else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
			System.out.println("네이버 로그인");
			oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttribute("response"));
		} else if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			System.out.println("구글 로그인");
			oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
		} else {
			System.out.println("카카오, 네이버, 구글만 지원합니다.");	
		}
		
		User user = userRepository.findByProviderAndProviderId(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId());

		if (user != null) {
			user.setEmail(oAuth2UserInfo.getEmail());
			user.setName(oAuth2UserInfo.getName());
			user.setNickname(oAuth2UserInfo.getNickname());
			
			String profileImageUrl = oAuth2UserInfo.getProfileImage();
			if (profileImageUrl != null) {
				byte[] profileImageBytes = ImageUtil.downloadImageAsBytes(profileImageUrl);
				user.setProfileImage(profileImageBytes);
			}
			userRepository.save(user);
		} else {
			String profileImageUrl = oAuth2UserInfo.getProfileImage();
			byte[] profileImageBytes = null;
			if (profileImageUrl != null) {
				profileImageBytes = ImageUtil.downloadImageAsBytes(profileImageUrl);
			}
			User nUser = User.builder()
								.username(oAuth2UserInfo.getProviderId())
								.email(oAuth2UserInfo.getEmail())
								.name(oAuth2UserInfo.getName())
								.nickname(oAuth2UserInfo.getNickname())
								.role(Role.ROLE_USER)
								.type("user")
								.provider(oAuth2UserInfo.getProvider())
								.providerId(oAuth2UserInfo.getProviderId())
								.profileImage(profileImageBytes)
								.build();
			userRepository.save(nUser);
		}
//		return new PrincipalDetails(user, oAuth2User.getAttributes());
		return new PrincipalDetails(oAuth2UserInfo);
	}

}

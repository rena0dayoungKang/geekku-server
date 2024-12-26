package com.kosta.geekku.config.oauth;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {
	
	private Map<String, Object> attributes;

	public KakaoUserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	@Override
	public String getProviderId() {
		return attributes.get("id").toString();
	}

	@Override
	public String getProvider() {
		return "Kakao";
	}

	@Override
	public String getEmail() {
		Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        return kakaoAccount.get("email") != null ? kakaoAccount.get("email").toString() : null;
	}

	@Override
	public String getName() {
		Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
	    return properties != null ? (String) properties.get("nickname") : null;
	}

	@Override
	public String getUsername() {
		return (String)attributes.get("id").toString();
	}

	@Override
	public String getNickname() {
		Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
	    return properties != null ? (String) properties.get("nickname") : null;
	}

	@Override
	public String getProfileImage() {
		Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
	    return properties != null ? (String) properties.get("profile_image") : null;
	}
	
	

}

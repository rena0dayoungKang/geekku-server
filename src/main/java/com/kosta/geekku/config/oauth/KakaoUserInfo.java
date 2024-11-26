package com.kosta.geekku.config.oauth;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {
	
	private Map<String, Object> attributes;

	public KakaoUserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	@Override
	public String getProviderId() {
		return String.valueOf(attributes.get("id"));
	}

	@Override
	public String getProvider() {
		return "Kakao";
	}

	@Override
	public String getEmail() {
		Map<String, Object> kakao_account = (Map<String, Object>)attributes.get("kakao_account");
		return (String)kakao_account.get("email");
	}

	@Override
	public String getName() {
		return (String)attributes.get("profile_nickname");
	}

	@Override
	public String getUsername() {
		return (String)attributes.get("profile_nickname");
	}

}

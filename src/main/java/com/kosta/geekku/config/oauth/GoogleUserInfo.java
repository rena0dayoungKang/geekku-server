package com.kosta.geekku.config.oauth;

import java.util.Map;

public class GoogleUserInfo implements OAuth2UserInfo {
	
	private Map<String, Object> attributes;
	
	public GoogleUserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String getProviderId() {
		return String.valueOf(attributes.get("id"));
	}

	@Override
	public String getProvider() {
		return "Google";
	}

	@Override
	public String getEmail() {
		Map<String, Object> google_account = (Map<String, Object>)attributes.get("google_account");
		return (String)google_account.get("profile");
	}

	@Override
	public String getName() {
		return (String)attributes.get("profile");
	}

	@Override
	public String getUsername() {
		return (String)attributes.get("profile");
	}

}

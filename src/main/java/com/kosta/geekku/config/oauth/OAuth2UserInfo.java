package com.kosta.geekku.config.oauth;

public interface OAuth2UserInfo {
	String getProviderId();
	String getProvider();
	String getEmail();
	String getName();
	String getNickname();
	String getUsername();
	String getProfileImage();
}

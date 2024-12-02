package com.kosta.geekku.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.kosta.geekku.config.oauth.OAuth2UserInfo;
import com.kosta.geekku.entity.Company;
import com.kosta.geekku.entity.User;

import lombok.Data;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

	private User user;
	private Company company;
	private String type;
	private OAuth2UserInfo oAuth2UserInfo;

	public PrincipalDetails(User user) {
		this.user = user;
		this.type = "user";
	}

	public PrincipalDetails(Company company) {
		this.company = company;
		this.type = "company";
	}

	public String getType() {
		return type;
	}

	public PrincipalDetails(OAuth2UserInfo oAuth2UserInfo) {
		this.oAuth2UserInfo = oAuth2UserInfo;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		if (user != null)
			collect.add(() -> user.getRole().toString());
		else if (company != null)
			collect.add(() -> company.getRole().toString());
		return collect;
	}

	@Override
	public String getPassword() {
		if (user != null)
			return user.getPassword();
		else if (company != null)
			return company.getPassword();
		else
			return null;
	}

	@Override
	public String getUsername() {
		if (user != null) {
			return user.getUsername();
		} else if (oAuth2UserInfo != null) {
			return oAuth2UserInfo.getUsername();
		} else if (company != null) {
			return company.getCompanyName();
		}
		return "username";
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getName() {
		if (user != null) {
			return user.getName(); 
		} else if (oAuth2UserInfo != null) {
			return oAuth2UserInfo.getName();
		} else if (company != null) {
			return company.getCompanyName(); 
		}
		return "name";
	}

	@Override
	public Map<String, Object> getAttributes() {
		return null;
	}
}

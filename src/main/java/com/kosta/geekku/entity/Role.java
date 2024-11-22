package com.kosta.geekku.entity;

public enum Role {
	ROLE_USER, 	//개인
	ROLE_COMPANY; //기업
	
	public String toString() {
        return name();
    }
}

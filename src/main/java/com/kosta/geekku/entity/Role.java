package com.kosta.geekku.entity;

public enum Role {
	ROLE_USER, 	//개인
	ROLE_COMPANY; //기업
//	ROLE_INTERIOR;
	
	public String toString() {
        return name();
    }
}

package com.kosta.geekku.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

@Configuration
public class FCMConfig {
	 
	@Bean
	FirebaseMessaging firebaseMessaging() throws IOException {
		ClassPathResource resource = new ClassPathResource("firebase/kosta-2424-25fc036013ba.json");
		InputStream refreshToken = resource.getInputStream();
		FirebaseApp firebaseApp = null;
		List<FirebaseApp> firebaseAppList = FirebaseApp.getApps();
		
		if(firebaseAppList != null && !firebaseAppList.isEmpty()) { //firebase 어플리케이션 있는지 확인
			for(FirebaseApp app : firebaseAppList) {
				if(app.getName().equals(FirebaseApp.DEFAULT_APP_NAME)) {
					firebaseApp = app;
					break;
				}
			}
		} else { // 없으면 생성
			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(refreshToken))
					.build();
			firebaseApp = FirebaseApp.initializeApp(options);
		}
		return FirebaseMessaging.getInstance(firebaseApp);
	}
}

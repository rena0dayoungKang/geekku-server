package com.kosta.geekku.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EstateNumberService {
	// 부동산중개업자조회 API사용하는 서비스

	@Value("${vworld.api.key}")
	private String apiKey;

	@Value("${vworld.api.base-url}")
	private String baseUrl;
	
	@Value("${vworld.api.domain}")
	private String vdomain;

	public void vworldSettings() {
		//System.out.println("vworld api key init success");
		//System.out.println("vworld base-Url : " + baseUrl);
	}

	public String searchEstate(String bsnmCmpnm, String brkrNm, String jurirno, int pageNo, int size) throws Exception {
		StringBuilder parameter = new StringBuilder();
		parameter.append("?key=").append(URLEncoder.encode(apiKey, "UTF-8"));
		parameter.append("&format=json");
		parameter.append("&domain=").append(URLEncoder.encode(vdomain, "UTF-8"));

		if (bsnmCmpnm != null && !bsnmCmpnm.isEmpty()) {
			parameter.append("&bsnmCmpnm=").append(URLEncoder.encode(bsnmCmpnm, "UTF-8"));
		}
		if (brkrNm != null && !brkrNm.isEmpty()) {
			parameter.append("&brkrNm=").append(URLEncoder.encode(brkrNm, "UTF-8"));
		}
		if (jurirno != null && !jurirno.isEmpty()) {
			parameter.append("&jurirno=").append(URLEncoder.encode(jurirno, "UTF-8"));
		}
		if (pageNo > 1) {
			parameter.append("&pageNo=").append(pageNo);
		}
		if (size >= 0) {
			parameter.append("&size=").append(size);
		}

		// API호출
		URL url = new URL(baseUrl + "/getEBBrokerInfo" + parameter.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/json");
		int responseCode = conn.getResponseCode();
		//System.out.println("Response code: " + responseCode);

		BufferedReader rd;
		if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}

		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();
		conn.disconnect();
		//System.out.println(sb.toString());		

		return sb.toString();	//API응답반환
	}

}

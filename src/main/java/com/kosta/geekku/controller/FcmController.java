package com.kosta.geekku.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.geekku.dto.MessageDto;
import com.kosta.geekku.service.FcmMessageService;

@RestController
public class FcmController {
	
	@Autowired
	private FcmMessageService fcmMessageService;
	
	@PostMapping("/fcmToken")
	public ResponseEntity<String> fcmToken(@RequestBody Map<String,String> param) {
		System.out.println(param);
		fcmMessageService.registFcmToken(param.get("username"),param.get("fcmToken"));
		return new ResponseEntity<String>("true", HttpStatus.OK);
	}
	
	@PostMapping("/alarms")
	public ResponseEntity<List<MessageDto>> alarms(@RequestBody Map<String,String> param) {
		return new ResponseEntity<List<MessageDto>>(fcmMessageService.getAlarmList(param.get("username")),HttpStatus.OK);
	}
	
	@GetMapping("/confirm/{num}")
	public ResponseEntity<Boolean> confirmAlarm(@PathVariable Integer num) {
		Boolean confirm = fcmMessageService.confirmAlarm(num);
		return new ResponseEntity<Boolean>(confirm, HttpStatus.OK);
	}
	
	@PostMapping("/confirmAll")
	public ResponseEntity<Boolean> confirmAlarmAll(@RequestBody Map<String, List<Integer>> param) {
		System.out.println(param);
		Boolean confirm = fcmMessageService.confirmAlarmAll(param.get("alarmList"));
		return new ResponseEntity<Boolean>(confirm, HttpStatus.OK);
	}
	
	@PostMapping("/sendAlarm")
	public ResponseEntity<Boolean> sendAlarm(@RequestBody MessageDto messageDto) {
		Boolean sendSucces = fcmMessageService.sendAlarm(messageDto);
		return new ResponseEntity<Boolean>(sendSucces, HttpStatus.OK);
	}
	
}

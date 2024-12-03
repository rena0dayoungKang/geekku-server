package com.kosta.geekku.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.kosta.geekku.dto.HouseAnswerDto;
import com.kosta.geekku.dto.InteriorAnswerDto;
import com.kosta.geekku.dto.InteriorRequestDto;
import com.kosta.geekku.dto.MessageDto;
import com.kosta.geekku.dto.OnestopAnswerDto;
import com.kosta.geekku.entity.AlarmUser;
import com.kosta.geekku.entity.Company;
import com.kosta.geekku.entity.User;
import com.kosta.geekku.repository.AlarmUserRepository;
import com.kosta.geekku.repository.CompanyRepository;
import com.kosta.geekku.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FcmMessageService {
	private final FirebaseMessaging firebaseMessaging;
	private final UserRepository userRepository;
	private final AlarmUserRepository alarmRepository;
	private final CompanyRepository companyRepository;

	public Boolean sendHouseAnswer(HouseAnswerDto houseAnswerDto) throws Exception {
		// 1. 수신자 fcmToken 가져오기
		User user = userRepository.findById(houseAnswerDto.getUserId()).orElseThrow(() -> new Exception("User 오류"));
		String fcmToken = user.getFcmToken();
		if (fcmToken == null || fcmToken.trim().length() == 0) {
			System.out.println("FCM Token 오류");
			return false;
		}
		// 2. AlarmTable에 저장
		AlarmUser alarm = AlarmUser.builder().user(user.getUserId()) // UUID 타입 전달
				.company(Company.builder().companyId(houseAnswerDto.getCompanyId()).build())
				.message(houseAnswerDto.getContent()).status(false).type("house")
				.detailPath(houseAnswerDto.getAnswerHouseNum()).title(houseAnswerDto.getTitle()).build();

		alarmRepository.save(alarm);

		// 3. Alarm 전성
		Message message = Message.builder().setToken(fcmToken).putData("num", alarm.getUserAlarmNum() + "")
				.putData("message", alarm.getMessage()).putData("type", alarm.getType())
				.putData("detailPath", alarm.getDetailPath() + "").putData("sender", houseAnswerDto.getCompanyName())
				.putData("title", alarm.getTitle()).build();

		try {
			firebaseMessaging.send(message);
			return true;
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Boolean sendInteriorAllAnswer(InteriorAnswerDto interiorAnswerDto) throws Exception {
		// 1. 수신자 fcmToken 가져오기
		User user = userRepository.findById(interiorAnswerDto.getUserId()).orElseThrow(() -> new Exception("User 오류"));
		String fcmToken = user.getFcmToken();
		if (fcmToken == null || fcmToken.trim().length() == 0) {
			System.out.println("FCM Token 오류");
			return false;
		}

		// 2. AlarmTable에 저장
		AlarmUser alarm = AlarmUser.builder().user(user.getUserId()) // UUID 타입 전달
				.company(Company.builder().companyId(interiorAnswerDto.getCompanyId()).build())
				.message(interiorAnswerDto.getTitle()).status(false).type("interior")
				.detailPath(interiorAnswerDto.getAnswerAllNum()).build();

		alarmRepository.save(alarm);

		// 3. Alarm 전성
		Message message = Message.builder().setToken(fcmToken).putData("num", alarm.getUserAlarmNum() + "")
				.putData("message", alarm.getMessage()).putData("type", alarm.getType())
				.putData("detailPath", alarm.getDetailPath() + "").putData("sender", interiorAnswerDto.getCompanyName())
				.putData("title", alarm.getTitle()).build();

		try {
			firebaseMessaging.send(message);
			return true;
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Boolean sendOnestopAnswer(OnestopAnswerDto onestopAnswerDto) throws Exception {
		// 1. 수신자 fcmToken 가져오기
		User user = userRepository.findById(onestopAnswerDto.getUserId()).orElseThrow(() -> new Exception("User 오류"));
		String fcmToken = user.getFcmToken();
		if (fcmToken == null || fcmToken.trim().length() == 0) {
			System.out.println("FCM Token 오류");
			return false;
		}

		// 3. AlarmTable에 저장
		AlarmUser alarm = AlarmUser.builder().user(user.getUserId()) // UUID 타입 전달
				.company(Company.builder().companyId(onestopAnswerDto.getCompanyId()).build())
				.message(onestopAnswerDto.getTitle()).status(false).type("interior")
				.detailPath(onestopAnswerDto.getAnswerOnestopNum()).build();

		alarmRepository.save(alarm);

		// 4. Alarm 전성
		Message message = Message.builder().setToken(fcmToken).putData("num", alarm.getUserAlarmNum() + "")
				.putData("message", alarm.getMessage()).putData("type", alarm.getType())
				.putData("detailPath", alarm.getDetailPath() + "").putData("sender", onestopAnswerDto.getCompanyName())
				.putData("title", alarm.getTitle()).build();

		try {
			firebaseMessaging.send(message);
			return true;
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
			return false;
		}

	}

	public Boolean sendInteriorRequest(InteriorRequestDto interiorRequestDto) throws Exception {
		// 1. 수신자 fcmToken 가져오기
		// 수신자가 인테리어 회사이기 때문에 해당 회사의 토큰을 가져온다.
		Company company = companyRepository.findById(interiorRequestDto.getCompanyId())
				.orElseThrow(() -> new Exception("Company 오류"));
		String fcmToken = company.getFcmToken();
		if (fcmToken == null || fcmToken.trim().length() == 0) {
			System.out.println("FCM Token 오류");
			return false;
		}

		// 3. AlarmTable에 저장
		AlarmUser alarm = AlarmUser.builder().user(interiorRequestDto.getUserId()) // UUID 타입 전달
				.company(company).message(interiorRequestDto.getName() + "님으로부터 인테리어 문의").status(false).type("request")
				.detailPath(interiorRequestDto.getRequestNum()).build();

		alarmRepository.save(alarm);

		// 4. Alarm 전송
		Message message = Message.builder().setToken(fcmToken).putData("num", alarm.getUserAlarmNum() + "")
				.putData("message", alarm.getMessage()).putData("type", alarm.getType())
				.putData("detailPath", alarm.getDetailPath() + "").putData("sender", interiorRequestDto.getName())
				.putData("title", alarm.getTitle()).build();

		try {
			firebaseMessaging.send(message);
			return true;
		} catch (FirebaseMessagingException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 알람목록 조회(미확인것만),개인
	public List<MessageDto> getUserAlarmList2(UUID userId) {
		List<AlarmUser> alarmList = alarmRepository.findByUser_UserIdAndStatusFalse(userId);
		List<MessageDto> A = alarmList.stream()
				.map(alarm -> MessageDto.builder().num(alarm.getUserAlarmNum()).receiver(alarm.getUser().getUserId())
						.message(alarm.getMessage()).createAt(alarm.getCreatedAt())
						.sender(alarm.getCompany().getCompanyId()).type(alarm.getType())
						.detailPath(alarm.getDetailPath()).title(alarm.getTitle()).build())
				.collect(Collectors.toList());
		System.out.println(A);
		return A;
	}

	// 알람목록 조회(미확인것만),인테리어 회사 수정해야함
	public List<MessageDto> getCompanyAlarmList(UUID comapnyId) {
		List<AlarmUser> alarmList = alarmRepository.findByCompany_CompanyIdAndStatusFalse(comapnyId);
		return alarmList.stream()
				.map(alarm -> MessageDto.builder().num(alarm.getUserAlarmNum()).receiver(alarm.getUser().getUserId())
						.message(alarm.getMessage()).createAt(alarm.getCreatedAt())
						.sender(alarm.getCompany().getCompanyId()).type(alarm.getType())
						.detailPath(alarm.getDetailPath()).title(alarm.getTitle()).build())
				.collect(Collectors.toList());
	}

	// sss
	 public List<MessageDto> getUserAlarmList(UUID userId) {
	        // Repository에서 MessageDto 리스트를 바로 반환
	        List<MessageDto> alarmList = alarmRepository.findAlarmsWithUserAndCompany(userId);

	        // 조회된 알람 리스트 출력 (디버깅용)
	        alarmList.forEach(System.out::println);

	        return alarmList;
	    }

	// 특정알람 확인(알람번호)
	public Boolean confirmAlarm(Integer alarmNum) {
		Optional<AlarmUser> oalarm = alarmRepository.findById(alarmNum);
		System.out.println(oalarm.get());
		if (oalarm.isEmpty()) {
			System.out.println("알람번호 오류");
			return false;
		}
		AlarmUser alarm = oalarm.get();
		alarm.setStatus(true);
		alarmRepository.save(alarm);
		return true;
	}

	// 프론트에서 받은 fcmToken DB에 저장
	public void registUserFcmToken(String userId, String fcmToken) throws Exception {
		User user = userRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new Exception("User Error"));

		user.setFcmToken(fcmToken);
		userRepository.save(user);
	}

	// 프론트에서 받은 fcmToken DB에 저장
	public void registCompanyFcmToken(String companyId, String fcmToken) throws Exception {
		Company company = companyRepository.findById(UUID.fromString(companyId))
				.orElseThrow(() -> new Exception("Company Err"));

		company.setFcmToken(fcmToken);
		companyRepository.save(company);
	}
}
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

	/** 집꾸 알림 */
	public Boolean sendHouseAnswer(HouseAnswerDto houseAnswerDto) throws Exception {
		// 1. 수신자 fcmToken 가져오기
		User user = userRepository.findById(houseAnswerDto.getUserId()).orElseThrow(() -> new Exception("User 오류"));
		String fcmToken = user.getFcmToken();
		if (fcmToken == null || fcmToken.trim().length() == 0) {
			//System.out.println("FCM Token 오류");
			return false;
		}
		// 2. AlarmTable에 저장
		AlarmUser alarm = AlarmUser.builder().user(user.getUserId())
				.company(Company.builder().companyId(houseAnswerDto.getCompanyId()).build())
				.message(houseAnswerDto.getContent()).status(false).type("house")
				.detailPath(houseAnswerDto.getHouseNum()).title(houseAnswerDto.getTitle()).build();
		alarmRepository.save(alarm);
		// 3. Alarm 전송
		Company company = companyRepository.findById(houseAnswerDto.getCompanyId()).get(); // 테스트 해봐야함
		if (company.getFcmToken().equals(fcmToken))
			return true;

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

	/** 방꾸 알림 */
	/** TODO */
	public Boolean sendInteriorAllAnswer(InteriorAnswerDto interiorAnswerDto) throws Exception {
		// 1. 수신자 fcmToken 가져오기
		User user = userRepository.findById(interiorAnswerDto.getUserId()).orElseThrow(() -> new Exception("User 오류"));
		String fcmToken = user.getFcmToken();
		if (fcmToken == null || fcmToken.trim().length() == 0) {
			//System.out.println("FCM Token 오류");
			return false;
		}
		// 2. AlarmTable에 저장
		AlarmUser alarm = AlarmUser.builder().user(user.getUserId()) // UUID 타입 전달
				.company(Company.builder().companyId(interiorAnswerDto.getCompanyId()).build())
				.message(interiorAnswerDto.getContent()).status(false).type("interior")
				.detailPath(interiorAnswerDto.getRequestAllNum()).title(interiorAnswerDto.getTitle()).build();

		alarmRepository.save(alarm);
		// 3. Alarm 전송
		Company company = companyRepository.findById(interiorAnswerDto.getCompanyId()).get();
		if (company.getFcmToken().equals(fcmToken))
			return true;

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

	/** onestop 답변이 달렸을 때, 개인에게 알림 가는 함수 */
	/** TODO */
	public Boolean sendOnestopAnswer(OnestopAnswerDto onestopAnswerDto) throws Exception {
		// 1. 수신자 fcmToken 가져오기
		User user = userRepository.findById(onestopAnswerDto.getUserId()).orElseThrow(() -> new Exception("User 오류"));
		String fcmToken = user.getFcmToken();
		if (fcmToken == null || fcmToken.trim().length() == 0) {
			//System.out.println("FCM Token 오류");
			return false;
		}
		// 2. AlarmTable에 저장
		AlarmUser alarm = AlarmUser.builder().user(user.getUserId()) // UUID 타입 전달
				.company(Company.builder().companyId(onestopAnswerDto.getCompanyId()).build())
				.message(onestopAnswerDto.getTitle()).status(false).type("interior")
				.detailPath(onestopAnswerDto.getAnswerOnestopNum()).build();

		alarmRepository.save(alarm);
		// 3. Alarm 전송
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

	/** 인테리어 신청할 때, 인테리어 업자에게 알림 보내는 함수 */
	public Boolean sendInteriorRequest(InteriorRequestDto requestDto) throws Exception {
		// 1. 수신자 fcmToken 가져오기
		// 수신자가 인테리어 회사이기 때문에 해당 회사의 토큰을 가져온다.

		Company company = companyRepository.findById(requestDto.getCompanyId())
				.orElseThrow(() -> new Exception("Company 오류"));
		String fcmToken = company.getFcmToken();
		if (fcmToken == null || fcmToken.trim().length() == 0) {
			//System.out.println("FCM Token 오류");
			return false;
		}

		// 2. AlarmTable에 저장
		AlarmUser alarm = AlarmUser.builder().user(requestDto.getUserId()) // UUID 타입 전달
				.company(company).message("<p>" + requestDto.getName() + "님으로부터 인테리어 문의" + "</p>").status(false)
				.type("request").title("인테리어 문의가 왔습니다").detailPath(requestDto.getRequestNum()).build();
		alarmRepository.save(alarm);

		// 3. Alarm 전송
		User user = userRepository.findById(requestDto.getUserId()).get();
		if (user.getFcmToken().equals(fcmToken))
			return true;

		Message message = Message.builder().setToken(fcmToken).putData("num", alarm.getUserAlarmNum() + "")
				.putData("message", alarm.getMessage()).putData("type", alarm.getType())
				.putData("detailPath", alarm.getDetailPath() + "").putData("sender", requestDto.getName())
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
	public List<MessageDto> getUserAlarmList(UUID userId) {
		// Repository에서 MessageDto 리스트를 바로 반환
		List<AlarmUser> alarmList = alarmRepository.findByUser_UserIdAndTypeNotAndStatusFalse(userId, "request");
		// List<AlarmUser> alarmList =
		// alarmRepository.findByUser_UserIdAndTypeInAndStatusFalse(userId,
		// Arrays.asList("house", "interior", "oneStop"));

		return alarmList.stream()
				.map(alarm -> MessageDto.builder().num(alarm.getUserAlarmNum()).receiver(alarm.getUser().getUserId())
						.message(alarm.getMessage()).companyName(alarm.getCompany().getCompanyName())
						.username(alarm.getUser().getUsername()).createAt(alarm.getCreatedAt())
						.sender(alarm.getCompany().getCompanyId()).type(alarm.getType())
						.detailPath(alarm.getDetailPath()).title(alarm.getTitle()).build())
				.collect(Collectors.toList());
	}

	// 알람목록 조회(미확인것만),인테리어
	public List<MessageDto> getCompanyAlarmList(UUID comapnyId) {
		List<AlarmUser> alarmList = alarmRepository.findByCompany_CompanyIdAndTypeAndStatusFalse(comapnyId, "request");
//		System.out.println("~~~~~~~~~~~~~~~~~~~~~");
//		System.out.println(alarmList);
		return alarmList.stream()
				.map(alarm -> MessageDto.builder().num(alarm.getUserAlarmNum())
						.receiver(alarm.getCompany().getCompanyId()).companyName(alarm.getCompany().getCompanyName())
						.username(alarm.getUser().getUsername()).message(alarm.getMessage())
						.createAt(alarm.getCreatedAt()).sender(alarm.getUser().getUserId()).type(alarm.getType())
						.detailPath(alarm.getDetailPath()).title(alarm.getTitle()).build())
				.collect(Collectors.toList());
	}

	// 특정알람 확인(알람번호)
	public Boolean confirmAlarm(Integer alarmNum) {
		Optional<AlarmUser> oalarm = alarmRepository.findById(alarmNum);
//		System.out.println(oalarm.get());
		if (oalarm.isEmpty()) {
			//System.out.println("알람번호 오류");
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
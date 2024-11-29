/*
 * package com.kosta.geekku.controller;
 * 
 * import java.util.List; import java.util.Map; import java.util.UUID;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.http.HttpStatus; import
 * org.springframework.http.ResponseEntity; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.PathVariable; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.RequestBody; import
 * org.springframework.web.bind.annotation.RestController;
 * 
 * import com.kosta.geekku.dto.MessageDto; import
 * com.kosta.geekku.service.FcmMessageService;
 * 
 * @RestController public class FcmController {
 * 
 * @Autowired private FcmMessageService fcmMessageService;
 * 
 * @PostMapping("/fcmToken") public ResponseEntity<String> fcmToken(@RequestBody
 * Map<String,String> param) { String type = param.get("type");
 * System.out.println(param); try { if (type.equals("user")) {
 * fcmMessageService.registUserFcmToken(param.get("userId"),
 * param.get("fcmToken")); } else {
 * fcmMessageService.registCompanyFcmToken(param.get("userId"),
 * param.get("fcmToken")); } return new ResponseEntity<String>("true",
 * HttpStatus.OK); } catch(Exception e) { e.printStackTrace(); return new
 * ResponseEntity<String>("false", HttpStatus.BAD_REQUEST); } }
 * 
 * @PostMapping("/userAlarms") public ResponseEntity<List<MessageDto>>
 * userAlarms(@RequestBody Map<String,String> param) { try {
 * System.out.println(param.get("userId")); return new
 * ResponseEntity<List<MessageDto>>(fcmMessageService.getUserAlarmList(UUID.
 * fromString(param.get("userId"))),HttpStatus.OK); } catch(Exception e) {
 * e.printStackTrace(); return new
 * ResponseEntity<List<MessageDto>>(HttpStatus.BAD_REQUEST); } }
 * 
 * @PostMapping("/companyAlarms") public ResponseEntity<List<MessageDto>>
 * companyAlarms(@RequestBody Map<String,String> param) { try { return new
 * ResponseEntity<List<MessageDto>>(fcmMessageService.getCompanyAlarmList(UUID.
 * fromString(param.get("userId"))),HttpStatus.OK); } catch(Exception e) {
 * e.printStackTrace(); return new
 * ResponseEntity<List<MessageDto>>(HttpStatus.BAD_REQUEST); } }
 * 
 * @GetMapping("/confirm/{num}") public ResponseEntity<Boolean>
 * confirmAlarm(@PathVariable Integer num) { Boolean confirm =
 * fcmMessageService.confirmAlarm(num); return new
 * ResponseEntity<Boolean>(confirm, HttpStatus.OK); } }
 * 
 */
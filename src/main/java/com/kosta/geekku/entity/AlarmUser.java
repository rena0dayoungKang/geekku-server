package com.kosta.geekku.entity;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@DynamicInsert
public class AlarmUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userAlarmNum; // 알림 고유 번호
    
    @Column(nullable = false)
    private String message; // 알림 메시지

    @CreationTimestamp
    private Timestamp createdAt; // 생성 시간

    @Column(nullable = false)
    private boolean status; // 알림 상태 (0: 미확인, 1: 확인)
 
    // User와의 연관 관계 설정 : house, interior, onestop
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false) // userId 외래 키로 매핑
    private User user;

    // Company와의 연관 관계 설정 : request
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyId", nullable = true) // companyId 외래 키로 매핑
    private Company company;

    @Column(nullable = true)
    private String type; // 알림 유형 (예: house, interior, onestop, request 등)

    //알람을 누르면 상세로 가기위해 
    //(answer_house_num, onestop_answer_num, interior_all_answer_num, interior_request_num)
    //answer_house_num : 집꾸하기 답변 달릴때 
    //interior_all_answer_num : 방꾸하기 답변 달릴때
    //onestop_answer_num : 한번에꾸하기 답변 달릴때
    //interior_request_num : 인테리어 1대1 문의 신청이 들어왔을 때    
    @Column(nullable = true)
    private Integer detailPath; 

    @CreationTimestamp
    private Timestamp createDate; // 알림 생성 날짜

    // 추가: 빌더에서 UUID를 사용할 수 있도록 설정
    public static class AlarmUserBuilder {
        public AlarmUserBuilder user(UUID userId) {
            this.user = new User(); // User 객체 생성
            this.user.setUserId(userId); // User 엔티티에 userId 설정
            return this;
        }
    }
}

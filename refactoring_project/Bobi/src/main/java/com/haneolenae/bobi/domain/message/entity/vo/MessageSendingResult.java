package com.haneolenae.bobi.domain.message.entity.vo;

import com.haneolenae.bobi.domain.message.entity.MessageCustomer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MessageSendingResult {
    private final CustomerInfo customerInfo;// 수신자 정보
    private boolean success;             // 메시지 전송 성공 여부
    private String errorMessage;         // 실패 시 오류 메시지 (성공 시 null)


    public static MessageSendingResult success(CustomerInfo customerInfo) {
        return new MessageSendingResult(customerInfo, true, null);
    }

    // ✅ 실패한 메시지 전송 결과 생성
    public static MessageSendingResult failure(CustomerInfo customerInfo, String errorMessage) {
        return new MessageSendingResult(customerInfo, false, errorMessage);
    }
}
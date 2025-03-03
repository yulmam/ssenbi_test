package com.haneolenae.bobi.domain.message.infrastructure;

import com.haneolenae.bobi.domain.customer.entity.Customer;
import com.haneolenae.bobi.domain.member.entity.Member;
import com.haneolenae.bobi.domain.message.entity.MessageCustomer;
import com.haneolenae.bobi.domain.message.entity.vo.CustomerInfo;
import com.haneolenae.bobi.domain.message.entity.vo.MessageSendingResult;
import com.haneolenae.bobi.domain.message.service.port.MessageSender;
import com.haneolenae.bobi.global.dto.ApiType;
import com.haneolenae.bobi.global.exception.ApiException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class MessageSenderImpl implements MessageSender {
    @Value("${coolsms.senderPhoneNumber}")
    private String senderPhoneNumber;
    private final DefaultMessageService coolSmsService;

    @Override
    public void sendMessage(String receiverPhone, String messageContent) {
        Message coolMessage = new net.nurigo.sdk.message.model.Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        coolMessage.setFrom(senderPhoneNumber);
        coolMessage.setTo(receiverPhone);
        coolMessage.setText(messageContent);

        SingleMessageSentResponse response = coolSmsService.sendOne(new SingleMessageSendingRequest(coolMessage));

        if (!response.getStatusCode().equals("2000")) {
            throw new ApiException(ApiType.EXTERNAL_MESSAGE_SERVICE_ERROR);
        }
    }

    @Async("asyncMessageExecutor")
    public CompletableFuture<MessageSendingResult> sendMessagesAsync(String messageContent, String senderPhone, CustomerInfo customerInfo) {

        Message coolMessage = new net.nurigo.sdk.message.model.Message();
        coolMessage.setFrom(senderPhoneNumber);
        coolMessage.setTo(customerInfo.getPhoneNumber());
        coolMessage.setText(messageContent);
        SingleMessageSentResponse response = coolSmsService.sendOne(new SingleMessageSendingRequest(coolMessage));
        if(response.getStatusCode().equals("2000"))
            return CompletableFuture.completedFuture(MessageSendingResult.success(customerInfo));
        else
            return CompletableFuture.completedFuture(MessageSendingResult.failure(customerInfo, response.getStatusMessage()));
    }
}

package com.haneolenae.bobi.domain.message.infrastructure;


import com.haneolenae.bobi.domain.message.entity.vo.CustomerInfo;
import com.haneolenae.bobi.domain.message.entity.vo.MessageSendingResult;
import com.haneolenae.bobi.domain.message.service.port.MessageSender;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Primary
@Component
public class FakeMessageSenderImpl implements MessageSender {
    @Override
    public void sendMessage(String receiverPhone, String messageContent) {
        try{
            System.out.println("뭐하냐?");
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    @Async("testExecutor")
    public CompletableFuture<MessageSendingResult> sendMessagesAsync(String messageContent, String senderPhone, CustomerInfo customerInfo) {
        try {
            Thread.sleep(1000); // 1초 지연 (비동기 시뮬레이션)
            System.out.println("뭐하냐?");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return CompletableFuture.completedFuture(MessageSendingResult.success(customerInfo));
    }

}

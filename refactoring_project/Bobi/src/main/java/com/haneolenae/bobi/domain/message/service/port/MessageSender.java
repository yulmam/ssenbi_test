package com.haneolenae.bobi.domain.message.service.port;

import com.haneolenae.bobi.domain.message.entity.vo.CustomerInfo;
import com.haneolenae.bobi.domain.message.entity.vo.MessageSendingResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface MessageSender {
    void sendMessage(String receiverPhone, String messageContent);
    CompletableFuture<MessageSendingResult> sendMessagesAsync(String messageContent, String senderPhone, CustomerInfo customerInfo);
}

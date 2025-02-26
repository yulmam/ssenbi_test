package com.haneolenae.bobi.domain.message.service.port;

import java.util.List;

public interface MessageSender {
    void sendMessage(String receiverPhone, String messageContent);
}

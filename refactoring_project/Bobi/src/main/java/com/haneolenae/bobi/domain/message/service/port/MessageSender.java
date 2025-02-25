package com.haneolenae.bobi.domain.message.service.port;

import java.util.List;

public interface MessageSender {
    public void sendMessage(String receiverPhone, String messageContent);
}

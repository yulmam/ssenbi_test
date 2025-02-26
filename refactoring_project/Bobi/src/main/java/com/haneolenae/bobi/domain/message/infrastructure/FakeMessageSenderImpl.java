package com.haneolenae.bobi.domain.message.infrastructure;


import com.haneolenae.bobi.domain.message.service.port.MessageSender;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class FakeMessageSenderImpl implements MessageSender {
    @Override
    public void sendMessage(String receiverPhone, String messageContent) {
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}

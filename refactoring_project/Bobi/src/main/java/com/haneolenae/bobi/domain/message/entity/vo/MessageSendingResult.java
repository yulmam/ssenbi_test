package com.haneolenae.bobi.domain.message.entity.vo;

import com.haneolenae.bobi.domain.message.entity.MessageCustomer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MessageSendingResult {
    private final List<MessageCustomer> successCustomers;
    private final List<String> failedCustomers;

    public boolean isAllFailed() {
        return successCustomers.isEmpty();
    }
}
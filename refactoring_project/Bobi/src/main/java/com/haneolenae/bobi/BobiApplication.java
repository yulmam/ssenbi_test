package com.haneolenae.bobi;

import com.haneolenae.bobi.domain.custom.entity.CustomTemplate;
import com.haneolenae.bobi.domain.customer.entity.Customer;
import com.haneolenae.bobi.domain.customer.entity.CustomerTag;
import com.haneolenae.bobi.domain.general.entity.Category;
import com.haneolenae.bobi.domain.general.entity.GeneralTemplate;
import com.haneolenae.bobi.domain.member.entity.Member;
import com.haneolenae.bobi.domain.message.entity.Message;
import com.haneolenae.bobi.domain.message.entity.MessageCustomer;
import com.haneolenae.bobi.domain.message.entity.MessageTag;
import com.haneolenae.bobi.domain.tag.entity.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.xml.bind.JAXBContext;
import java.util.List;


@SpringBootApplication
public class BobiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BobiApplication.class, args);
    }

}

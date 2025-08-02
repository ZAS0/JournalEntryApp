package com.zeeecom.journalEntry.Services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    public void testSendMail(){
        emailService.sendEmail("zeeshansiddcr7@gmail.com",
                "Tere Liye",
                "Haan bhai test hai"
        );
    }

}

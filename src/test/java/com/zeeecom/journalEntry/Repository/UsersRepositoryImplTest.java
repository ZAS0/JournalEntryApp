package com.zeeecom.journalEntry.Repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UsersRepositoryImplTest {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Test
    public void userRepoImplTest(){
        assertNotNull(userRepository.getUserForSentimentAnalysis());
    }

}

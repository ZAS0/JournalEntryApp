package com.zeeecom.journalEntry.Services;

import com.zeeecom.journalEntry.Repository.UserRepo;
import com.zeeecom.journalEntry.entity.Users;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;

import static org.junit.jupiter.api.Assertions.*;

//This ensures that the app start and all thoes config that was present
                //in main will be here so no nullPointer Exception occurs
@SpringBootTest
public class UserServicesTests {

    @Autowired
    UserRepo userRepo;

    //A function annotated with @Test is regarded as single test even in the fuction
     //we are testing multiple things.
    @Test
    @Disabled //This test doesn't occur's when we run the class due to that annotation
    public void testfindByUserName(){
        assertEquals(2,1+1);
        assertNotNull(userRepo.findByuserName("jiff"));

        //There are many assert..() function which allow us to do many things try it out
        //These are few

        Users user=userRepo.findByuserName("amaan");
        assertTrue(user.getJournalEntries().isEmpty());
    }


    @ParameterizedTest //This is to tell that the test function have parameters
    @CsvSource({
            "1,2,4",
            "2,3,5",
            "2,6,8",
            "1,1,0"
    }) //This is one way to feed data into the function we can also give a csvFile path
    public void test(int a,int b,int expected){
        assertEquals(expected,a+b);
    }

    @ParameterizedTest //This is to tell that the test function have parameters
    @ValueSource(strings ={
            "jiff", //It takes single value parameter
            "admin",
            "amaan",
            "kali"
    }) //This is one other way to feed data into the function,there are lot of ways like enumSource e.t.c
    public void testToFindUserName(String UserName){
        assertNotNull(userRepo.findByuserName(UserName));
    }

    //Some other Annotation are
    // @BeforeEach() -Before each test the function annotated with this annotation will run
    // @BeforeAll() - Before All tests the function annotated with this annotation will run
    // @AfterEach() - After each test the function annotated with this annotation will run
    // @AfterAll() - After all tests the function annotated with this annotation will run

    //Code Coverage is used for generating reports of test that shows how many lines
    //of code are being used for testing.
}

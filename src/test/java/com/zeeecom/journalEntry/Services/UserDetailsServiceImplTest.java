package com.zeeecom.journalEntry.Services;
import com.zeeecom.journalEntry.Repository.UserRepo;
import com.zeeecom.journalEntry.entity.Users;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import static org.mockito.Mockito.*;

//@SpringBootTest -> this is used when we want to use spring boot application context
public class UserDetailsServiceImplTest {

    @InjectMocks
    private  UserDetailsServiceIMP userDetailsServiceImpl;

    @Mock
    private UserRepo userRepo;

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loadUserByUsername(){
        when(userRepo.findByuserName(ArgumentMatchers.anyString())).thenReturn(Users.builder().userName("jiff").password("hskhd").roles(new ArrayList<>()).build());
        UserDetails user=userDetailsServiceImpl.loadUserByUsername("jiff");
        Assertions.assertNotNull(user);
    }

    //All explanation in the copy

}

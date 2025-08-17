package com.zeeecom.journalEntry.Services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.zeeecom.journalEntry.Repository.UserRepo;
import com.zeeecom.journalEntry.entity.Users;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServices {
    
    @Autowired
    private UserRepo userRepo;

    private static final PasswordEncoder encrypter=new BCryptPasswordEncoder();

    public void SaveUser(Users user) {
        userRepo.save(user);
    }

    public void SaveNewUser(Users user) {
        user.setPassword(encrypter.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        userRepo.save(user);
    }

    public List<Users> getAllUser() {
        return userRepo.findAll();
    }

    public Optional<Users> find_User_by_id(ObjectId id) {
        return userRepo.findById(id);
    }

    public void delete_User_by_id(ObjectId id) {
        userRepo.deleteById(id);
    }
    
    public Users find_by_userName(String userName){
        return userRepo.findByuserName(userName);
    }

    public void SaveAdminUser(Users user) {
        user.setPassword(encrypter.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER","ADMIN"));
        userRepo.save(user);
    }

    public boolean deleteUserByUsername(String username) {
        try {
            Users user = find_by_userName(username);
            if (user != null) {
                ObjectId id = user.getId();
                if (id != null) {
                    delete_User_by_id(id);
                    return true; // Deleted successfully
                }
            }
            // User not found or id is null
            return false;
        } catch (Exception e) {
            log.error("Error deleting user by username: {}", username, e);
            return false;
        }
    }

}

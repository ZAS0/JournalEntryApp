package com.zeeecom.journalEntry.Scheduler;

import com.zeeecom.journalEntry.Repository.UserRepositoryImpl;
import com.zeeecom.journalEntry.Enums.Sentiment;
import com.zeeecom.journalEntry.Services.EmailService;
import com.zeeecom.journalEntry.entity.JournalEntry;
import com.zeeecom.journalEntry.entity.Users;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    private EmailService emailService;
    private UserRepositoryImpl userRepository;

    public UserScheduler(EmailService emailService,UserRepositoryImpl userRepository){
        this.emailService=emailService;
        this.userRepository=userRepository;
    }

    //@Scheduled(cron ="0 * * ? * *") --for every second
    @Scheduled(cron ="0 0 9 ? * SUN") //generate cron expr via gpt
    public void fetchUserAndSendSaMail(){
        List<Users> userForSentimentAnalysis = userRepository.getUserForSentimentAnalysis();
        for (Users user:userForSentimentAnalysis){
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> fileterdSentiments = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x -> x.getSentiment()).collect(Collectors.toList());
            Map<Sentiment,Integer> sentimentCounts=new HashMap<>();
            for (Sentiment sentiment:fileterdSentiments){
                if(sentiment!= null){
                    sentimentCounts.put(sentiment,sentimentCounts.getOrDefault(sentiment,0)+1);
                }
            }
            Sentiment mostFrequentSentiment=null;
            int maxCount=0;
            for (Map.Entry<Sentiment,Integer> entry:sentimentCounts.entrySet()){
                if (maxCount < entry.getValue()){
                    maxCount=entry.getValue();
                    mostFrequentSentiment=entry.getKey();
                }
            }
            if (mostFrequentSentiment != null){
                emailService.sendEmail(user.getEmail(),"Sentiment for last 7 days",mostFrequentSentiment.toString());
            }
        }
    }
}

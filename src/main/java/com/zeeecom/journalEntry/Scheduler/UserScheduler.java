package com.zeeecom.journalEntry.Scheduler;

import com.zeeecom.journalEntry.Repository.UserRepositoryImpl;
import com.zeeecom.journalEntry.Enums.Sentiment;
import com.zeeecom.journalEntry.Services.EmailService;
import com.zeeecom.journalEntry.entity.JournalEntry;
import com.zeeecom.journalEntry.entity.Users;
import com.zeeecom.journalEntry.model.SentimentData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserScheduler {

    private EmailService emailService;
    private UserRepositoryImpl userRepository;
    private KafkaTemplate<String, SentimentData> kafkaTemplate;

    public UserScheduler(EmailService emailService, UserRepositoryImpl userRepository, KafkaTemplate<String, SentimentData> kafkaTemplate) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    //@Scheduled(cron ="0 * * ? * *") --for every second
    @Scheduled(cron = "0 0 9 ? * SUN") //generate cron expr via gpt
    public void fetchUserAndSendSaMail() {
        List<Users> userForSentimentAnalysis = userRepository.getUserForSentimentAnalysis();
        for (Users user : userForSentimentAnalysis) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> fileterdSentiments = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x -> x.getSentiment()).collect(Collectors.toList());
            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();
            for (Sentiment sentiment : fileterdSentiments) {
                if (sentiment != null) {
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
                }
            }
            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;
            for (Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()) {
                if (maxCount < entry.getValue()) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }
            if (mostFrequentSentiment != null) {
                SentimentData sentimentData = SentimentData.builder().email(user.getEmail()).sentiment("Sentiment for last 7 dyas: " + mostFrequentSentiment).build();
                try {
                    //Kafka producer - producer sent data to kafka topic weekly-sentiments
                    kafkaTemplate.send("weekly-sentiments", sentimentData.getEmail(), sentimentData);
                } catch (Exception e) {
                    //Kafka FallBack - if kafka don't work then do this
                    log.error("Kafka didnt worked so sending mail directly", e);
                    emailService.sendEmail(sentimentData.getEmail(), "Sentiment for previous week", sentimentData.getSentiment());
                }
            }
        }
    }
}

package com.zeeecom.journalEntry.Services;

import com.zeeecom.journalEntry.model.SentimentData;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SentimentConsumerService {

    private EmailService emailService;

    public SentimentConsumerService(EmailService emailService){
        this.emailService=emailService;
    }

    //This is a kafka listener that constantly listens for the data sent to the cluster
    @KafkaListener(topics="weekly-sentiments",groupId = "weekly-sentiment-group")
    public void consume(SentimentData sentimentData){
        sendEmail(sentimentData);
    }

    private void sendEmail(SentimentData sentimentData){
        emailService.sendEmail(sentimentData.getEmail(),"Sentiment for previous week",sentimentData.getSentiment());
    }


}

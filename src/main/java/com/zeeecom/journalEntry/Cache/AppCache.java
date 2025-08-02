package com.zeeecom.journalEntry.Cache;

import com.zeeecom.journalEntry.Repository.ConfigjournalAppRepository;
import com.zeeecom.journalEntry.entity.ConfigJournalAppEntity;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Component
public class AppCache {

    public enum Keys{
        WEATHER_API;
    }

    private ConfigjournalAppRepository configjournalAppRepository;

    public AppCache(ConfigjournalAppRepository configjournalAppRepository){
        this.configjournalAppRepository=configjournalAppRepository;
    }

    public  Map<String,String> appCache;


    //This method helps in re-initialization of hashMap without restarting the application
    //If any changes made in db collection directly So instead of restarting use this
    @PostConstruct
    public void init(){
        appCache=new HashMap<>();
        List<ConfigJournalAppEntity> all = configjournalAppRepository.findAll();
        for (ConfigJournalAppEntity configJournalAppEntity:all){
            appCache.put(configJournalAppEntity.getKey(),configJournalAppEntity.getValue());
        }
    }
}

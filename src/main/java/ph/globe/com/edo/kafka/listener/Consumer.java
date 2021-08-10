package ph.globe.com.edo.kafka.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ph.globe.com.edo.kafka.configs.AppConfiguration;

@Service
public class Consumer {
    @KafkaListener(topics = "spring-test", groupId = AppConfiguration.groupid)
    public void consume(String message){
        System.out.println("Message: " + message);
    }
}

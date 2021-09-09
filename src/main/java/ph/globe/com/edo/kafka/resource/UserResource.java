package ph.globe.com.edo.kafka.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ph.globe.com.edo.kafka.configs.AppConfiguration;
import ph.globe.com.edo.kafka.model.User;

@RestController
@RequestMapping("kafka")

public class UserResource {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping ("/publish/{message}")
    public String post(@PathVariable("message") final String message){

        this.kafkaTemplate.send(AppConfiguration.topic, message);

        return "Published Successfully";

    }


}

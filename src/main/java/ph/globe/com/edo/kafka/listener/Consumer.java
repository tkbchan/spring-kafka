package ph.globe.com.edo.kafka.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import ph.globe.com.edo.kafka.configs.AppConfiguration;

import java.util.concurrent.CountDownLatch;

public class Consumer {
    public static class Listener {
        public final CountDownLatch latch = new CountDownLatch(3);

        @KafkaListener(topics = AppConfiguration.topic, groupId = AppConfiguration.groupid, containerFactory = "kafkaListenerContainerFactory")
        public void listen(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
            try{
                System.out.println("Successfully Received: " + message + " (partition: " + partition + ")");
                this.latch.countDown();
            } catch (Exception e){
                System.out.println("Error in sending record");
                System.out.println(e);
                e.printStackTrace();
            }

        }

    }
}

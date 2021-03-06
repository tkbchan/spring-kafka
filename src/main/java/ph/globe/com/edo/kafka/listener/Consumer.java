package ph.globe.com.edo.kafka.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import ph.globe.com.edo.kafka.configs.AppConfiguration;

import java.util.concurrent.CountDownLatch;

public class Consumer {
    public static class Listener {
        public final CountDownLatch latch = new CountDownLatch(3);
        private final Logger logger = LoggerFactory.getLogger(Consumer.class);
        private final TaskExecutor exec = new SimpleAsyncTaskExecutor();

        @KafkaListener(topics = AppConfiguration.topic, groupId = AppConfiguration.groupid, containerFactory = "kafkaListenerContainerFactory")
        public void listen(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
            try{
                if (message.startsWith("This")) {
                    throw new RuntimeException("failed");
                }
                System.out.println("Successfully Received: " + message + " (partition: " + partition + ")");
                this.latch.countDown();

            } catch (Exception e){
                System.out.println("Error in sending record");
                this.exec.execute(() -> System.out.println("Hit Enter to terminate..."));
                //System.out.println(e);
                e.printStackTrace();
            }
        }

        @KafkaListener(id = "dltGroup", topics = AppConfiguration.deadTopic)
        public void dltListen(String in) {
            logger.info("Received from DLT: " + in);
            this.exec.execute(() -> System.out.println("Hit Enter to terminate..."));
        }


    }
}

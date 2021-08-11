package ph.globe.com.edo.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import ph.globe.com.edo.kafka.configs.AppConfiguration;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableKafka
public class KafkaApplication {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext context = SpringApplication.run(KafkaApplication.class, args);
		TestBean testBean = context.getBean(TestBean.class);
		while(true){
			for (int i = 0; i < 5; i++) {
				testBean.send("This is message " + i);
			}
			context.getBean(Listener.class).latch.await(60, TimeUnit.SECONDS);
		}

	}

	@Bean
	public TestBean test() {
		return new TestBean();
	}

	@Bean
	public Listener listener() {
		return new Listener();
	}

	public static class TestBean {
		@Autowired
		private KafkaTemplate<String, String> template;

		public void send(String message) {
			this.template.send(AppConfiguration.topic, message);
			this.template.flush();
		}

	}

	public static class Listener {

		private final CountDownLatch latch = new CountDownLatch(3);

		private int count;

		@KafkaListener(topics = AppConfiguration.topic, groupId = AppConfiguration.groupid, containerFactory = "kafkaListenerContainerFactory")
		public void listen(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
			System.out.println("Received: " + message + " attempt " + ++count);
			if (this.count < 3) {
				throw new RuntimeException("retry");
			}
			System.out.println("Successfully Received: " + message + " (partition: " + partition + ")");
			this.count = 0;
			this.latch.countDown();
		}

	}

}

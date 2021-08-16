package ph.globe.com.edo.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import ph.globe.com.edo.kafka.configs.AppConfiguration;
import ph.globe.com.edo.kafka.listener.Consumer;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableKafka
public class KafkaApplication {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext context = SpringApplication.run(KafkaApplication.class, args);
		TestBean testBean = context.getBean(TestBean.class);
		while(true){
			for (int i = 0; i < 50; i++) {
				try{
					Thread.sleep(5000);

				} catch (Exception e){
					System.out.println("exception" + e.getMessage());
				}
				testBean.send("This is message " + i);
			}
			context.getBean(Consumer.Listener.class).latch.await(60, TimeUnit.SECONDS);
		}

	}

	@Bean
	public TestBean test() {
		return new TestBean();
	}

	@Bean
	public Consumer.Listener listener() {
		return new Consumer.Listener();
	}

	public static class TestBean {
		@Autowired
		private KafkaTemplate<String, String> template;

		public void send(String message) {
			this.template.send(AppConfiguration.topic, message);
			this.template.flush();
		}

	}



}

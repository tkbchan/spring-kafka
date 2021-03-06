package ph.globe.com.edo.kafka.configs;

public class AppConfiguration {
    public final static String bootstrapServers = "localhost:9092,localhost:9093";
    public static final String topic = "spring-test";
    public static final String deadTopic = "spring-test.DLT";
    public static final int numPartitions = 1;
    public static final String groupid = "group-id";
}

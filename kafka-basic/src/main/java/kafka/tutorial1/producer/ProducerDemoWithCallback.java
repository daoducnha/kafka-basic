package kafka.tutorial1.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoWithCallback {
    public static void main(String[] args) {
        final Logger logger = LoggerFactory.getLogger(ProducerDemoWithCallback.class);
        String bootstrapServers = "127.0.0.1:9092";
        // Create Producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Create  the producer
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        for (int i = 0; i < 9; i++) {
            // create Producer record
            final ProducerRecord<String, String> record =
                    new ProducerRecord<String, String>("first_topic", "hello world!" + Integer.toString(i));
            // send data - async
            producer.send(record, new Callback() {
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    // execute every time record successfull or an exception is throw
                    if (exception == null) {
                        logger.info("Received new metadata. \n" +
                                "Topic: " + metadata.topic() + "\n" +
                                "Partition: " + metadata.partition() + "\n" +
                                "Offset:" + metadata.offset() + "\n" +
                                "Timestamp:" + metadata.timestamp());
                    } else {
                        logger.error("Error while producing: ", exception);
                    }
                }
            });
        }
        producer.flush();
        producer.close();
    }
}

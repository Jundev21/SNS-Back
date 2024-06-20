package com.sns.sns.service.configuration.kafkaConfig;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

//@Configuration
//@EnableKafkaStreams
//@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaServer;

    public KafkaStreamsConfiguration kafkaStreamsConfiguration(){
        Map<String, Object> kafkaStreamConfig = new HashMap<>();
        kafkaStreamConfig.put(StreamsConfig.APPLICATION_ID_CONFIG, "stream test");
        kafkaStreamConfig.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        kafkaStreamConfig.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        kafkaStreamConfig.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        kafkaStreamConfig.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 3);

        return new KafkaStreamsConfiguration(kafkaStreamConfig);
    }

//    @Bean
//    public KafkaTemplate<String, Object> kafkaTemplate(){
//        // 해당 템플릿 실행될때 카프카 정보 실행된다.
//        return new KafkaTemplate<String, Object>(producerFactory());
//    }
//
//    @Bean
//    public ProducerFactory<String,Object> producerFactory(){
//        //producer 시리얼라이즈 또는 카프카 서버 정보 입력 부분
//        Map<String, Object> kafkaConfig = new HashMap<>();
//        kafkaConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
//        kafkaConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        kafkaConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//
//        return new DefaultKafkaProducerFactory<>(kafkaConfig);
//    }
//
//    @Bean
//    public ConsumerFactory<String, Object> consumerFactory(){
//        //consumer 시리얼라이즈 또는 카프카 서버 정보 입력 부분
//        Map<String, Object> kafkaConfig = new HashMap<>();
//        kafkaConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
//        kafkaConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        kafkaConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//
//        return new DefaultKafkaConsumerFactory<>(kafkaConfig);
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String,Object> concurrentKafkaListenerContainerFactory(){
//        ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListener = new ConcurrentKafkaListenerContainerFactory<>();
//        kafkaListener.setConsumerFactory(consumerFactory());
//        return kafkaListener;
//    }
}

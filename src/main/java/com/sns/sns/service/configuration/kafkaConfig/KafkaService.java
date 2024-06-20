package com.sns.sns.service.configuration.kafkaConfig;


import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public class KafkaService {

    private static final Serde<String> STRING_SERDE = Serdes.String();

//    @Bean
//    public void buildPipeline(StreamsBuilder sb){
//        KStream<String, String> kafkaStream = sb.stream("notification", Consumed.with(STRING_SERDE, STRING_SERDE));
//        kafkaStream.filter((key, value) -> value.contains("something")).to("targettopic");
//    }

}

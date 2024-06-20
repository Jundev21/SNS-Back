package com.sns.sns.service.configuration.redisConfig;


import com.sns.sns.service.domain.member.model.entity.Member;
import io.lettuce.core.RedisURI;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
@RequiredArgsConstructor
public class RedisConfig {

    // redis url 데이터 설정
    private final RedisProperties redisProperties;

//    @Value("${spring.data.redis.host}")
//    private String host;
//
//    @Value("${spring.data.redis.port}")
//    private int port;


    //redisConnectionFactory 는 어떤 레디스 서버랑 통신해야하는지 설정하는 곳
    //
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory(){
//        RedisURI redisURI = RedisURI.create(redisProperties.getUrl());
//        RedisConfiguration configuration = LettuceConnectionFactory.createRedisConfiguration(redisURI);
//        LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration);
//        factory.afterPropertiesSet();
//        return factory;
//    }

//    @Bean
//    public RedisTemplate<String, String> testRedisTemplate(RedisConnectionFactory redisConnectionFactory){
//        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        return redisTemplate;
//    }

    // 로컬 레디스 실행
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisProperties.getHost(),redisProperties.getPort());
        lettuceConnectionFactory.afterPropertiesSet();
        return lettuceConnectionFactory;
    }

    // RedisTemplate 는 redis 데이터를 접근할수 있도록 도와주는 클래스
    // Key,value 타입
    // 사용자에 대한 정보를 캐싱하기위한 클래스

    @Bean
    public RedisTemplate<String, Member> memberRedisTemplates(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Member> redisTemplate = new RedisTemplate<>();
        //redis 서로 연결
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<Member>(Member.class));
        return redisTemplate;
    }

}

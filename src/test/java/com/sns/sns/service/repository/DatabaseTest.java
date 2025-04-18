package com.sns.sns.service.repository;


import com.sns.sns.service.domain.member.model.UserStatus;
import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.member.repository.MemberRedisRepository;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DatabaseTest {

    @Autowired
    private MemberRedisRepository memberRedisRepository;

    @Test
    public void connectRedisBasic() {
        RedisURI uri = RedisURI.Builder
                .redis("localhost", 6379)
                .build();

        RedisClient client = RedisClient.create(uri);
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisCommands<String, String> commands = connection.sync();

        commands.set("foo", "bar");
        String result = commands.get("foo");
        System.out.println("connect redis " + result);

        connection.close();
        client.shutdown();
    }

    @Test
    public void MemberRedisTest(){
        Member newMember = new Member("loginId","username","password","emal","image",null);
        Member saved = memberRedisRepository.save(newMember);
        Member savedMember = memberRedisRepository.findByUserLoginIdAndUserStatus(saved.getUserLoginId(), UserStatus.JOIN).orElseThrow();

        Assertions.assertEquals(savedMember.getUserLoginId(), newMember.getUserLoginId());
    }

}

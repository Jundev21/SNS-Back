package com.sns.sns.service.integration;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.sns.sns.service.domain.member.dto.request.RegisterRequest;
import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.member.repository.MemberRepository;
import com.sns.sns.service.domain.member.service.MemberService;

@SpringBootTest
public class UserIntegrationTest {


	@Test
	public void checkRegisterQuery(){

	}
	@Test
	public void checkLoginQuery(){
		Path imagePath = Paths.get ("/","Users", "jun", "Desktop", "uploadImages","/");


	}
}

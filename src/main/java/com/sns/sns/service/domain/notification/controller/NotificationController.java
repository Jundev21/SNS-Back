package com.sns.sns.service.domain.notification.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.sns.sns.service.common.basicResponse.DataResponse;
import com.sns.sns.service.domain.member.model.entity.Member;
import com.sns.sns.service.domain.notification.dto.response.NotificationResponse;
import com.sns.sns.service.domain.notification.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/notification")
public class NotificationController {

	private final NotificationService notificationService;

	// 알림 저장할떄
	// 어떤사용자가 어떤 타입의 알림 타입으로 보냈는지

	//알림 보낼때
	// 어떤 사용자에게 어떤 사용자가 어떤 타입의 알림을 보냈는지.

	@Operation(summary = "알림 조회 API")
	@ApiResponses(value = {
		@ApiResponse(
			description = "알림 조회 성공",
			useReturnTypeSchema = true
		)
	})
	@GetMapping
	public DataResponse<NotificationResponse> makeNotification(
		@AuthenticationPrincipal Member member
	) {
		return DataResponse.successBodyResponse(HttpStatus.OK, notificationService.makeNotification(member));
	}


	@Operation(summary = "알림 SSE 연동 API")
	@ApiResponses(value = {
		@ApiResponse(
			description = "연동 성공",
			useReturnTypeSchema = true
		),
		@ApiResponse(
			description = "연동 실패",
			responseCode = "404",
			content = {@Content(schema = @Schema(implementation = DataResponse.class))}
		)
	})
	@GetMapping(value = "/subscribe", produces = "text/event-stream")
	@CrossOrigin(originPatterns = "*", allowedHeaders = "*", allowCredentials = "true")
	public SseEmitter notificationSubscribe(
		@AuthenticationPrincipal Member member
	){
		return notificationService.sseConnector(member);
	}
	//
	// @PostMapping(value = "/kafkaProducer")
	// public void notificationSubscribe(
	// 	@RequestParam String msg
	// ) {
	// 	notificationService.sendProducer(msg);
	// }

}

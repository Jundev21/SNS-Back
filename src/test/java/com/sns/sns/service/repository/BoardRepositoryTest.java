package com.sns.sns.service.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.sns.sns.service.domain.board.model.BoardEntity;
import com.sns.sns.service.domain.board.repository.BoardRepository;


@DataJpaTest
class BoardRepositoryTest {

	@Autowired
	private BoardRepository boardRepository;

	@Test void pagingTest(){

		Pageable pageable = PageRequest.of(0, 10);

		Page<BoardEntity> result = boardRepository.findAll(pageable);

		System.out.println("==============================");


	}

	@Test void pagingTestSort(){

		Sort sort = Sort.by("createdTime").descending();

		Pageable pageable = PageRequest.of(0, 10,sort);

		Page<BoardEntity> result = boardRepository.findAll(pageable);

		System.out.println("==============================");


	}

}
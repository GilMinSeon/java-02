package com.sparta.java_02.domain.purchase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.java_02.domain.purchase.dto.PurchaseProductRequestTest;
import com.sparta.java_02.domain.purchase.dto.PurchaseRequestTest;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class PurchaseControllerTest {

  @Autowired // 필드주입?? 이렇게 하지말라고 했지만 테스트케이스는 이렇게 많이 함
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void 주문_생성() throws Exception {
    // given: 데이터 셋팅 ex) 포스트맨의 body값
    // 본 소스의 request쓰려면 해당 클래스에 @builder 붙여야함 -> 본소스를 수정하므로 최대한 지양해야하는 방법
    // 그래서 테스트 폴더 아래에 클래스를 하나 만든다

    List<PurchaseProductRequestTest> purchaseProductRequestTests = new ArrayList<>();
    purchaseProductRequestTests.add(new PurchaseProductRequestTest(1L, 10));

    PurchaseRequestTest purchaseRequestTest = new PurchaseRequestTest(1L,
        purchaseProductRequestTests);

    String requestBody = new ObjectMapper().writeValueAsString(purchaseRequestTest);

    // when & then
    // 테스트 케이스 언제 실행할건지, then: 기대한 값과 일치하는지
    mockMvc.perform(
            MockMvcRequestBuilders.post("/api/purchases")
                .contentType(MediaType.APPLICATION_JSON.toString())
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON.toString()))
        .andExpect(MockMvcResultMatchers.status().isOk()) // 상태값이 200이고
        .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true)); // result에 들어온 값이 true면
  }


  @Test
  void 유저_없음_체크() throws Exception {
    // given
    List<PurchaseProductRequestTest> purchaseProductRequestTests = new ArrayList<>();
    purchaseProductRequestTests.add(
        new PurchaseProductRequestTest(1L, 10)); // productId: 1, quantity: 10

    // null 넣으니까 500 에러남 -> PurchaseRequest 수정
    PurchaseRequestTest purchaseRequestTest = new PurchaseRequestTest(null,
        purchaseProductRequestTests); // userId: null

    String requestBody = new ObjectMapper().writeValueAsString(purchaseRequestTest);

    // when & then
    mockMvc.perform(
            MockMvcRequestBuilders.post("/api/purchases")
                .contentType(MediaType.APPLICATION_JSON.toString())
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON.toString())
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.error.errorCode").value("NOT_FOUND_USER"));
  }
}
package com.yonsai.springAi2.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yonsai.springAi2.controller.AiController;
import com.yonsai.springAi2.entity.Product;
import com.yonsai.springAi2.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductService {

    
	// 1. Ai엔진 가져오기 
	private final ChatModel chatModel;
	// 2. AI랑 대화하는 도구!
	private final ChatClient chatClient;
	// 3. 임시 저장소!(서버)
	private final ChatMemory chatMemory;
	
	// 4. mysql에 접근하는 변수!
	private final ProductRepository productRepository;
	
	public ProductService(ChatModel chatModel,
						  ChatMemory chatMemory,
						  ProductRepository productRepository) {
		//4. 아래 코드는 스프링부트가 직접 컨테이너에서 꺼내서 
		//   객체를 저장한다. 
		this.chatModel = chatModel;
		this.chatMemory = chatMemory;
		this.productRepository = productRepository;
		
		//5. 데이터를 기록해야되기 때문에 client는 직접 설정하기
		this.chatClient = ChatClient
							.builder(chatModel)
							.defaultSystem("""
								당신은 쇼핑몰 상품 정보 생성기 입니다.
								결과를 JSON타입으로 만들어줘
								{
									name: 상품명,
									price : 가격,
									feature :특징	(1한줄)
								}
								""")									
							.build();
		
		//5. 데이터를 기록해야되기 때문에 client는 직접 설정하기
			
	}
	
	public String chat(String id,String msg) {
		String resp = chatClient
						.prompt()
						.user(msg)
						.advisors(
							// id 값으로 연결!
							MessageChatMemoryAdvisor
								.builder(chatMemory) //핵심코드!
								.conversationId(id)  //고유한 식별키!
								.build()							
						) //대화 기억 연결!
						.call()  // 실행(AI한테 전송)
						.content();
//		데이터가 들어오면 mysql 에 저장할 수있도록 문자들을 
//		파싱해서 DB에 저장될 수있도록 메서드!
		saveProduct(resp);
		
		
		return null;
	}

	
	
//	  AI 응답 후 파싱(분리해서) 디비에 저장하기
	private void saveProduct(String resp) {
		
		log.info("넘겨받은 {}" ,resp);
		
		//위에 json타입으로 받으면 ```json ...``` 으로 
		// 감싸서 반환되기 때문에 없애기!
		String json = resp
						.replace("```json", "")
						.replace("```", "")
						.trim();
		
		log.info("제거한 json: {}",json);
		
		//JSON 문자열 -> 자바 객체로 변환하는 도구!
		ObjectMapper mapper = new ObjectMapper();
		
		// 자바 객체로 변화면 JSON의 key/value 접근하는 객체	
		try {
			JsonNode jsonNode = mapper.readTree(json);
		
			// 꺼내기 
			String name = jsonNode.get("name")
								  .asText();
			String price = jsonNode.get("price")
					  			   .asText();

			String feature = jsonNode.get("feature")
					  				 .asText();
			// 확인
			log.info("name : {}",name);
			log.info("price : {}",price);
			log.info("feature : {}",feature);
			
			// 디비 저장
			Product pro = new Product();
			pro.setName(name);
			pro.setPrice(price);
			pro.setFeature(feature);
			
			productRepository.save(pro);
			log.info("DB에 잘 저장됨!");
			
			
		} catch (Exception e) {		
			e.printStackTrace();	
		}
	}
	
	
	
	
	
}

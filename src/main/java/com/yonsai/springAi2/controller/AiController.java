package com.yonsai.springAi2.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yonsai.springAi2.service.AiService;
import com.yonsai.springAi2.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AiController {

	private final AiService aiService;
	private final ProductService productService;
	
//	추후에 유지 보수시 코드를 건드리지 않고 업데이트 할 수 있다.
//	객체 타입을 확인하는 명령어! (instance of)
//	클래스명<T>  - 제네릭 내가 필요한 타입을 그때 그때 생성해주는것!
	public AiController(AiService aiService,
						ProductService productService) {
		this.aiService = aiService;	
		this.productService = productService;
	}
	
	
	@GetMapping("/first")
	public String main(Model 가방) {
		log.info("Ai Controller - main()");
//		String result = aiService
//						.start("아이폰 17 검은색 재고 확인해줘!(서울에만)");
//		가방.addAttribute("result" , result);
		return "main";
	}
	
	/**
	 * 첫번째 질문을 내이름은 에리나야!
	 *   ↓
	 * ChatClient 생성 → AI 전달 → 답변 반환
	 *   ↓
	 *   끝! 대화 내용 저장 안함!X
	 * 두번째 질문을 내이름이 뭐야?!
	 *   ↓
	 * ChatClient 생성(처음부터다시) → AI 전달 → 답변 반환
	 *   ↓
	 *  끝! 대화 내용 저장 안함!X 
	 * 
	 * @param 가방
	 * @param msg
	 * @return
	 */
	
	// 위에 설정들은 사용을 하면 AI의 단점이 뭐냐!?
	//  - 대화 기억을 못한다. 
	
	
	@GetMapping("/second")
	public String second(Model 가방,
						@RequestParam String msg) {
		log.info("Ai Controller - second()");
		log.info("이전 대화내용 기록");
		
		//String result = aiService.second("user1",msg);
						
		//가방.addAttribute("result" , result);
		return "main";
	}
	
	//쇼핑몰 검색 페이지 열기 
	@GetMapping("/")
	public String product() {
		log.info("Ai Controller - product()");
		return "product";
	}
		
	/**
	 * 쇼핑몰 검색 챗봇!
	 *  MessageWindowChatMemory(임시 저장소)
	 *  input으로 입력받기 
	 */
	@GetMapping("/product")
	public String productPro(Model 가방 ,
					@RequestParam String msgInput) {
		log.info("Ai Controller - productPro()");
		log.info("넘겨 받은 전달값: {}",msgInput);
		
		String result = productService
							.chat("seohee",
								  msgInput);
		log.info("AI: {}",result);
		가방.addAttribute("result" ,result);
		
		//saveProduct();
		return "product";
	}
		
	private void saveProduct() {
		
		log.info("saveProduct() 매개변수 없음~");
		String resp = """
				상품명: Apple Mac mini (M2 칩)\n
				가격: 790,000원 부터\n
				특징: 강력한 M2 칩과 컴팩트한 디자인으로 뛰어난 성능과 휴대성을 제공하는 데스크탑 컴퓨터\n
				""";
		System.out.println(resp);
		System.out.println("-".repeat(30));
		System.out.println();
		
//		1.  split()
		String name = "";
		String price = "";
		String feature = "";
		
//		2. 실제 자르기!
		String[] lines = resp.split("\n");
		System.out.println(Arrays.toString(lines));
		System.out.println("-".repeat(30));
		System.out.println();
		
		for(String line : lines) {
			if(!line.isEmpty()) {
				String[] temp = line.split(":");
//				System.out.println(Arrays
//								.toString(temp));
				//위에서 : 구분해서 1번째 인덱스가 값
				if(temp[0].contains("상품명")) {
					name = temp[1].trim();
				System.out.println("값: "+ name);}
				
				if(temp[0].contains("가격")) {
					price = temp[1].trim();
					System.out.println("가격: "+ price);}	
			}
		}		
	}
	/*
	 *  정규식 방식 
	 *   - 자바에서도 정규식 
	 *   
	 *  String name = extract(resp, "상품명:\\s*(.*)");
	    String price = extract(resp, "가격:\\s*(.*)");
	    String feature = extract(resp, "특징:\\s*(.*)");

		Pattern pattern = Pattern.compile(regex);
    	Matcher matcher = pattern.matcher(text);
    	return matcher.find() ? matcher.group(1) : "";
	    장점 : 공백 조금 달라도 동작, 줄 순서 바뀌어도 가능!
	    단점 : AI문장 형태가 바뀌면 꺠져요!
	 * 
	 * 
	 * 실무에서 추천하는 방식은 JSON(AI를 같이 쓰면 )
	 * 자동으로 객체를 저장하고 싶을 경우에는 
	 * ObjectMapper 객체를 이용하면 쉽게 파싱할 수있다.
	 *  - 내가 만든 DTO나 entity에 자동으로 매핑이된다.
	 * 
	 * 라이브러리 추가! 
	 * {
		  "name": "Apple Mac mini (M2 칩)",
		  "price": "790,000원 부터",
		  "feature": "강력한 M2 칩과 컴팩트한 디자인..."
	   }
	 * 
	 */
	
	
	
	
	
	
	
	
}

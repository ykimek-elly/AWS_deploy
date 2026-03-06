package com.yonsai.springAi2.config;

import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 이거는 서버가 실행이 되면 spring서버에 메모리에 대화
// 기록을 저장하는 임시 저장소!

@Configuration //이거 설정하는 객체야! 안에 Bean어노테이션 찾아서
			   //객체 만들어줘! 그래서 니가 관리해줄래!!
public class ChatMemoryConfig {
	
	@Bean
	public ChatMemory chatMemory() {
		
		
//		AI를 사용할 때는 버전을 꼭 확인! 
//		MessageWindowChatMemory
//		인메모리의 버전이 spring ai 1.0버전에서만 사용이 가능하다
//		스프링 GEN AI랑 spring AI는 1.1.0버전에서만 사용할 수있는
//		MessageWindowChatMemory 업그레이드됨!
		
		MessageWindowChatMemory memory = 
					MessageWindowChatMemory
							.builder()
							.maxMessages(10) // 최근 10개만 유지
							.build();
		
		return memory;
//		return new MessageWindowChatMemory(new InMemoryChatMemoryRepository());
	}
	
	//MessageWindowChatMemory
	//  - 최근 N개의 대화만 유지하는 메모리 구조!
	//  - 재미니는 글자수(토큰) 조절 해야된다(너무길면 비용 증가)
	//    10개 만 하시는게 좋다! 
	//  - 서버를 재시작하면 삭제가 된다.
	

}

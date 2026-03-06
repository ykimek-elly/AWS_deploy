package com.yonsai.springAi2.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 어떤 기능들이 AI서비스를 호출할 수 있다! AiService를 따로 빼 놓는게 좋다!
 */

@Service
//@RequiredArgsConstructor
public class AiService {

	private final ChatMemory chatMemory;

//	@Autowired
//	private ChatModel chatModel; //자동 주입!

	// 1. 재미니 엔진 도구 포함하기!
	private final ChatModel chatModel;
	private final ChatClient chatClient;

	// 2. 생성자를 이용해서 저장한다.
	public AiService(ChatModel chatModel, ChatMemory chatMemory) {

		// 내가 직접 한번 기억하는 역할,
		// 메모리에 자동으로 기억 연결

		this.chatClient = ChatClient
				.builder(chatModel)
				.defaultSystem("""
					당신은 친절한 AI 어시스턴스입니다!
					결과는 HTML형식으로 작성하세요!
					제목은 <h3>
					   항목은 <ul><li>
					강조는 <b>
				""")// 기본 역할 한번만 지정
				.build();

		// 내가 직접 한번 기억하는 역할,
		// 메모리에 자동으로 기억 연결

		this.chatMemory = chatMemory;
		this.chatModel = chatModel;

	}

	// 3. 핵심 코드
	// ChatClient Ai모델을 쉽게 사용할 수있도록 해주는 도구
	// chatModel 실제 AI랑 연결 담당!
	public String start(String msg) {
		return ChatClient.builder(chatModel).build() // 매번 ChatClient생성!(이전 대화 불러오기 저장X)
				.prompt().system("당신은 친절한 AI 어시스턴스입니다!").user(msg).call() // 위에서 역할 주고 질문주고 실제 전송!
				.content();
	}
	// 위에 start()코드는 매번 객체를 생성하다 보니
	// 기존 내용이 없다!
	// 그래서 가장 간단하게 내용을 기억하게 만드는
	// InMemoryChatMemory (업그레이드버전 사용)
	// 이전 대화를 AI에게 자동으로 전달할 연결 고리를 만든다.
	// 최근 메모리에 저장을 하려면 어떤 사람이 어떤 메시를 몇개저장
	// 할 지 구별해야되기 때문에 id는 꼭! 받아야된다.

	/**
	 * AI 호출할 때 기존 메시지 저장하는 역할
	 * 
	 * @param userId 사용자 식별용! 독립적 대화!
	 * @param msg    사용자 질문
	 * @return AI 답변 넘어간다.
	 */
	public String second(String userId, String msg) {

		return chatClient.prompt()
						 .user(msg)
//						 이전 데이터를 기억하게 만드는 설정부분
//						 userId를 기준으로 대화창을 구별한다.
//						 자동으로 임시 저장소에 접근 할수있도록 설정하는 부분
						 
						 .advisors(
								MessageChatMemoryAdvisor
										.builder(chatMemory)
										.conversationId(userId)
										.build())

						 .call()
						 .content();
	}
}

/*
 * system(""" 당신은 10년차 개발자 입니다. 당신은 HTML 형식으로 결과를 줄래 제목은 <h3> 항목은 <ul><li> 강조는
 * <b> """)
 * 
 * 1. 재고 여부(한줄) 2. 확인 방법(2줄 이내) 3. 간단한 팁(한줄)
 * 
 * AI는 구조를 잘 만드는 것이 더 중요하다!
 * 
 */

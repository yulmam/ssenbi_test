package com.haneolenae.bobi.domain.message.service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.haneolenae.bobi.domain.message.controller.port.MessageService;
import com.haneolenae.bobi.domain.message.entity.vo.MessageSendingResult;
import com.haneolenae.bobi.domain.message.service.port.MessageSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

import com.haneolenae.bobi.domain.custom.infrastructure.entity.CustomTemplate;
import com.haneolenae.bobi.domain.custom.service.port.CustomTemplateRepository;
import com.haneolenae.bobi.domain.customer.entity.Customer;
import com.haneolenae.bobi.domain.customer.repository.CustomerRepository;
import com.haneolenae.bobi.domain.customer.repository.CustomerTagRepository;
import com.haneolenae.bobi.domain.member.entity.Member;
import com.haneolenae.bobi.domain.member.repository.MemberRepository;
import com.haneolenae.bobi.domain.message.dto.request.SendMessageRequest;
import com.haneolenae.bobi.domain.message.dto.response.MessageDetailResponse;
import com.haneolenae.bobi.domain.message.dto.response.MessageResponse;
import com.haneolenae.bobi.domain.message.entity.Message;
import com.haneolenae.bobi.domain.message.entity.MessageCustomer;
import com.haneolenae.bobi.domain.message.entity.MessageTag;
import com.haneolenae.bobi.domain.message.mapper.MessageMapper;
import com.haneolenae.bobi.domain.message.service.port.MessageCustomerRepository;
import com.haneolenae.bobi.domain.message.service.port.MessageRepository;
import com.haneolenae.bobi.domain.message.service.port.MessageTagRepository;
import com.haneolenae.bobi.domain.tag.dto.response.TagStatisticsResponse;
import com.haneolenae.bobi.domain.tag.entity.Tag;
import com.haneolenae.bobi.domain.tag.repository.TagRepository;
import com.haneolenae.bobi.global.dto.ApiType;
import com.haneolenae.bobi.global.exception.ApiException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

	private static final String BUSINESS_NAME_PLACEHOLDER = "[[업체명]]";
	private static final String CUSTOMER_NAME_PLACEHOLDER = "[[고객명]]";
	private final MemberRepository memberRepository;
	private final CustomerRepository customerRepository;
	private final CustomTemplateRepository customTemplateRepository;
	private final CustomerTagRepository customerTagRepository;
	private final TagRepository tagRepository;
	private final MessageRepository messageRepository;
	private final MessageCustomerRepository messageCustomerRepository;
	private final MessageTagRepository messageTagRepository;
	private final MessageMapper messageMapper;
	private final MessageSender messageSender;

	//문자 메시지를 받을 고객 목록을 정리한 뒤 단체 메시지를 전송하는 API 입니다.
	@Transactional
	public void sendMessage(long memberId, SendMessageRequest sendMessageRequest) {

		// TODO: 멤버 유효성 검사
		Member sender = memberRepository.findById(memberId)
			.orElseThrow(() -> new ApiException(ApiType.MEMBER_NOT_EXIST));

		Message originMessage = Message.builder()
			.content(sendMessageRequest.getMessageContent())
			.member(sender)
			.build();

		// 고객 아이디에 해당하는 Customer 가져오기
		Set<Customer> finalReceiverCustomers = customerRepository.findByMemberIdAndCustomerIdIn(memberId,
			sendMessageRequest.getMessageCustomerIds());


		// 태그에 해당하는 Customer 가져오기
		List<Customer> tagCustomers = customerRepository.findALlByMemberIdAndTags(memberId,
			sendMessageRequest.getMessageTagIds());

		//최종 문자 메시지 받을 Customer
		finalReceiverCustomers.addAll(tagCustomers);


		if (finalReceiverCustomers.isEmpty()) {
			throw new ApiException(ApiType.TARGET_NOT_FOUND);
		}


		List<MessageSendingResult> messageSendingResults = sendMessages(originMessage, sender, finalReceiverCustomers);

		//성공한 고객들 연관관계 설정
		List<MessageCustomer> messageCustomers =  messageSendingResults.stream()
				.filter(MessageSendingResult::isSuccess)  // ✅ 성공한 메시지만 필터링
				.map(result -> MessageCustomer.from(result, originMessage))
				.collect(Collectors.toList());

		originMessage.setMessageCustomers(messageCustomers);

		// 태그 가져오기
		List<Tag> tags = tagRepository.findByMemberIdAndTagIds(memberId, sendMessageRequest.getMessageTagIds());

		//TODO: messageTag 생성 및 저장
		List<MessageTag> messageTags = tags.stream()
				.map(tag -> new MessageTag(tag.getName(), tag.getColor(), originMessage))
				.toList();

		originMessage.setMessageTags(messageTags);

		messageRepository.save(originMessage);

		//전송자 메시지 전송 횟수 중가
		sender.increaseMessageCount();
		memberRepository.save(sender);

		if (sendMessageRequest.getCustomTemplateId() != null) {
			Optional<CustomTemplate> optionalCustomTemplate = customTemplateRepository.findById(sendMessageRequest.getCustomTemplateId());
			if (optionalCustomTemplate.isPresent()) {
				CustomTemplate customTemplate = optionalCustomTemplate.get();
				customTemplate.countUp();
				customTemplateRepository.save(customTemplate);
			}
		}
	}
	private List<MessageSendingResult> sendMessages(Message message, Member sender, Set<Customer> customers) {
		List<CompletableFuture<MessageSendingResult>> futures = new ArrayList<>();

		for (Customer customer : customers) {
			log.info("고객에게 메시지 전송 : {}", customer.getId());

			// 개인화된 메시지 생성
			String personalizedMessage = message.makePersonalMessage(customer.getName(), sender.getBusiness());

			// 비동기 메시지 전송 및 CompletableFuture 리스트에 추가
			CompletableFuture<MessageSendingResult> future = messageSender
					.sendMessagesAsync(personalizedMessage, sender.getPersonalPhoneNumber(), customer.toCustomerInfo());

			futures.add(future);
		}

		// 모든 비동기 작업이 완료될 때까지 대기
		CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

		// 모든 작업 완료 후 결과 리스트 반환
		return allDoneFuture.thenApply(v ->
				futures.stream()
						.map(CompletableFuture::join) // 모든 작업 완료 후 join 실행
						.collect(Collectors.toList())
		).exceptionally(e -> {
			log.error("비동기 작업 중 오류 발생: {}", e.getMessage());
			return Collections.emptyList(); // 실패 시 빈 리스트 반환
		}).join();
	}



	@Override
	public List<MessageResponse> getMessageList(long memberId, String keyword, Pageable pageable) {

		// TODO: 멤버 유효성 검사
		Member sender = memberRepository.findById(memberId)
			.orElseThrow(() -> new ApiException(ApiType.MEMBER_NOT_EXIST));

		// TODO: 검색어로 검색
		List<Message> messages = messageRepository.findMessagesByKeywordAndMemberId(keyword, memberId, pageable)
			.getContent();

		return messages.stream()
			.map(messageMapper::toMessageResponse)
			.toList();
	}

	@Override
	public MessageDetailResponse getMessageDetail(long memberId, long messageId) {

		// TODO: 멤버 유효성 검사
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new ApiException(ApiType.MEMBER_NOT_EXIST));

		// TODO: message 가져오기
		Message message = messageRepository.findByIdAndMemberId(messageId, member.getId())
			.orElseThrow(() -> new ApiException(ApiType.MESSAGE_NOT_FOUND));

		return messageMapper.toMessageDetailResponse(message);
	}

	@Transactional
	public void deleteMessage(long memberId, long messageId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new ApiException(ApiType.MEMBER_NOT_EXIST));

		Message message = messageRepository.findByIdAndMemberId(messageId, member.getId())
			.orElseThrow(() -> new ApiException(ApiType.MESSAGE_NOT_FOUND));

		messageRepository.delete(message);
		member.decreaseMessageCount();
	}

	@Transactional
	public List<TagStatisticsResponse> getMessageTagStatistics(long memberId) {
		List<Message> messages = messageRepository.findByMemberIdWithTag(memberId);

		Map<String, Integer> tagCount = new HashMap<>();
		Map<String, String> tagColor = new HashMap<>();

		for (Message message : messages) {
			List<MessageTag> messageTags = message.getMessageTags();
			for (MessageTag tag : messageTags) {
				if (tagCount.containsKey(tag.getName())) {
					tagCount.put(tag.getName(), tagCount.get(tag.getName()) + 1);
				} else {
					tagCount.put(tag.getName(), 1);
					tagColor.put(tag.getName(), tag.getColor());
				}
			}
		}

		List<TagStatisticsResponse> responses = new ArrayList<>();

		for (Map.Entry<String, Integer> entry : tagCount.entrySet()) {
			responses.add(
				new TagStatisticsResponse(
					entry.getKey(),
					entry.getValue(),
					tagColor.get(entry.getKey())
				)
			);
		}
		Collections.sort(responses);
		return responses;
	}
}

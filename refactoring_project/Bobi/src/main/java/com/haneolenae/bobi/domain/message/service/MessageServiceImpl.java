package com.haneolenae.bobi.domain.message.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

import com.haneolenae.bobi.domain.custom.infrastructure.entity.CustomTemplateEntity;
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
import com.haneolenae.bobi.domain.message.repository.MessageCustomerRepository;
import com.haneolenae.bobi.domain.message.repository.MessageRepository;
import com.haneolenae.bobi.domain.message.repository.MessageTagRepository;
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
	private final DefaultMessageService coolSmsService;
	@Value("${coolsms.senderPhoneNumber}")
	private String senderPhoneNumber;

	@Transactional
	public void sendMessage(long memberId, SendMessageRequest sendMessageRequest) {

		// TODO: 멤버 유효성 검사
		Member sender = memberRepository.findById(memberId)
			.orElseThrow(() -> new ApiException(ApiType.MEMBER_NOT_EXIST));

		Message originMessage = Message.builder()
			.content(sendMessageRequest.getMessageContent())
			.member(sender)
			.build();

		//TODO : 받는이 유효성 검사
		Set<Customer> finalReceiverCustomers = customerRepository.findByMemberIdAndCustomerIdIn(memberId,
			sendMessageRequest.getMessageCustomerIds());

		if (finalReceiverCustomers.size() != sendMessageRequest.getMessageCustomerIds().size()) {
			throw new ApiException(ApiType.CUSTOMER_NOT_FOUND);
		}

		//TODO: 태그 유효성 검사
		List<Tag> tags = tagRepository.findByMemberIdAndTagIds(memberId, sendMessageRequest.getMessageTagIds());

		if (tags.size() != sendMessageRequest.getMessageTagIds().size()) {
			throw new ApiException(ApiType.TAG_MEMBER_INVALID);
		}

		// TODO: 태그에 해당하는 Customer 가져오기
		List<Customer> tagCustomers = customerRepository.findALlByMemberIdAndTags(memberId,
			sendMessageRequest.getMessageTagIds());

		finalReceiverCustomers.addAll(tagCustomers);

		// TODO: customer에 메시지 전송

		if (finalReceiverCustomers.isEmpty()) {
			throw new ApiException(ApiType.TARGET_NOT_FOUND);
		}

		// 성공한 고객 리스트
		List<MessageCustomer> successCustomers = new ArrayList<>();
		// 실패한 고객 이름 리스트
		List<String> failedCustomers = new ArrayList<>();

		Executor executor = Executors.newFixedThreadPool(10);

		List<CompletableFuture<Void>> futures = finalReceiverCustomers.stream()
				.map(customer -> sendMessageAsync(originMessage, sender, customer, successCustomers, failedCustomers, executor))
				.collect(Collectors.toList());

		CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

		if (failedCustomers.size() == finalReceiverCustomers.size()) {
			throw new ApiException(ApiType.MESSAGE_SERVICE_ALL_FAIL);
		}

		//TODO: message 저장
		messageRepository.saveAndFlush(originMessage);

		sender.increaseMessageCount();

		if (sendMessageRequest.getCustomTemplateId() != null) {
			CustomTemplateEntity customTemplateEntity = customTemplateRepository.findById(sendMessageRequest.getCustomTemplateId())
				.orElse(null);
			if (customTemplateEntity != null) {
				customTemplateEntity.countUp();
			}
		}

		//TODO: messageTag 생성 및 저장
		List<MessageTag> messageTags = tags.stream()
			.map(tag -> new MessageTag(tag.getName(), tag.getColor(), originMessage))
			.toList();
		messageTagRepository.saveAllAndFlush(messageTags);

		//TODO: messageCustomer 생성 및 저장
		messageCustomerRepository.saveAllAndFlush(successCustomers);

		if (!failedCustomers.isEmpty()) {
			throw new ApiException(ApiType.EXTERNAL_MESSAGE_SERVICE_ERROR, failedCustomers);
		}
	}
	private CompletableFuture<Void> sendMessageAsync(Message originMessage, Member sender, Customer customer,
													 List<MessageCustomer> successCustomers, List<String> failedCustomers,
													 Executor executor) {
		return CompletableFuture.runAsync(() -> {
			log.info("고객에게 메시지 전송 : " + customer.getId());
			String msg = generateMessageForCustomer(originMessage.getContent(), sender, customer);
			try {
				sendCoolSms(customer.getPhoneNumber(), msg);
				synchronized (successCustomers) {
					successCustomers.add(MessageCustomer.builder()
							.name(customer.getName())
							.phoneNumber(customer.getPhoneNumber())
							.color(customer.getColor())
							.message(originMessage)
							.build());
				}
			} catch (ApiException e) {
				synchronized (failedCustomers) {
					failedCustomers.add(customer.getName());
				}
			}
		}, executor);
	}



	public String generateMessageForCustomer(String content, Member sender, Customer customer) {
		String message = content.replace(BUSINESS_NAME_PLACEHOLDER, sender.getBusiness());
		message = message.replace(CUSTOMER_NAME_PLACEHOLDER, customer.getName());

		return message;
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

	@Override
	public void sendCoolSms(String receiverPhone, String msg) {
		net.nurigo.sdk.message.model.Message coolMessage = new net.nurigo.sdk.message.model.Message();
		// 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
		coolMessage.setFrom(senderPhoneNumber);
		coolMessage.setTo(receiverPhone);
		coolMessage.setText(msg);

		SingleMessageSentResponse response = coolSmsService.sendOne(new SingleMessageSendingRequest(coolMessage));
		log.info(response.toString());
		log.info(response.getStatusCode());
		if (!response.getStatusCode().equals("2000")) {
			log.info("third party message api fail");
			throw new ApiException(ApiType.EXTERNAL_MESSAGE_SERVICE_ERROR);
		}
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

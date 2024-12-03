package com.billit.user_service.kafka.consumer;

import com.billit.common.event.LoanDisbursementEvent;
import com.billit.user_service.account.dto.request.GroupDepositRequest;
import com.billit.user_service.account.dto.response.TransactionResponse;
import com.billit.user_service.account.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanDisbursementConsumer {
    private final TransactionService transactionService;

    @KafkaListener(
            topics = "loan-disbursement",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "loanDisbursementEventKafkaListenerContainerFactory"
    )
    public void consumeLoanDisbursement(LoanDisbursementEvent event) {
        try {
            log.info("Processing loan disbursement for groupId: {}", event.getGroupId());

            List<GroupDepositRequest> disbursementRequests = new ArrayList<>(event.getGroupLoans())
                    .stream()
                    .map(loan -> new GroupDepositRequest(
                            loan.getAccountBorrowId(),
                            loan.getUserBorrowId(),
                            loan.getLoanAmount(),
                            "대출금 입금"
                    ))
                    .collect(Collectors.toList());

            List<TransactionResponse> responses = transactionService.depositGroupBorrow(disbursementRequests);

            log.info("Successfully processed {} loan disbursements for groupId: {}",
                    responses.size(), event.getGroupId());
        } catch (Exception e) {
            log.error("Error processing loan disbursement for groupId: {}", event.getGroupId(), e);
            throw e;
        }
    }
}
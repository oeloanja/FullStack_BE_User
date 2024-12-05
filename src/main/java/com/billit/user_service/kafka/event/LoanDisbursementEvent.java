package com.billit.user_service.kafka.event;

import com.billit.user_service.kafka.dto.LoanResponseClientEventDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanDisbursementEvent {
    private List<LoanResponseClientEventDto> groupLoans;
    private Integer groupId;
    private String status;
}

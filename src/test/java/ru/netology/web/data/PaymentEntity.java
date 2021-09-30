package ru.netology.web.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PaymentEntity {
    private String id;
    private String amount;
    private String created;
    private String status;
    private String transaction_id;
}

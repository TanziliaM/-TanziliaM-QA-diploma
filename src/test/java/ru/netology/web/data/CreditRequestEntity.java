package ru.netology.web.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CreditRequestEntity {
    private String id;
    private String bank_id;
    private String created;
    private String status;
}

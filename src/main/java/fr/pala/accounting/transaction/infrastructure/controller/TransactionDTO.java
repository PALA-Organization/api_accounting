package fr.pala.accounting.transaction.infrastructure.controller;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class TransactionDTO {
    @NotEmpty(message = "type must not be empty")
    private String type;

    @NotEmpty(message = "shop_name must not be empty")
    private String shop_name;

    @NotEmpty(message = "shop_address must not be empty")
    private String shop_address;

    @NotNull(message = "amount must not be null")
    private Double amount;

    @NotEmpty(message = "description must not be empty")
    private String description;
}

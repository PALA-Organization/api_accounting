package fr.pala.accounting.account.infrastructure.controller;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

@Data
@Accessors(chain = true)
public class AccountDTO {
    @NotEmpty(message = "type must not be empty")
    private String type;

    @NotEmpty(message = "shop_name must not be empty")
    private String shop_name;

    @NotEmpty(message = "shop_address must not be empty")
    private String shop_address;

    @NotEmpty(message = "amount must not be empty")
    private Double amount;

    @NotEmpty(message = "description must not be empty")
    private String description;
}

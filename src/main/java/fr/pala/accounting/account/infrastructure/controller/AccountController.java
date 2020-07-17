package fr.pala.accounting.account.infrastructure.controller;

import fr.pala.accounting.account.domain.model.Account;
import fr.pala.accounting.account.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final CreateAccount createAccount;
    private final DeleteAccount deleteAccount;
    private final GetAccount getAccount;
    private final GetAccountAmount getAccountAmount;
    private final GetAllAccounts getAllAccounts;
    private final UpdateAccount updateAccount;

    public AccountController(CreateAccount createAccount, DeleteAccount deleteAccount, GetAccount getAccount, GetAccountAmount getAccountAmount, GetAllAccounts getAllAccounts, UpdateAccount updateAccount) {
        this.createAccount = createAccount;
        this.deleteAccount = deleteAccount;
        this.getAccount = getAccount;
        this.getAccountAmount = getAccountAmount;
        this.getAllAccounts = getAllAccounts;
        this.updateAccount = updateAccount;
    }


    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts(Principal principal) {
        List<Account> accounts = getAllAccounts.getAllAccounts(principal.getName());
        return ResponseEntity.ok(accounts);
    }

    @PostMapping
    public ResponseEntity<String> addAccount(Principal principal) {
        String accountId = createAccount.createAccount(principal.getName()).getId();
        return ResponseEntity.ok(accountId);
    }

    @GetMapping("/{accountId}/amount")
    public ResponseEntity<Double> getAccountAmount(@PathVariable("accountId") String accountId, Principal principal) {
        return ResponseEntity.ok(getAccountAmount.getAccountAmount(principal.getName(), accountId));
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<String> deleteAccount(@PathVariable("accountId") String accountId, Principal principal) {
        deleteAccount.deleteAccount(principal.getName(), accountId);
        return ResponseEntity.ok().build();
    }
}

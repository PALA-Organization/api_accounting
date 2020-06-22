package fr.pala.accounting.account.infrastructure.controller;

import fr.pala.accounting.account.domain.model.Account;
import fr.pala.accounting.account.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAccounts(Principal principal) {
        List<Account> accounts = accountService.getAccounts(principal.getName());
        return ResponseEntity.ok(accounts);
    }

    @PostMapping
    public ResponseEntity<String> addAccount(Principal principal) {
        String accountId = accountService.createAccount(principal.getName()).getId();
        return ResponseEntity.ok(accountId);
    }

    @GetMapping("/{accountId}/amount")
    public ResponseEntity<Double> getAccountAmount(@PathVariable("accountId") String accountId, Principal principal) {
        return ResponseEntity.ok(accountService.getAccountAmount(principal.getName(), accountId));
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<String> deleteAccount(@PathVariable("accountId") String accountId, Principal principal) {
        accountService.deleteAccount(principal.getName(), accountId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping("/filter")
    public ResponseEntity<String> filter(Principal principal, String filter) {
        // TODO : filtrer par max ou min montant de compte
        return ResponseEntity.noContent().build();
    }

}

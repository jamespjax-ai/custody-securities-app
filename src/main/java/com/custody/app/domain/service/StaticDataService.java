package com.custody.app.domain.service;

import com.custody.app.adapter.out.AccountRepository;
import com.custody.app.adapter.out.SecurityRepository;
import com.custody.app.domain.model.Account;
import com.custody.app.domain.model.Security;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaticDataService {

    private final SecurityRepository securityRepository;
    private final AccountRepository accountRepository;

    public StaticDataService(SecurityRepository securityRepository, AccountRepository accountRepository) {
        this.securityRepository = securityRepository;
        this.accountRepository = accountRepository;
    }

    public Security saveSecurity(Security security) {
        return securityRepository.save(security);
    }

    public List<Security> getAllSecurities() {
        return securityRepository.findAll();
    }

    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public void seedInitialData() {
        if (securityRepository.count() == 0) {
            securityRepository.save(new Security("US0378331005", "Apple Inc.", "USA", "EQUITY"));
            securityRepository.save(new Security("US5949181045", "Microsoft Corp.", "USA", "EQUITY"));
            securityRepository.save(new Security("GB0007188757", "Rio Tinto PLC", "UK", "EQUITY"));
        }
        if (accountRepository.count() == 0) {
            accountRepository.save(new Account("ACCT_ABC_123", "Global Asset Management", "ACTIVE"));
            accountRepository.save(new Account("ACCT_XYZ_789", "Prime Wealth Trust", "ACTIVE"));
        }
    }
}

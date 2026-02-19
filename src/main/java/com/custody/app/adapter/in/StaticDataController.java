package com.custody.app.adapter.in;

import com.custody.app.domain.model.Account;
import com.custody.app.domain.model.Security;
import com.custody.app.domain.service.StaticDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/static")
public class StaticDataController {

    private final StaticDataService staticDataService;

    public StaticDataController(StaticDataService staticDataService) {
        this.staticDataService = staticDataService;
    }

    @GetMapping("/securities")
    public List<Security> getSecurities() {
        return staticDataService.getAllSecurities();
    }

    @GetMapping("/accounts")
    public List<Account> getAccounts() {
        return staticDataService.getAllAccounts();
    }
}

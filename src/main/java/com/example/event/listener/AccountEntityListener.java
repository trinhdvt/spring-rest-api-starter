package com.example.event.listener;


import com.example.model.entity.Account;
import com.example.utils.MyStringUtils;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class AccountEntityListener {

    @PrePersist
    @PreUpdate
    private void onAnyUpdate(Account acc) {
        acc.setName(MyStringUtils.escapeHtml(acc.getName()));
    }

}
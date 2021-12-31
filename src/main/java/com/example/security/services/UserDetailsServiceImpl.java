package com.example.security.services;

import com.example.model.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository accRepo;

    @Autowired
    public UserDetailsServiceImpl(AccountRepository accRepo) {
        this.accRepo = accRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account acc = accRepo.findAccountByUsername(username);

        if (acc == null) {
            throw new UsernameNotFoundException("Username not found!");
        }

        return new CurrentUser(acc.getUsername(),
                acc.getPassword(),
                acc.isEnabled(),
                acc.getId(),
                acc.getRole());
    }
}
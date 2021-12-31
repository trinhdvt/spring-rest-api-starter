package com.example.services.impl;

import com.example.exception.HttpError;
import com.example.exception.UnauthorizedError;
import com.example.model.dto.request.LoginPayload;
import com.example.model.entity.Account;
import com.example.repository.AccountRepository;
import com.example.security.services.TokenServices;
import com.example.services.IAuthServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AuthServicesImpl implements IAuthServices {

    private final AccountRepository accountRepository;

    private final TokenServices tokenServices;

    private final AuthenticationManager authManager;

    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(LoginPayload loginPayload, HttpServletResponse resp) {
        String username = loginPayload.getUsername();
        String password = loginPayload.getPassword();

        try {
            // attempting to authenticate user with username and password
            authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            // if authentication is successful, generate token
            Account acc = accountRepository.findAccountByUsername(username);
            String accessToken = tokenServices.createAccessToken(acc);

            // add refresh token to response or do something else

            return accessToken;
        } catch (DisabledException ex) {

            // if user is disabled, throw error
            throw new HttpError("Account is not verified",
                    HttpStatus.LOCKED);

        } catch (AuthenticationException ex) {

            // if authentication fails, throw error
            throw new UnauthorizedError("Invalid username / password");
        }
    }


}
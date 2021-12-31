package com.example.controller;

import com.example.model.dto.request.LoginPayload;
import com.example.services.IAuthServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Validated
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IAuthServices authServices;

    @PostMapping("/login")
    public ResponseEntity<Object> login(LoginPayload loginPayload, HttpServletResponse resp) {

        String accessToken = authServices.login(loginPayload, resp);

        Map<String, Object> response = new HashMap<>();
        response.put("token", accessToken);
        return ResponseEntity.ok(response);
    }


}
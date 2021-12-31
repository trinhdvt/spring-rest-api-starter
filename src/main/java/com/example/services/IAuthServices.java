package com.example.services;

import com.example.model.dto.request.LoginPayload;

import javax.servlet.http.HttpServletResponse;

public interface IAuthServices {

    String login(LoginPayload loginPayload, HttpServletResponse resp);
}
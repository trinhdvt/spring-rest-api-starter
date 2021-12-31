package com.example.controller;

import com.example.security.services.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Validated
public class ExampleController {

    // secure this endpoint with @PreAuthorize
    @GetMapping("/get-me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> exampleGet(@AuthenticationPrincipal CurrentUser currentUser) {

        Long userId = currentUser.getUserId();

        return ResponseEntity.ok("example - " + userId);
    }

    // use cache for heavy computation requests
    @GetMapping("/cache-me")
    @Cacheable(value = "example", key = "#root.methodName")
    public ResponseEntity<?> cacheMe(){
        return ResponseEntity.ok("Cached");
    }

}
package com.example.security.ratelimit;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Optional;

public class ApiLimitInterceptor implements HandlerInterceptor {

    private ApiBucketManager bucketManager;
    private final int limit;
    private final Duration duration;
    private String additionalKeyPrefix = "";

    public ApiLimitInterceptor(int limit, Duration duration) {
        Assert.isTrue(limit > 0, "limit must be greater than 0");
        Assert.isTrue(duration.toMillis() > 0, "duration must be greater than 0");

        this.bucketManager = new ApiBucketManager();
        this.limit = limit;
        this.duration = duration;
    }

    public ApiLimitInterceptor(ApiBucketManager bucketManager, int limit, Duration duration) {
        this(limit, duration);
        this.bucketManager = bucketManager;
    }

    public ApiLimitInterceptor(ApiBucketManager bucketManager, int limit, Duration duration, String additionalKeyPrefix) {
        this(bucketManager, limit, duration);
        this.additionalKeyPrefix = additionalKeyPrefix;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse response, Object handler) throws Exception {
        String bucketKey = additionalKeyPrefix;

        String forwardHeader = "X-Forwarded-For"; // change this header to match your reverse-proxy config
        String clientIp = Optional.ofNullable(req.getHeader(forwardHeader)).orElse(req.getRemoteAddr());
        bucketKey += clientIp;

        Bucket bucket = bucketManager.resolveBucket(bucketKey, limit, duration);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            response.addHeader("API-Rate-Limit-Remaining", probe.getRemainingTokens() + "");
            return true;
        } else {
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            response.addHeader("API-Rate-Limit-Retry-After-Seconds", waitForRefill + "");
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "You have sent too much request");
            return false;
        }
    }

}
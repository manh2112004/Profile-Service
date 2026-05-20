package org.Profile.query.controller;

import org.Profile.query.model.response.ProfileResponse;
import org.Profile.query.queries.GetMyProfileQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/profiles")
public class ProfileQueryController {
    @Autowired
    private QueryGateway queryGateway;

    @GetMapping("/me")
    public CompletableFuture<ProfileResponse> getMyProfile(@AuthenticationPrincipal Jwt jwt) {
        return queryGateway.query(
                new GetMyProfileQuery(jwt.getSubject()),
                ResponseTypes.instanceOf(ProfileResponse.class)
        );
    }
}

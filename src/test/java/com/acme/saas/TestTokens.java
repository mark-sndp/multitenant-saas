package com.acme.saas;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Map;

/** Fetches access tokens from Keycloak's password grant for the demo test users. */
public final class TestTokens {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    private TestTokens() {
    }

    public static String accessTokenFor(String issuerUri, String username, String password) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", "acme-cli");
        form.add("username", username);
        form.add("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        ResponseEntity<Map<String, Object>> response = REST_TEMPLATE.exchange(
            issuerUri + "/protocol/openid-connect/token",
            org.springframework.http.HttpMethod.POST,
            new HttpEntity<>(form, headers),
            new ParameterizedTypeReference<Map<String, Object>>() {
            });
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new IllegalStateException("Failed to obtain token for " + username + ": " + response);
        }
        return (String) response.getBody().get("access_token");
    }
}

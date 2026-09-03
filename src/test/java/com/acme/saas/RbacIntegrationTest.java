package com.acme.saas;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class RbacIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String issuerUri() {
        return KEYCLOAK.getAuthServerUrl() + "/realms/acme";
    }

    @Test
    void tenantUserForbiddenFromAdminEndpoints() {
        String bobToken = TestTokens.accessTokenFor(issuerUri(), "bob@northwind.example", "Passw0rd!");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bobToken);
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/admin/tenants", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void unauthenticatedRequestRejected() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/products", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void tenantAdminCanCreateProductButTenantUserCannot() {
        String aliceToken = TestTokens.accessTokenFor(issuerUri(), "alice@northwind.example", "Passw0rd!");
        String bobToken = TestTokens.accessTokenFor(issuerUri(), "bob@northwind.example", "Passw0rd!");

        String body = "{\"sku\":\"NW-TEST-1\",\"name\":\"Test\",\"priceCents\":100}";

        HttpHeaders aliceHeaders = new HttpHeaders();
        aliceHeaders.setBearerAuth(aliceToken);
        aliceHeaders.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        ResponseEntity<String> aliceResponse = restTemplate.exchange(
                "/api/products", HttpMethod.POST, new HttpEntity<>(body, aliceHeaders), String.class);
        assertThat(aliceResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        HttpHeaders bobHeaders = new HttpHeaders();
        bobHeaders.setBearerAuth(bobToken);
        bobHeaders.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        ResponseEntity<String> bobResponse = restTemplate.exchange(
                "/api/products", HttpMethod.POST, new HttpEntity<>(body.replace("NW-TEST-1", "NW-TEST-2"), bobHeaders), String.class);
        assertThat(bobResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}

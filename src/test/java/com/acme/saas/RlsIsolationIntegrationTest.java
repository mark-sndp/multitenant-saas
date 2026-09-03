package com.acme.saas;

import com.acme.saas.product.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class RlsIsolationIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String issuerUri() {
        return KEYCLOAK.getAuthServerUrl() + "/realms/acme";
    }

    @Test
    void tenantOnlySeesOwnProducts() {
        String aliceToken = TestTokens.accessTokenFor(issuerUri(), "alice@northwind.example", "Passw0rd!");
        String carolToken = TestTokens.accessTokenFor(issuerUri(), "carol@globex.example", "Passw0rd!");

        ProductResponse[] northwindProducts = getProducts(aliceToken);
        ProductResponse[] globexProducts = getProducts(carolToken);

        assertThat(northwindProducts).extracting(ProductResponse::sku)
                .containsExactlyInAnyOrder("NW-WIDGET-1", "NW-GADGET-1");
        assertThat(globexProducts).extracting(ProductResponse::sku)
                .containsExactlyInAnyOrder("GX-GIZMO-1");
    }

    @Test
    void platformAdminSeesAllTenants() {
        String adminToken = TestTokens.accessTokenFor(issuerUri(), "admin@acme.io", "Passw0rd!");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        ResponseEntity<Object[]> response = restTemplate.exchange(
                "/api/admin/tenants", org.springframework.http.HttpMethod.GET, new HttpEntity<>(headers), Object[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    private ProductResponse[] getProducts(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        ResponseEntity<ProductResponse[]> response = restTemplate.exchange(
                "/api/products", org.springframework.http.HttpMethod.GET, new HttpEntity<>(headers), ProductResponse[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody();
    }
}

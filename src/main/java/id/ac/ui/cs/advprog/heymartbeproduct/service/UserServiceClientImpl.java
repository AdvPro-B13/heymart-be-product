package id.ac.ui.cs.advprog.heymartbeproduct.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import id.ac.ui.cs.advprog.heymartbeproduct.dto.UserResponseDto;

@Service
public class UserServiceClientImpl implements UserServiceClient {

    private final RestTemplate restTemplate;

    @Value("${user.api}")
    private String userServiceUrl;

    @Autowired
    public UserServiceClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean verifySupermarket(String token, String supermarketId) {
        if (token == null || supermarketId == null) {
            return false;
        }

        HttpEntity<String> entity = createHttpEntity(token);
        try {
            String url = userServiceUrl + "/get";
            ResponseEntity<UserResponseDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    UserResponseDto.class);
            return Optional.ofNullable(response.getBody())
                    .map(UserResponseDto::getSupermarketId)
                    .filter(id -> id.equals(supermarketId))
                    .isPresent();
        } catch (Exception e) {
            return false;
        }
    }

    protected HttpEntity<String> createHttpEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);
        return new HttpEntity<>(headers);
    }
}
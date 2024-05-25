package id.ac.ui.cs.advprog.heymartbeproduct.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import id.ac.ui.cs.advprog.heymartbeproduct.dto.UserResponseDto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserServiceClientImplTest {

    @InjectMocks
    UserServiceClientImpl userServiceClientImpl;

    @Mock
    RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testVerifySupermarket_Successful() {
        String token = "validToken";
        String supermarketId = "validSupermarketId";

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setSupermarketId(supermarketId);
        ResponseEntity<UserResponseDto> responseEntity = new ResponseEntity<>(userResponseDto, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(userServiceClientImpl.createHttpEntity(token)),
                eq(UserResponseDto.class))).thenReturn(responseEntity);

        boolean result = userServiceClientImpl.verifySupermarket(token, supermarketId);
        assertTrue(result);
    }

    @Test
    void testVerifySupermarket_Failed() {
        String token = "validToken";
        String supermarketId = "invalidSupermarketId";

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setSupermarketId("validSupermarketId");
        ResponseEntity<UserResponseDto> responseEntity = new ResponseEntity<>(userResponseDto, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(userServiceClientImpl.createHttpEntity(token)),
                eq(UserResponseDto.class))).thenReturn(responseEntity);

        boolean result = userServiceClientImpl.verifySupermarket(token, supermarketId);
        assertFalse(result);
    }

    @Test
    void testVerifySupermarket_TokenNull() {
        boolean result = userServiceClientImpl.verifySupermarket(null, "anySupermarketId");
        assertFalse(result);
    }

    @Test
    void testVerifySupermarket_Exception() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(UserResponseDto.class)))
                .thenThrow(new RuntimeException());

        boolean result = userServiceClientImpl.verifySupermarket("anyToken", "anySupermarketId");
        assertFalse(result);
    }

    @Test
    void testVerifySupermarket_TokenOrSupermarketIdNull() {
        boolean resultTokenNull = userServiceClientImpl.verifySupermarket(null, "validSupermarketId");
        assertFalse(resultTokenNull);

        boolean resultSupermarketIdNull = userServiceClientImpl.verifySupermarket("validToken", null);
        assertFalse(resultSupermarketIdNull);

        boolean resultBothNull = userServiceClientImpl.verifySupermarket(null, null);
        assertFalse(resultBothNull);
    }

}
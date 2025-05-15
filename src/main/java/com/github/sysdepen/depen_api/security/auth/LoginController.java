package com.github.sysdepen.depen_api.security.auth;

import jakarta.validation.Valid;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v1/login")
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {

	@PostMapping("/logar")
	public ResponseEntity<?> logar(@Valid @RequestBody Login login) {
	    RestTemplate restTemplate = new RestTemplate();
	    String keycloakUrl = "http://localhost:8080/realms/my-realm/protocol/openid-connect/token";

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

	    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
	    map.add("client_id", "depen");
	    map.add("client_secret", "ikuqfRi0vUHaHz3ACisZroIG3B0iU7Jp");
	    map.add("username", login.getUsername());
	    map.add("password", login.getPassword());
	    map.add("grant_type", "password");

	    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

	    try {
	        ResponseEntity<String> response = restTemplate.postForEntity(keycloakUrl, request, String.class);
	        if (response.getStatusCode().is2xxSuccessful()) {
	            return ResponseEntity.ok(response.getBody());
	        } else {
	            return ResponseEntity.status(response.getStatusCode()).body("Falha ao autenticar no Keycloak");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao realizar login: " + e.getMessage());
	    }
	}

}

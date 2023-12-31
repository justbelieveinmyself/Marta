package com.justbelieveinmyself.marta.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.justbelieveinmyself.marta.domain.dto.auth.OAuthTokenDto;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

@RestController
@RequestMapping("api/v1/oauth")
@CrossOrigin
@Tag(
        name = "OAuth",
        description = "The OAuth API"
)

public class OAuthController {
    private final UserService userService;

    public OAuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(description = "OAuth vkontakte login", deprecated = true)
    public User vk(@RequestBody OAuthTokenDto OAuthTokenDto) throws IOException {
        User userFromVK = getUserFromVK(OAuthTokenDto);
        return userService.save(userFromVK);
    }
    public User getUserFromVK(OAuthTokenDto OAuthTokenDto) throws IOException {
        User user = new User();
        String jsonFromVK = getJSONFromVK(OAuthTokenDto);
        Map<String, String> parsedMap = parseJSON(jsonFromVK);
        user.setGender( parsedMap.get("gender"));
        user.setFirstName( parsedMap.get("first_name"));
        user.setLastName(parsedMap.get("last_name"));
        // bdate, gender
        return user;
    }

    private Map<String, String> parseJSON(String jsonFromVK) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode jsonNode = objectMapper.readTree(jsonFromVK);
            JsonNode responseNode = jsonNode.get("response");

            Map<String, String> jsonMap = objectMapper.convertValue(responseNode.get(0), Map.class);
            return jsonMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getJSONFromVK(OAuthTokenDto OAuthTokenDto) throws IOException {
        String urlString = String.format("https://api.vk.com/method/users.get?user_ids=%s&v=5.131&fields=sex,bdate&access_token=%s"
                , OAuthTokenDto.getUserId()
                , OAuthTokenDto.getToken());
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", Integer.toString("".length()));
        String result="";
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(
                conn.getInputStream())))
        {
            String line;
            while ((line = bf.readLine()) != null) {
                result += line;
            }
        }
        return result;
    }
}

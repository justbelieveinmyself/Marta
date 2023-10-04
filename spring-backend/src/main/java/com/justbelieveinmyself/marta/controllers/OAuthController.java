package com.justbelieveinmyself.marta.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.dto.TokenDto;
import com.justbelieveinmyself.marta.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

@RestController
@RequestMapping("/oauth")
@CrossOrigin
public class OAuthController {
    @Autowired
    private UserService userService;
    @PostMapping
    public User vk(@RequestBody TokenDto tokenDto) throws IOException {
        User userFromVK = getUserFromVK(tokenDto);
        return userService.save(userFromVK);
    }
    public User getUserFromVK(TokenDto tokenDto) throws IOException {
        User user = new User();
        String jsonFromVK = getJSONFromVK(tokenDto);
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

    private String getJSONFromVK(TokenDto tokenDto) throws IOException {
        String urlString = String.format("https://api.vk.com/method/users.get?user_ids=%s&v=5.131&fields=sex,bdate&access_token=%s"
                ,tokenDto.getUserId()
                ,tokenDto.getToken());
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

package com.justbelieveinmyself.marta.domain.dto;

import com.justbelieveinmyself.marta.domain.entities.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Information about User")
public class SellerDto {
    private Long id;
    private String username;
    private ZonedDateTime registeredAt;
    private Long ratingCount;
    public static SellerDto of(User user){
        return new SellerDto(user.getId(), user.getUsername(), user.getRegisteredAt(), user.getRatingCount());
    }
}

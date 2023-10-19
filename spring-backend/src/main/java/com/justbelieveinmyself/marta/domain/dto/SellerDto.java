package com.justbelieveinmyself.marta.domain.dto;

import com.justbelieveinmyself.marta.domain.entities.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Information about User")
public class SellerDto {
    private Long id;
    private String username;
    private String email;
    public static SellerDto of(User user){
        return new SellerDto(user.getId(), user.getUsername(), user.getEmail());
    }
}

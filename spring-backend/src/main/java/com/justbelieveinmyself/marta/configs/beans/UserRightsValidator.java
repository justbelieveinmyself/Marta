package com.justbelieveinmyself.marta.configs.beans;

import com.justbelieveinmyself.marta.domain.dto.ProductDto;
import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.enums.Role;
import com.justbelieveinmyself.marta.exceptions.ForbiddenException;

public class UserRightsValidator {
    public void validateRights(User userFromAuthToken, User userFromInput) {
        if(!userFromInput.getId().equals(userFromAuthToken.getId())){
            throw new ForbiddenException("You don't have the rights!");
        }
    }

    public void validateRightsOrAdminRole(Product product, User productOwnerOrAdmin) {
        if (product.getSeller().getId().equals(productOwnerOrAdmin.getId()) || productOwnerOrAdmin.getRoles().contains(Role.ADMIN)) {
            return;
        }
        throw new ForbiddenException("You don't have rights!");
    }

    public void validateRights(ProductDto productDto, User productOwner) {
        if (!productDto.getSeller().getId().equals(productOwner.getId())) {
            throw new ForbiddenException("You don't have rights!");
        }
    }

    public void validateRights(Product product, User productOwner) {
        if (!product.getSeller().getId().equals(productOwner.getId())) {
            throw new ForbiddenException("You don't have rights!");
        }
    }


}

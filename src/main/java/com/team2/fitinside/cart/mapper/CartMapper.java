package com.team2.fitinside.cart.mapper;

import com.team2.fitinside.cart.dto.CartCreateRequestDto;
import com.team2.fitinside.cart.dto.CartResponseDto;
import com.team2.fitinside.cart.entity.Cart;
import com.team2.fitinside.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CartMapper {

    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    @Mapping(target = "member", ignore = true) // user 필드 매핑 제외
    @Mapping(target = "product", ignore = true) // product 필드 매핑 제외
    Cart toEntity(CartCreateRequestDto cartCreateRequestDto);

    @Mapping(source="product", target = "productId")
    CartResponseDto toCartResponseDto(Cart cart);

    default Long mapProductToLong(Product product) { return product != null ? product.getId() : null; }
}

package com.sanjay.store.products;

import com.sanjay.store.carts.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface Product_Mapper {

    @Mapping( target="categoryId",source="category.id")
    ProductDto toDto(Product product);

    Product toEntity(ProductDto product);

    @Mapping(target = "id",ignore = true)
    void update(ProductDto request, @MappingTarget Product product);
}

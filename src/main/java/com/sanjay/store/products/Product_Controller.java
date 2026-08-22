package com.sanjay.store.products;

import com.sanjay.store.carts.ProductDto;
import lombok.AllArgsConstructor;

import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class Product_Controller {

    private ProductRepository productRepository;
    private Product_Mapper productMapper;
    private CategoryRepository categoryRepository;
    @GetMapping
    public List<ProductDto> getAllproducts(@RequestHeader(name="x-auth-token",required = false) String Author_Token, @RequestParam(name="categoryId",required = false)Byte categoryId)
    {
        List<Product> products;
        if(categoryId!=null)
        {
            products=productRepository.findByCategoryId(categoryId);
        }
        else
        {
            products=productRepository.findAllByCategory();
        }
        return products.stream().map(productMapper::toDto).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto, UriComponentsBuilder uribuilder)
    {
        var category=categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if(category==null)
        {
            return ResponseEntity.notFound().build();
        }
        var Product=productMapper.toEntity(productDto);
        Product.setCategory(category);
        productRepository.save(Product);
        productDto.setId(Product.getId());
        var uri = uribuilder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri();
        return ResponseEntity.created(uri).body(productDto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable(name = "id") Long id,
                                          @RequestBody ProductDto data_update) {
        var category=categoryRepository.findById(data_update.getCategoryId()).orElse(null);
        if(category==null)
        {
            return ResponseEntity.badRequest().build();
        }
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        productMapper.update(data_update, product);
        product.setCategory(category);
        productRepository.save(product);
        data_update.setId(product.getId());
        return ResponseEntity.ok(data_update);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }
}

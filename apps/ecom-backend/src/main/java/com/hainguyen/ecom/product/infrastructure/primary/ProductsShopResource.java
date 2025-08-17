package com.hainguyen.ecom.product.infrastructure.primary;

import com.hainguyen.ecom.product.application.ProductsApplicationService;
import com.hainguyen.ecom.product.domain.aggregate.FilterQueryBuilder;
import com.hainguyen.ecom.product.domain.aggregate.Product;
import com.hainguyen.ecom.product.domain.vo.ProductSize;
import com.hainguyen.ecom.product.domain.vo.PublicId;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products-shop")
public class ProductsShopResource {

  private final ProductsApplicationService productsApplicationService;

  public ProductsShopResource(ProductsApplicationService productsApplicationService) {
    this.productsApplicationService = productsApplicationService;
  }

  @GetMapping("/featured")
  public ResponseEntity<Page<RestProduct>> getAllProductsFeaturedTrue(Pageable pageable) {
    Page<Product> products = productsApplicationService.getFeaturedProducts(pageable);

    Page<RestProduct> restProducts = new PageImpl<>(
      products.getContent().stream().map(RestProduct::from).collect(Collectors.toList()),
      pageable,
      products.getTotalElements()
    );
    return ResponseEntity.ok(restProducts);
  }

  @GetMapping("/find-one")
  public ResponseEntity<RestProduct> getOne(@RequestParam UUID publicId) {
    var productOpt = productsApplicationService.findOne(new PublicId(publicId));

    return productOpt.map(product -> ResponseEntity.ok(RestProduct.from(product)))
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/related")
  public ResponseEntity<Page<RestProduct>> getAllRelated(Pageable pageable,
                                                         @RequestParam("publicId") UUID productPublicId) {
    try {

      Page<Product> products = productsApplicationService.findRelated(pageable, new PublicId(productPublicId));

      Page<RestProduct> restProducts = new PageImpl<>(
        products.getContent().stream().map(RestProduct::from).collect(Collectors.toList()),
        pageable,
        products.getTotalElements()
      );
      return ResponseEntity.ok(restProducts);
    } catch (EntityNotFoundException enfe) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/filter")
  public ResponseEntity<Page<RestProduct>> filter(Pageable pageable,
                                                  @RequestParam(value = "category") UUID categoryPublicId,
                                                  @RequestParam(value = "productSizes", required = false) List<ProductSize> productSizes) {
    // Spring Boot can automatically convert a comma-separated query string into
    // a list of enum values if the parameter is defined as List<EnumType>, ProductSize.

    var filterQueryBuilder = FilterQueryBuilder.filterQuery()
      .categoryPublicId(new PublicId(categoryPublicId));

    if(productSizes !=null){
      filterQueryBuilder.sizes(productSizes);
    }

    Page<Product> products = productsApplicationService.filter(pageable, filterQueryBuilder.build());
    Page<RestProduct> restProducts = new PageImpl<>(
      products.getContent().stream().map(RestProduct::from).collect(Collectors.toList()),
      pageable,
      products.getTotalElements()
    );
    return ResponseEntity.ok(restProducts);
  }



}

package com.hainguyen.ecom.product.infrastructure.primary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hainguyen.ecom.product.application.ProductsApplicationService;
import com.hainguyen.ecom.product.domain.aggregate.Product;
import com.hainguyen.ecom.product.domain.vo.PublicId;
import com.hainguyen.ecom.product.infrastructure.primary.exceptions.MultipartPictureException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.GeneratedValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductsAdminResource {
  private static final Logger log = LoggerFactory.getLogger(ProductsAdminResource.class);

  private final ProductsApplicationService productsApplicationService;
  public static final String ROLE_ADMIN = "ROLE_ADMIN";
  private final ObjectMapper objectMapper = new ObjectMapper();

  public ProductsAdminResource(ProductsApplicationService productsApplicationService) {
    this.productsApplicationService = productsApplicationService;
  }

  @PreAuthorize("hasAnyRole('" + ROLE_ADMIN + "')")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<RestProduct> save(MultipartHttpServletRequest request,
                                          @RequestPart("dto") String productRaw)
    throws JsonProcessingException {

    Set<RestPicture> pictures = request.getFileMap().values().stream()
      .map(mapMultipartFileToRestPicture())
      .collect(Collectors.toSet());
    RestProduct newRestProduct = objectMapper.readValue(productRaw, RestProduct.class);
    newRestProduct.addAllPictures(pictures);

    Product product = productsApplicationService.createProduct(RestProduct.to(newRestProduct));
    return ResponseEntity.ok(RestProduct.from(product));
  }

  @DeleteMapping
  @PreAuthorize("hasAnyRole('" + ROLE_ADMIN + "')")
  public ResponseEntity<UUID> delete(@RequestParam UUID publicId) {
    try {
      PublicId deleteProduct = productsApplicationService.deleteProduct(new PublicId(publicId));
      return ResponseEntity.ok(deleteProduct.value());
    } catch (EntityNotFoundException e) {
      log.error("Could not delete product with id {}", publicId, e);
      ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
      return ResponseEntity.of(problemDetail).build();
    }
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('" + ROLE_ADMIN + "')")
  public ResponseEntity<Page<RestProduct>> getAll(Pageable pageable) {
    Page<Product> products = productsApplicationService.findAllProducts(pageable);
    Page<RestProduct> restProducts = new PageImpl<>(
      products.getContent().stream().map(RestProduct::from).collect(Collectors.toList()),
      pageable,
      products.getTotalElements()
    );
    return ResponseEntity.ok(restProducts);
  }


  private Function<MultipartFile, RestPicture> mapMultipartFileToRestPicture() {
    return multipartFile -> {
      try {
        return new RestPicture(multipartFile.getBytes(), multipartFile.getContentType());
      } catch (IOException e) {
        throw new MultipartPictureException(String.format("Cannot parse multipart file: %s", multipartFile.getOriginalFilename()));
      }
    };
  }

}

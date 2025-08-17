package com.hainguyen.ecom.product.infrastructure.primary;

import com.hainguyen.ecom.product.application.ProductsApplicationService;
import com.hainguyen.ecom.product.domain.aggregate.Category;
import com.hainguyen.ecom.product.domain.vo.PublicId;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

import static com.hainguyen.ecom.product.infrastructure.primary.ProductsAdminResource.ROLE_ADMIN;

@RestController
@RequestMapping("/api/categories")
public class CategoriesResource {
  private static final Logger log = LoggerFactory.getLogger(CategoriesResource.class);
  private final ProductsApplicationService productsApplicationService;

  public CategoriesResource(ProductsApplicationService productsApplicationService) {
    this.productsApplicationService = productsApplicationService;
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('"+ROLE_ADMIN+"')")
  public ResponseEntity<RestCategory> save (@RequestBody RestCategory restCategory){
    var newCategory = RestCategory.to(restCategory);
    var savedCategory = productsApplicationService.createCategory(newCategory);

    return ResponseEntity.ok(RestCategory.from(savedCategory));
  }

  @DeleteMapping
  @PreAuthorize("hasAnyRole('"+ROLE_ADMIN+"')")
  public ResponseEntity<UUID> delete(@RequestParam UUID publicId){
    try{
      PublicId deletedCategoryPublicId = productsApplicationService.deleteCategory(new PublicId(publicId));

      return ResponseEntity.ok(deletedCategoryPublicId.value());
    }catch (EntityNotFoundException e){
      log.error("Could not delete category with id {}",publicId,e);
      ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,e.getMessage());
      return ResponseEntity.of(problemDetail).build();
    }
  }

  @GetMapping
  public ResponseEntity<Page<RestCategory>> findAll(Pageable pageable){
    Page<Category> categories = productsApplicationService.findAllCategories(pageable);
    Page<RestCategory> restCategories = new PageImpl<>(
      categories.getContent().stream().map(RestCategory::from).collect(Collectors.toList()),
      pageable,
      categories.getTotalElements()
    );
    return ResponseEntity.ok(restCategories);
  }


}

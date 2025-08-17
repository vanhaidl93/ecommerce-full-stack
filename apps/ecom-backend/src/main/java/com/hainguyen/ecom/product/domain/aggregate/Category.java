package com.hainguyen.ecom.product.domain.aggregate;

import com.hainguyen.ecom.product.domain.vo.CategoryName;
import com.hainguyen.ecom.product.domain.vo.PublicId;
import com.hainguyen.ecom.shared.error.domain.Assert;
import org.jilt.Builder;

import java.util.UUID;

@Builder
public class Category {

  private final CategoryName name;

  private Long dbId;
  private PublicId publicId;

  public Category(CategoryName name, Long dbId, PublicId publicId) {
    assertMandatoryFields(name);
    this.name = name;
    this.dbId = dbId;
    this.publicId = publicId;
  }

  private void assertMandatoryFields(CategoryName name) {
    Assert.notNull("name", name);
  }

  public void initDefaultFields(){
    this.publicId = new PublicId(UUID.randomUUID());
  }

  public CategoryName getName() {
    return name;
  }

  public Long getDbId() {
    return dbId;
  }

  public PublicId getPublicId() {
    return publicId;
  }
}

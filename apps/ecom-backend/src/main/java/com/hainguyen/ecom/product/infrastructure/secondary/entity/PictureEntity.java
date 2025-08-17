package com.hainguyen.ecom.product.infrastructure.secondary.entity;

import com.hainguyen.ecom.product.domain.aggregate.Picture;
import com.hainguyen.ecom.product.domain.aggregate.PictureBuilder;
import com.hainguyen.ecom.shared.jpa.AbstractAuditingEntity;
import jakarta.persistence.*;
import org.jilt.Builder;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "product_picture", schema = "ecommerce")
@Builder
public class PictureEntity extends AbstractAuditingEntity<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pictureSequence")
  @SequenceGenerator(name = "pictureSequence", sequenceName = "product_picture_sequence",
    allocationSize = 1, schema = "ecommerce")
  Long id;

  @Lob
  @Column(nullable = false)
  private byte[] file;

  @Column(name = "file_content_type", nullable = false)
  private String mimeType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_fk",nullable = false)
  private ProductEntity product;

  public PictureEntity() {
  }

  public PictureEntity(Long id, byte[] file, String mimeType, ProductEntity product) {
    this.id = id;
    this.file = file;
    this.mimeType = mimeType;
    this.product = product;
  }

  // utility
  public static PictureEntity from(Picture picture) {

    return PictureEntityBuilder.pictureEntity()
      .file(picture.file())
      .mimeType(picture.mimeType())
      .build();
  }

  public static Picture toDomain(PictureEntity pictureEntity) {
    return PictureBuilder.picture()
      .file(pictureEntity.getFile())
      .mimeType(pictureEntity.getMimeType())
      .build();
  }

  public static Set<PictureEntity> from(Set<Picture> pictures) {
    return pictures.stream().map(PictureEntity::from).collect(Collectors.toSet());
  }

  public static Set<Picture> toDomain(Set<PictureEntity> pictureEntities) {
    return pictureEntities.stream().map(PictureEntity::toDomain).collect(Collectors.toSet());
  }

  @Override
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public byte[] getFile() {
    return file;
  }

  public void setFile(byte[] file) {
    this.file = file;
  }

  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  public ProductEntity getProduct() {
    return product;
  }

  public void setProduct(ProductEntity product) {
    this.product = product;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof PictureEntity that)) return false;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}

package com.hainguyen.ecom.order.infrastructure.secondary.entity;

import com.hainguyen.ecom.order.domain.user.aggregate.Authority;
import com.hainguyen.ecom.order.domain.user.aggregate.AuthorityBuilder;
import com.hainguyen.ecom.order.domain.user.valueobject.AuthorityName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.jilt.Builder;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "authority", schema = "ecommerce")
@Builder
public class AuthorityEntity implements Serializable {


  @NotNull
  @Size(max = 50)
  @Id
  @Column(length = 50)
  private String name;

  public AuthorityEntity() {
  }

  public AuthorityEntity(String name) {
    this.name = name;
  }

  public static Set<AuthorityEntity> from(Set<Authority> authorities) {
    return authorities.stream()
      .map(authority -> AuthorityEntityBuilder.authorityEntity().name(authority.getName().name()).build())
      .collect(Collectors.toSet());
  }

  public static Set<Authority> toDomain(Set<AuthorityEntity> authorityEntities) {
    return authorityEntities.stream()
      .map(authorityEntity -> AuthorityBuilder.authority()
        .name(new AuthorityName(authorityEntity.getName()))
        .build()
      ).collect(Collectors.toSet());
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof AuthorityEntity that)) return false;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name);
  }
}

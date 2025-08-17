package com.hainguyen.ecom.order.infrastructure.secondary.entity;

import com.hainguyen.ecom.order.domain.user.aggregate.User;
import com.hainguyen.ecom.order.domain.user.aggregate.UserBuilder;
import com.hainguyen.ecom.order.domain.user.valueobject.*;
import com.hainguyen.ecom.shared.jpa.AbstractAuditingEntity;
import jakarta.persistence.*;
import org.jilt.Builder;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "ecommerce_user", schema = "ecommerce")
@Builder
public class UserEntity extends AbstractAuditingEntity<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "userSequenceGenerator")
  @SequenceGenerator(name = "userSequenceGenerator",sequenceName = "user_sequence", allocationSize = 1, schema ="ecommerce")
  private Long id; // userDbId
  private UUID publicId; // publicId
  private String firstName;
  private String lastName;
  private String email;
  private String addressStreet;
  private String addressCity;
  private String addressZipCode;
  private String addressCountry;
  private String imageUrl;
  private Instant lastSeen;

  @ManyToMany( cascade = CascadeType.REMOVE)
  @JoinTable(
    name="user_authority",schema ="ecommerce",
    joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
    inverseJoinColumns = {@JoinColumn(name = "authority_name",referencedColumnName = "name")}
  )
  private Set<AuthorityEntity> authorities;

  public UserEntity() {
  }

  public UserEntity(Long id, UUID publicId, String firstName, String lastName, String email, String addressStreet, String addressCity, String addressZipCode, String addressCountry, String imageUrl, Instant lastSeen, Set<AuthorityEntity> authorities) {
    this.id = id;
    this.publicId = publicId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.addressStreet = addressStreet;
    this.addressCity = addressCity;
    this.addressZipCode = addressZipCode;
    this.addressCountry = addressCountry;
    this.imageUrl = imageUrl;
    this.lastSeen = lastSeen;
    this.authorities = authorities;
  }

  // utility class
  public void updateFromUser(User user){
    this.email = user.getEmail().value();
    this.lastName = user.getLastname().value();
    this.firstName = user.getFirstname().value();
    this.imageUrl = user.getImageUrl().value();
    this.lastSeen = user.getLastSeen();
  }

  public static UserEntity from(User user){
    UserEntityBuilder userEntityBuilder = UserEntityBuilder.userEntity();
    if(user.getImageUrl() != null){
      userEntityBuilder.imageUrl(user.getImageUrl().value());
    }
    if (user.getUserPublicId() != null){
      userEntityBuilder.publicId(user.getUserPublicId().value());
    }
    if(user.getUserAddress() != null){
      userEntityBuilder.addressCity(user.getUserAddress().city());
      userEntityBuilder.addressCountry(user.getUserAddress().country());
      userEntityBuilder.addressStreet(user.getUserAddress().street());
      userEntityBuilder.addressZipCode(user.getUserAddress().zipcode());
    }
    return userEntityBuilder
      .authorities(AuthorityEntity.from(user.getAuthorities()))
      .email(user.getEmail().value())
      .firstName(user.getFirstname().value())
      .lastName(user.getLastname().value())
      .lastSeen(user.getLastSeen())
      .id(user.getDbId())
      .build();
  }

  public static User toDomain(UserEntity userEntity){
    UserBuilder userBuilder = UserBuilder.user();
    if(userEntity.getImageUrl() != null){
      userBuilder.imageUrl(new UserImageUrl(userEntity.getImageUrl()));
    }
    if(userEntity.getAddressStreet() != null){
      userBuilder.userAddress(
        UserAddressBuilder.userAddress()
          .country(userEntity.getAddressCountry())
          .city(userEntity.getAddressCity())
          .zipcode(userEntity.getAddressZipCode())
          .street(userEntity.getAddressStreet())
          .build());
    }

    return userBuilder.email(new UserEmail(userEntity.getEmail()))
      .lastname(new UserLastname(userEntity.getLastName()))
      .firstname(new UserFirstname(userEntity.getFirstName()))
      .authorities(AuthorityEntity.toDomain(userEntity.getAuthorities()))
      .userPublicId(new UserPublicId(userEntity.getPublicId()))
      .lastModifiedDate(userEntity.getLastModifiedDate())
      .createdDate(userEntity.getCreatedDate())
      .dbId(userEntity.getId())
      .build();
  }

  public static Set<UserEntity> from(List<User> users){
    return users.stream().map(UserEntity::from).collect(Collectors.toSet());
  }

  public static Set<User> toDomain(List<UserEntity> userEntities){
    return userEntities.stream().map(UserEntity::toDomain).collect(Collectors.toSet());
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UUID getPublicId() {
    return publicId;
  }

  public void setPublicId(UUID publicId) {
    this.publicId = publicId;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAddressStreet() {
    return addressStreet;
  }

  public void setAddressStreet(String addressStreet) {
    this.addressStreet = addressStreet;
  }

  public String getAddressCity() {
    return addressCity;
  }

  public void setAddressCity(String addressCity) {
    this.addressCity = addressCity;
  }

  public String getAddressZipCode() {
    return addressZipCode;
  }

  public void setAddressZipCode(String addressZipCode) {
    this.addressZipCode = addressZipCode;
  }

  public String getAddressCountry() {
    return addressCountry;
  }

  public void setAddressCountry(String addressCountry) {
    this.addressCountry = addressCountry;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public Instant getLastSeen() {
    return lastSeen;
  }

  public void setLastSeen(Instant lastSeen) {
    this.lastSeen = lastSeen;
  }

  public Set<AuthorityEntity> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Set<AuthorityEntity> authorities) {
    this.authorities = authorities;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof UserEntity that)) return false;
    // 'that' is just a typed alias for 'o' - Java 16+
    return Objects.equals(publicId, that.publicId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(publicId);
  }
}

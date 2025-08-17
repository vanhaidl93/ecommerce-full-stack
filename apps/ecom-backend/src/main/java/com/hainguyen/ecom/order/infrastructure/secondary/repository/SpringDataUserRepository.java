package com.hainguyen.ecom.order.infrastructure.secondary.repository;

import com.hainguyen.ecom.order.domain.user.aggregate.User;
import com.hainguyen.ecom.order.domain.user.repository.UserRepository;
import com.hainguyen.ecom.order.domain.user.valueobject.UserAddressToUpdate;
import com.hainguyen.ecom.order.domain.user.valueobject.UserEmail;
import com.hainguyen.ecom.order.domain.user.valueobject.UserPublicId;
import com.hainguyen.ecom.order.infrastructure.secondary.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SpringDataUserRepository implements UserRepository {

  private final JpaUserRepository jpaUserRepository;

  public SpringDataUserRepository(JpaUserRepository jpaUserRepository) {
    this.jpaUserRepository = jpaUserRepository;
  }

  @Override
  public void save(User user) {

    if(user.getDbId() !=null){
      // update user int existing userEntity
      Optional<UserEntity> userEntityOptional = jpaUserRepository.findById(user.getDbId());
      if(userEntityOptional.isPresent()){
        UserEntity userEntityToUpdate = userEntityOptional.get();
        userEntityToUpdate.updateFromUser(user);
        jpaUserRepository.saveAndFlush(userEntityToUpdate);
      }
    }else {
      // save new
      jpaUserRepository.save(UserEntity.from(user));
    }
  }

  @Override
  public Optional<User> get(UserPublicId userPublicId) {
    return jpaUserRepository.findOneByPublicId(userPublicId.value())
      .map(UserEntity::toDomain);
  }

  @Override
  public Optional<User> getOneByEmail(UserEmail userEmail) {
    return jpaUserRepository.findByEmail(userEmail.value())
      .map(UserEntity::toDomain);
  }

  @Override
  public void updateAddress(UserPublicId userPublicId, UserAddressToUpdate userAddressToUpdate) {
    jpaUserRepository.updateAddress(
      userPublicId.value(),
      userAddressToUpdate.userAddress().street(),
      userAddressToUpdate.userAddress().city(),
      userAddressToUpdate.userAddress().country(),
      userAddressToUpdate.userAddress().zipcode());
  }
}

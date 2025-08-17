package com.hainguyen.ecom.order.domain.user.repository;

import com.hainguyen.ecom.order.domain.user.aggregate.User;
import com.hainguyen.ecom.order.domain.user.valueobject.UserAddressToUpdate;
import com.hainguyen.ecom.order.domain.user.valueobject.UserEmail;
import com.hainguyen.ecom.order.domain.user.valueobject.UserPublicId;

import java.util.Optional;

public interface UserRepository {

  void save(User user);

  Optional<User> get(UserPublicId userPublicId);

  Optional<User> getOneByEmail(UserEmail userEmail);

  void updateAddress(UserPublicId userPublicId, UserAddressToUpdate userAddressToUpdate);
}

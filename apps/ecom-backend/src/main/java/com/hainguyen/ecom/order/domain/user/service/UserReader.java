package com.hainguyen.ecom.order.domain.user.service;

import com.hainguyen.ecom.order.domain.user.aggregate.User;
import com.hainguyen.ecom.order.domain.user.repository.UserRepository;
import com.hainguyen.ecom.order.domain.user.valueobject.UserEmail;
import com.hainguyen.ecom.order.domain.user.valueobject.UserPublicId;

import java.util.Optional;

// Read User domain over UserRepository
public class UserReader {

  private final UserRepository userRepository;

  public UserReader(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Optional<User> getUserByEmail(UserEmail userEmail) {
    return userRepository.getOneByEmail(userEmail);
  }

  public Optional<User> getByPublicId(UserPublicId userPublicId) {
    return userRepository.get(userPublicId);
  }

}

package com.hainguyen.ecom.order.application;

import com.hainguyen.ecom.order.domain.user.aggregate.User;
import com.hainguyen.ecom.order.domain.user.repository.UserRepository;
import com.hainguyen.ecom.order.domain.user.service.UserReader;
import com.hainguyen.ecom.order.domain.user.service.UserSynchronizer;
import com.hainguyen.ecom.order.domain.user.valueobject.UserAddressToUpdate;
import com.hainguyen.ecom.order.domain.user.valueobject.UserEmail;
import com.hainguyen.ecom.order.infrastructure.secondary.service.kinde.KindeService;
import com.hainguyen.ecom.shared.authentication.application.AuthenticatedUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsersApplicationService {

  private final UserSynchronizer userSynchronizer;
  private final UserReader userReader;

  public UsersApplicationService(KindeService kindeService, UserRepository userRepository){
    userSynchronizer = new UserSynchronizer(userRepository,kindeService);
    userReader = new UserReader(userRepository);
  }

  @Transactional
  public User getAuthenticatedUser(Jwt jwtToken, boolean forceReSync) {
    userSynchronizer.syncWithIdp(jwtToken,forceReSync);
    return userReader.getUserByEmail(new UserEmail(AuthenticatedUser.username().get()))
      .orElseThrow();
  }

  @Transactional(readOnly = true)
  public User getAuthenticatedUser(){
    return userReader.getUserByEmail(new UserEmail(AuthenticatedUser.username().get()))
      .orElseThrow();
  }

  @Transactional
  public void updateAddress(UserAddressToUpdate userAddressToUpdate){
    userSynchronizer.updateAddress(userAddressToUpdate);
  }

}

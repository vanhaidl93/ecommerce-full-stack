package com.hainguyen.ecom.order.domain.user.service;

import com.hainguyen.ecom.order.domain.user.aggregate.User;
import com.hainguyen.ecom.order.domain.user.repository.UserRepository;
import com.hainguyen.ecom.order.domain.user.valueobject.UserAddressToUpdate;
import com.hainguyen.ecom.order.infrastructure.secondary.service.kinde.KindeService;
import com.hainguyen.ecom.shared.authentication.application.AuthenticatedUser;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserSynchronizer {

  private final UserRepository userRepository;
  private final KindeService kindeService;

  private static final String UPDATE_AT_KEY = "last_signed_in";
  // IDP (Identify provider - Kinde) update (email, password....)

  public UserSynchronizer(UserRepository userRepository, KindeService kindeService) {
    this.userRepository = userRepository;
    this.kindeService = kindeService;
  }

  /*
 * example JWT access token payload
 *
 {
    "aud": [
      "https://hainguyenkinde.kinde.com/api"
    ],
    "azp": "83e6ffcc2e724fd49667af8423a28cad",
    "exp": 1755188283,
    "gty": [
      "client_credentials"
    ],
    "iat": 1755101883,
    "iss": "https://hainguyenkinde.kinde.com",
    "jti": "89d888ea-3385-49e9-9d6f-84a5f02ce3ae",
    "scope": "read:users",
    "scp": [],
    "v": "2"
  }
 * --> payload, kinde management api publish access token to ecom-backend.
 *
 *
 *
 {
  "aud": [
    "http://localhost:8080/api"
  ],
  "azp": "26f6e968189341cab933ae63986876fa",
  "email": "vanhaidl93@gmail.com",
  "exp": 1755250550,
  "iat": 1755164150,
  "iss": "https://hainguyenkinde.kinde.com",
  "jti": "8970778e-4d63-4847-b055-f8a6e17087de",
  "org_code": "org_ab596313e40",
  "permissions": [],
  "roles": [
    {
      "id": "019853c7-6b7c-c309-4909-dd17ed017e60",
      "key": "ROLE_USER",
      "name": "Role User"
    }
  ],
  "scp": [],
  "sub": "kp_a5ef780eae164abf88a1b4c89bab1777"
 }
 * --> payload, custom api publish access token to ecom-frontend,
 * ecom-frontend send to ecom-backend to get accessible resource server (OIDC standard)
 * "sub", subject, represent to user info.
 * */
  public void syncWithIdp(Jwt jwtToken, boolean forceResync) {
    Map<String, Object> claims = jwtToken.getClaims();
    List<String> rolesFromToken = AuthenticatedUser.extractRolesFromToken(jwtToken);
    Map<String, Object> userInfo = kindeService.getUserInfo(claims.get("sub").toString());
    User user = User.fromTokenAttributes(userInfo, rolesFromToken);

    // update for existingUser from database.
    Optional<User> existingUser = userRepository.getOneByEmail(user.getEmail());
    if (existingUser.isPresent()) {
      if (existingUser.get().getLastModifiedDate() !=null) {
        Instant lastModifiedDate = existingUser.orElseThrow().getLastModifiedDate();
        Instant lastSignedIn = Instant.parse(userInfo.get(UPDATE_AT_KEY).toString());

        if (lastModifiedDate.isAfter(lastModifiedDate) || forceResync) {
          updateUser(user, existingUser.get());
        }
      }
    } else {
      // save new user from Token.
      user.initFieldForSignup();
      userRepository.save(user);
    }
  }

  public void updateAddress(UserAddressToUpdate userAddressToUpdate) {
    userRepository.updateAddress(userAddressToUpdate.userPublicId(), userAddressToUpdate);
  }

  private void updateUser(User user, User existingUser) {
    existingUser.updateFromUser(user);
    userRepository.save(existingUser);
  }


}

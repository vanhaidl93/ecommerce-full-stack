export interface BaseUser {
  firstName?: string;
  lastName?: string;
  email?: string;
  imageUrl?: string;
  pulicId?: string;
}

export interface ConnectedUser extends BaseUser {
  authorities?: string[];
}

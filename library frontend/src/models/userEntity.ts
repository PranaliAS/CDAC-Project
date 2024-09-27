import Role from "./role";

class User {
  userName: string;
  password: string;
  fullName: string;
  emailAddress: string;
  contactNumber: string;
  role: Role = Role.USER;
  jwtAccessToken?: string;
  jwtRefreshToken?: string;
  userId?: number;

  constructor(
    userName: string,
    password: string,
    fullName: string,
    emailAddress: string,
    contactNumber: string
  ) {
    this.userName = userName;
    this.password = password;
    this.fullName = fullName;
    this.emailAddress = emailAddress;
    this.contactNumber = contactNumber;
  }
}

export default User;

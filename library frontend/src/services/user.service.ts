import { USER_BASE_URL } from "../commons/constants";
import axios from "axios";
import { AuthHeader } from "./base.service";
import Role from "../models/role";

class UserService {
  changeUserRole(role: Role, username: string) {
    return axios.put(
      USER_BASE_URL + "/change" + role + "?userName=" + username,
      {},
      { headers: AuthHeader() }
    );
  }
}

export default new UserService();

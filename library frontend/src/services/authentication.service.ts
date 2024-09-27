import axios from "axios";
import { AUTHENTICATION_BASE_URL } from "../commons/constants";
import User from "../models/userEntity";

class AuthenticationService {
  signIn(user: any) {
    return axios.post(AUTHENTICATION_BASE_URL + "/signIn", user);
  }

  signUp(user: any) {
    return axios.post(AUTHENTICATION_BASE_URL + "/signUp", user);
  }

  getCurrentUser() {
    const currentUserStr = localStorage.getItem("currentUser");
    if (currentUserStr) return JSON.parse(currentUserStr);
  }

  clearCurrentUser() {
    localStorage.removeItem("currentUser");
  }
}

export default new AuthenticationService();

import axios from "axios";
import { sessionHistory } from "../commons/history";
import authenticationService from "./authentication.service";

export const AuthHeader = () => {
  const currentSignInUser = authenticationService.getCurrentUser();

  return {
    "Content-Type": "application/json",
    Authorization: `Bearer ${currentSignInUser?.jwtAccessToken}`,
  };
};

export function handleResponseWithLoginCheck() {
  axios.interceptors.response.use(
    (response) => response,
    (error) => {
      const currentUser = authenticationService.getCurrentUser();
      const isSignedIn = currentUser?.jwtAccessToken;
      const status = error?.response?.status;

      if (isSignedIn && [401, 403].includes(status)) {
        authenticationService.clearCurrentUser();
        sessionHistory.push("/login");
      }
      return Promise.reject(error);
    }
  );
}

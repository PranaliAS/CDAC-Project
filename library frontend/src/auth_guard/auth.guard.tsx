import authenticationService from "../services/authentication.service";
import Role from "../models/role";
import { useLocation } from "react-router-dom";

export const AuthGuard = (children: any, roles: Role) => {
  const currentUser = authenticationService.getCurrentUser();
  const location = useLocation();

  const authorize = () => {
    if (!currentUser) {
      return location("/login");
    }

    if (roles?.indexOf(currentUser.role) === -1) {
      return location("/login");
    }
    return children;
  };

  return authorize();
};

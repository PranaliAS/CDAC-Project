import { NavLink } from "react-router-dom";
import authenticationService from "../../services/authentication.service";
import { SpinnerLoading } from "../Utils/SpinnerLoading";
import { sessionHistory } from "../../commons/history";

export const Navbar = () => {
  const currentSigInUser = authenticationService.getCurrentUser();

  const handleLogOut = async () => {
    authenticationService.clearCurrentUser();
    sessionHistory.push("/login");
    window.location.reload();
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-dark main-color py-3">
      <div className="container-fluid">
        <span className="navbar-brand">Library Management System</span>
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNavDropdown"
          aria-controls="navbarNavDropdown"
          aria-expanded="false"
          aria-label="Toggle Navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarNavDropdown">
          <ul className="navbar-nav">
            <li className="nav-item">
              <NavLink className="nav-link" to="/">
                Home
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink className="nav-link" to="/search">
                Search Books
              </NavLink>
            </li>
            {currentSigInUser?.userId && (
              <li className="nav-item">
                <NavLink className="nav-link" to="/shelf">
                  Shelf
                </NavLink>
              </li>
            )}
            {currentSigInUser?.userId && (
              <li className="nav-item">
                <NavLink className="nav-link" to="/messages">
                  messages
                </NavLink>
              </li>
            )}
            {currentSigInUser?.userId && currentSigInUser?.role === "ADMIN" && (
              <li className="nav-item">
                <NavLink className="nav-link" to="/admin">
                  Admin
                </NavLink>
              </li>
            )}
          </ul>
          {currentSigInUser?.userId ? (
            <ul className="navbar-nav ms-auto">
              <li className="nav-item m-1">
                Welcome {currentSigInUser.userName}
              </li>
              <li>
                <button
                  className="btn btn-outline-light"
                  onClick={handleLogOut}
                >
                  LogOut
                </button>
              </li>
            </ul>
          ) : (
            <ul className="navbar-nav ms-auto">
              <li className="nav-item m-1">
                <NavLink className="nav-link" to="/login">
                  Sign In
                </NavLink>
              </li>
              <li className="nav-item m-1">
                <NavLink className="nav-link" to="/register">
                  Sign Up
                </NavLink>
              </li>
            </ul>
          )}
        </div>
      </div>
    </nav>
  );
};

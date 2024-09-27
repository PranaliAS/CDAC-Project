import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUserCircle } from "@fortawesome/free-solid-svg-icons";
import User from "../../models/userEntity";
import authenticationService from "../../services/authentication.service";
import { sessionHistory } from "../../commons/history";

export const LoginPage = () => {
  // Login User And SignUp User
  const [userName, setUserName] = useState("");
  const [password, setPassword] = useState("");

  // Displays
  const [displayWarning, setDisplayWarning] = useState(false);
  const [displaySuccess, setDisplaySuccess] = useState(false);
  const [displayErrorWarning, setDisplayErrorWarning] = useState("");

  // LOading
  const [loading, setLoading] = useState(false);

  const currentSigInUser = authenticationService.getCurrentUser();

  useEffect(() => {
    if (currentSigInUser?.userId) {
      // navigate
      sessionHistory.push("/profile");
    }
  }, [currentSigInUser]);

  const userSignIn = (e: any) => {
    e.preventDefault();
    if (!currentSigInUser?.userId && userName !== "" && password !== "") {
      const userRegistered: User = new User(userName, password, "", "", "");
      authenticationService
        .signIn(userRegistered)
        .then((response) => {
          alert("User Successfully Login.");
          sessionHistory.push("/profile");
          window.location.reload();
          localStorage.setItem("currentUser", JSON.stringify(response.data));
          console.log(response.data);
        })
        .catch((error) => {
          console.log(error);
          if (error?.response?.status === 409) {
            setDisplayErrorWarning(error.message);
            setDisplayErrorWarning("Username or Password is not valid");
          } else {
            setDisplayErrorWarning("Unexpected error occurred");
          }
          setLoading(false);
        });

      /* if (submitLoginResponse) {
        localStorage.setItem("currentUser", JSON.stringify(response.data));
      }*/

      setUserName("");
      setPassword("");
      setLoading(true);
      setDisplayWarning(false);
      setDisplaySuccess(true);
    } else {
      setLoading(false);
      setDisplayWarning(true);
      setDisplaySuccess(false);
    }
  };

  return (
    <div className="container mt-5">
      <div className="card ms-auto me-auto p-3 shadow-lg custom-card">
        <h3 className="card-text text-center">SignIn Form</h3>
        <FontAwesomeIcon
          className="ms-auto me-auto user-icon"
          icon={faUserCircle}
        />
        {displaySuccess && (
          <div className="alert alert-success">User Successfully Login.</div>
        )}

        {displayErrorWarning && (
          <div className="alert alert-danger">{displayErrorWarning}</div>
        )}

        <form method="POST">
          <div className="form-group">
            <label htmlFor="userName">User Name</label>
            <input
              type="text"
              name="userName"
              className="form-control"
              value={userName}
              onChange={(e) => setUserName(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input
              type="password"
              name="password"
              className="form-control"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          <button
            className="btn btn-info w-100 mt-3"
            disabled={loading}
            type="button"
            onClick={(e) => userSignIn(e)}
          >
            Sign In
          </button>
        </form>
        <Link
          to="/register"
          className="btn btn-link"
          style={{ color: "darkgray" }}
        >
          Create New Account!
        </Link>
      </div>
    </div>
  );
};

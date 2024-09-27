import { useState, useEffect } from "react";
import { Redirect } from "react-router-dom";
import Role from "../../models/role";
import UserService from "../../services/user.service";
import authenticationService from "../../services/authentication.service";

export const Profile = () => {
  const [errorMessage, setErrorMessage] = useState("");

  const currentSigInUser = authenticationService.getCurrentUser();

  useEffect(() => {}, []);

  const changeUserRole = () => {
    const newRole =
      currentSigInUser.role === Role.ADMIN ? Role.USER : Role.ADMIN;

    const fetchUserName = currentSigInUser.userName;

    UserService.changeUserRole(newRole, fetchUserName)
      .then(() => {
        authenticationService.clearCurrentUser();
        <Redirect to="/login" />;
      })
      .catch((error) => {
        setErrorMessage("Unexpected Error Occurred!");
        console.log(error);
      });
  };

  return (
    <div className="container">
      <div className="pt-5">
        {errorMessage && (
          <div className="alert alert-danger">{errorMessage}</div>
        )}
        <div className="card">
          <div className="card-header">
            <div className="row">
              <div className="col-6">
                <h3>User Details</h3>
              </div>
              <div className="col-6 text-end">
                Current Role is : <strong>{currentSigInUser?.role}</strong>
                <button
                  className="btn btn-primary"
                  onClick={() => changeUserRole()}
                >
                  Change Role
                </button>
              </div>
            </div>
          </div>
          <div className="card-body">
            <table className="table table-striped">
              <thead>
                <tr>
                  <th scope="col">#</th>
                  <th scope="col">User Name</th>
                  <th scope="col">Full Name</th>
                  <th scope="col">Email Address</th>
                  <th scope="col">Contact Number</th>
                  <th scope="col">Role</th>
                </tr>
              </thead>
              <tbody>
                <tr key={currentSigInUser?.userId}>
                  <th scope="row">{currentSigInUser?.userId + 1}</th>
                  <td>{currentSigInUser?.userName}</td>
                  <td>{currentSigInUser?.fullName}</td>
                  <td>{currentSigInUser?.emailAddress}</td>
                  <td>{currentSigInUser?.contactNumber}</td>
                  <td>{currentSigInUser?.role}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};

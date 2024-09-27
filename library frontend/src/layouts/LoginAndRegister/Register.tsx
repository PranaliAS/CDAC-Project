// import { useState, useEffect } from "react";
// import { Link, Route } from "react-router-dom";
// import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
// import { faUserCircle } from "@fortawesome/free-solid-svg-icons";
// import User from "../../models/userEntity";
// import { AUTHENTICATION_BASE_URL } from "../../commons/constants";
// import authenticationService from "../../services/authentication.service";

// export const RegisterPage = () => {
//   // Login User And SignUp User
//   const [userName, setUserName] = useState("");
//   const [password, setPassword] = useState("");
//   const [fullName, setFullName] = useState("");
//   const [emailAddress, setEmailAddress] = useState("");
//   const [contactNumber, setContactNumber] = useState("");

//   // Displays
//   const [displayWarning, setDisplayWarning] = useState(false);
//   const [displaySuccess, setDisplaySuccess] = useState(false);
//   const [displayErrorWarning, setDisplayErrorWarning] = useState("");

//   // LOading
//   const [loading, setLoading] = useState(false);

//   const currentSigInUser = authenticationService.getCurrentUser();

//   useEffect(() => {
//     if (currentSigInUser?.userId) {
//       // navigate
//       <Route path="/profile" />;
//     }
//   }, [currentSigInUser]);

//   async function userSignUp(e: any) {
//     e.preventDefault();
//     const signUpUrl: string = AUTHENTICATION_BASE_URL + "/signUp";
//     if (
//       currentSigInUser?.userId &&
//       userName !== "" &&
//       password !== "" &&
//       fullName !== "" &&
//       emailAddress !== "" &&
//       contactNumber !== ""
//     ) {
//       const userToBeRegister: User = new User(
//         userName,
//         password,
//         fullName,
//         emailAddress,
//         contactNumber
//       );
//       const signUpUserRequestOptions = {
//         method: "POST",
//         headers: {
//           "Content-Type": "application/json",
//         },
//         body: JSON.stringify(userToBeRegister),
//       };

//       const submitSignUpUserResponse = await fetch(
//         signUpUrl,
//         signUpUserRequestOptions
//       );

//       if (!submitSignUpUserResponse.ok) {
//         throw new Error("Something went wrong!");
//       }

//       setUserName("");
//       setPassword("");
//       setFullName("");
//       setEmailAddress("");
//       setContactNumber("");
//       setLoading(true);
//       setDisplayWarning(false);
//       setDisplaySuccess(true);
//       alert("User Successfully Registered.");
//       <Route path="/login" />;
//     } else {
//       setLoading(false);
//       setDisplayWarning(true);
//       setDisplaySuccess(false);
//     }
//   }

//   return (
//     <div className="container mt-5">
//       <div className="card ms-auto me-auto p-3 shadow-lg custom-card">
//         <h3 className="card-text text-center">SignUp Form</h3>
//         <FontAwesomeIcon
//           className="ms-auto me-auto user-icon"
//           icon={faUserCircle}
//         />
//         {displaySuccess && (
//           <div className="alert alert-danger">
//             User Successfully Registered.
//           </div>
//         )}
//         {displayWarning && (
//           <div className="alert alert-danger">All Fields must be Filled.</div>
//         )}

//         <form onSubmit={(e) => userSignUp(e)} noValidate>
//           <div className="form-group">
//             <label htmlFor="userName">User Name</label>
//             <input
//               type="text"
//               name="userName"
//               className="form-control"
//               value={userName}
//               onChange={(e) => setUserName(e.target.value)}
//               required
//             />
//             <div className="invalid-feedback">User Name is required.</div>
//           </div>
//           <div className="form-group">
//             <label htmlFor="password">Password</label>
//             <input
//               type="password"
//               name="password"
//               className="form-control"
//               value={password}
//               onChange={(e) => setPassword(e.target.value)}
//               required
//             />
//             <div className="invalid-feedback">Password is required.</div>
//           </div>
//           <div className="form-group">
//             <label htmlFor="fullName">Full Name</label>
//             <input
//               type="text"
//               name="fullName"
//               className="form-control"
//               value={fullName}
//               onChange={(e) => setFullName(e.target.value)}
//               required
//             />
//             <div className="invalid-feedback">Full Name is required.</div>
//           </div>
//           <div className="form-group">
//             <label htmlFor="emailAddress">Email Address</label>
//             <input
//               type="email"
//               name="emailAddress"
//               className="form-control"
//               value={emailAddress}
//               onChange={(e) => setEmailAddress(e.target.value)}
//               required
//             />
//             <div className="invalid-feedback">Email Address is required.</div>
//           </div>
//           <div className="form-group">
//             <label htmlFor="contactNumber">Contact Number</label>
//             <input
//               type="number"
//               name="contactNumber"
//               className="form-control"
//               value={contactNumber}
//               onChange={(e) => setContactNumber(e.target.value)}
//               required
//             />
//             <div className="invalid-feedback">Contact Number is required.</div>
//           </div>
//           <button
//             className="btn btn-info w-100 mt-3"
//             disabled={loading}
//             onClick={userSignUp}
//           >
//             Sign Up
//           </button>
//         </form>
//         <Link
//           to="/login"
//           className="btn btn-link"
//           style={{ color: "darkgray" }}
//         >
//           Already had an Account!
//         </Link>
//       </div>
//     </div>
//   );
// };

import React, { useState, useEffect } from "react";
import { Link, useHistory } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUserCircle } from "@fortawesome/free-solid-svg-icons";
import User from "../../models/userEntity";
import { AUTHENTICATION_BASE_URL } from "../../commons/constants";
import authenticationService from "../../services/authentication.service";

export const RegisterPage = () => {
  const [userName, setUserName] = useState("");
  const [password, setPassword] = useState("");
  const [fullName, setFullName] = useState("");
  const [emailAddress, setEmailAddress] = useState("");
  const [contactNumber, setContactNumber] = useState("");

  const [displayWarning, setDisplayWarning] = useState(false);
  const [displaySuccess, setDisplaySuccess] = useState(false);
  const [displayErrorWarning, setDisplayErrorWarning] = useState("");
  const [loading, setLoading] = useState(false);

  const history = useHistory();
  const currentSigInUser = authenticationService.getCurrentUser();

  useEffect(() => {
    if (currentSigInUser?.userId) {
      history.push("/profile");
    }
  }, [currentSigInUser, history]);

  const userSignUp = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (
      userName &&
      password &&
      fullName &&
      emailAddress &&
      contactNumber
    ) {
      setLoading(true);
      const signUpUrl = `${AUTHENTICATION_BASE_URL}/signUp`;
      const userToBeRegister = new User(
        userName,
        password,
        fullName,
        emailAddress,
        contactNumber
      );

      const signUpUserRequestOptions = {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(userToBeRegister),
      };

      try {
        const response = await fetch(signUpUrl, signUpUserRequestOptions);
        if (response.ok) {
          setUserName("");
          setPassword("");
          setFullName("");
          setEmailAddress("");
          setContactNumber("");
          setDisplayWarning(false);
          setDisplaySuccess(true);
          alert("User Successfully Registered.");
          history.push("/login");
        } else {
          setDisplayErrorWarning("Something went wrong!");
        }
      } catch (error) {
        setDisplayErrorWarning("Error occurred during registration.");
      } finally {
        setLoading(false);
      }
    } else {
      setLoading(false);
      setDisplayWarning(true);
      setDisplaySuccess(false);
    }
  };

  return (
    <div className="container mt-5">
      <div className="card ms-auto me-auto p-3 shadow-lg custom-card">
        <h3 className="card-text text-center">SignUp Form</h3>
        <FontAwesomeIcon
          className="ms-auto me-auto user-icon"
          icon={faUserCircle}
        />
        {displaySuccess && (
          <div className="alert alert-success">
            User Successfully Registered.
          </div>
        )}
        {displayWarning && (
          <div className="alert alert-danger">All Fields must be filled.</div>
        )}
        {displayErrorWarning && (
          <div className="alert alert-danger">{displayErrorWarning}</div>
        )}

        <form onSubmit={userSignUp} noValidate>
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
            <div className="invalid-feedback">User Name is required.</div>
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
            <div className="invalid-feedback">Password is required.</div>
          </div>
          <div className="form-group">
            <label htmlFor="fullName">Full Name</label>
            <input
              type="text"
              name="fullName"
              className="form-control"
              value={fullName}
              onChange={(e) => setFullName(e.target.value)}
              required
            />
            <div className="invalid-feedback">Full Name is required.</div>
          </div>
          <div className="form-group">
            <label htmlFor="emailAddress">Email Address</label>
            <input
              type="email"
              name="emailAddress"
              className="form-control"
              value={emailAddress}
              onChange={(e) => setEmailAddress(e.target.value)}
              required
            />
            <div className="invalid-feedback">Email Address is required.</div>
          </div>
          <div className="form-group">
            <label htmlFor="contactNumber">Contact Number</label>
            <input
              type="text"
              name="contactNumber"
              className="form-control"
              value={contactNumber}
              onChange={(e) => setContactNumber(e.target.value)}
              required
            />
            <div className="invalid-feedback">Contact Number is required.</div>
          </div>
          <button
            type="submit"
            className="btn btn-info w-100 mt-3"
            disabled={loading}
          >
            Sign Up
          </button>
        </form>
        <Link
          to="/login"
          className="btn btn-link"
          style={{ color: "darkgray" }}
        >
          Already have an account?
        </Link>
      </div>
    </div>
  );
};

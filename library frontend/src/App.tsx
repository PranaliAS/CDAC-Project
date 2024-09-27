import React from "react";
import "./App.css";
import { Switch, Route, Redirect } from "react-router-dom";
import { Navbar } from "./layouts/NavbarAndFooter/Navbar";
import { Footer } from "./layouts/NavbarAndFooter/Footer";
import { BookCheckoutPage } from "./layouts/BookCheckoutPage/BookCheckoutPage";
import { HomePage } from "./layouts/HomePage/HomePage";
import { LoginPage } from "./layouts/LoginAndRegister/Login";
import { Profile } from "./layouts/LoginAndRegister/Profile";
import { RegisterPage } from "./layouts/LoginAndRegister/Register";
import { ManageLibraryPage } from "./layouts/ManageLibraryPage/ManageLibraryPage";
import { MessagesPage } from "./layouts/MessagesPage/MessagesPage";
import { SearchBooksPage } from "./layouts/SearchBooksPage/SearchBooksPage";
import { ShelfPage } from "./layouts/ShelfPage/ShelfPage";
import { ReviewListPage } from "./layouts/BookCheckoutPage/ReviewListPage/ReviewListPage";

export const App = () => {
  return (
    <div className="d-flex flex-column min-vh-100">
      <Navbar />
      <div className="flex-grow-1">
        <Switch>
          <Route path="/" exact>
            <Redirect to="/home" />
          </Route>
          <Route path="/home">
            <HomePage />
          </Route>
          <Route path="/search">
            <SearchBooksPage />
          </Route>
          <Route path="/reviewList/:bookId">
            <ReviewListPage />
          </Route>
          <Route path="/checkout/:bookId">
            <BookCheckoutPage />
          </Route>
          <Route path="/login">
            <LoginPage />
          </Route>
          <Route path="/register">
            <RegisterPage />
          </Route>
          <Route path="/profile">
            <Profile />
          </Route>
          <Route path="/shelf">
            <ShelfPage />
            {/*<AuthGuard roles={[Role.ADMIN]}></AuthGuard>*/}
          </Route>
          <Route path="/messages">
            <MessagesPage />
            {/*<AuthGuard roles={[Role.ADMIN]}></AuthGuard>*/}
          </Route>
          <Route path="/admin">
            <ManageLibraryPage />
            {/*<AuthGuard roles={[Role.ADMIN]}></AuthGuard>*/}
          </Route>
        </Switch>
      </div>
      <Footer />
    </div>
  );
};

import { useEffect, useState } from "react";
import History from "../../../models/historyEntity";
import { SpinnerLoading } from "../../Utils/SpinnerLoading";
import { Link } from "react-router-dom";
import { Pagination } from "../../Utils/Pagination";
import { AuthHeader } from "../../../services/base.service";
import { HISTORY_BASE_URL } from "../../../commons/constants";
import authenticationService from "../../../services/authentication.service";

export const HistoryPage = () => {
  const currentSignInUser = authenticationService.getCurrentUser();
  const [isLoadingHistory, setIsLoadingHistory] = useState(true);
  const [httpError, setHttpError] = useState(null);

  // Histories
  const [histories, setHistories] = useState<History[]>([]);

  // pagination
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    const fetchUserCheckOutHistory = async () => {
      if (currentSignInUser && currentSignInUser?.userId) {
        const checkOutAndReturnedHistoryUrl =
          HISTORY_BASE_URL +
          `/service/all/byUser/pageWise?page=${currentPage - 1}&&size=5`;
        const requestCheckOutAndReturnedHistoryOptions = {
          method: "GET",
          headers: AuthHeader(),
        };
        const checkOutAndReturnedHistoryResponse = await fetch(
          checkOutAndReturnedHistoryUrl,
          requestCheckOutAndReturnedHistoryOptions
        );

        if (!checkOutAndReturnedHistoryResponse.ok) {
          throw new Error("Something went wrong!");
        }
        const checkOutAndReturnedHistoryResponseJson =
          await checkOutAndReturnedHistoryResponse.json();

        setHistories(checkOutAndReturnedHistoryResponseJson.content);
        setTotalPages(checkOutAndReturnedHistoryResponseJson.totalPages);
      }
      setIsLoadingHistory(false);
    };
    fetchUserCheckOutHistory().catch((error: any) => {
      setIsLoadingHistory(false);
      setHttpError(error.message);
    });
  }, [currentPage, currentSignInUser]);

  if (isLoadingHistory) {
    return <SpinnerLoading />;
  }

  if (httpError) {
    return (
      <div className="container m-5">
        <p>{httpError}</p>
      </div>
    );
  }

  const paginate = (pageNumber: number) => setCurrentPage(pageNumber);

  return (
    <div className="mt-2">
      {histories.length > 0 ? (
        <>
          <h5>Recent History:</h5>

          {histories.map((history) => (
            <div key={history.historyId}>
              <div className="card mt-3 shadow p-3 mb-3 bg-body rounded">
                <div className="row g-0">
                  <div className="col-md-2">
                    <div className="d-none d-lg-block">
                      {history.bookImage ? (
                        <img
                          src={history.bookImage}
                          width="123"
                          height="196"
                          alt="Book"
                        />
                      ) : (
                        <img
                          src={require("./../../../Images/BooksImages/book-luv2code-1000.png")}
                          width="123"
                          height="196"
                          alt="Default"
                        />
                      )}
                    </div>
                    <div className="d-lg-none d-flex justify-content-center align-items-center">
                      {history.bookImage ? (
                        <img
                          src={history.bookImage}
                          width="123"
                          height="196"
                          alt="Book"
                        />
                      ) : (
                        <img
                          src={require("./../../../Images/BooksImages/book-luv2code-1000.png")}
                          width="123"
                          height="196"
                          alt="Default"
                        />
                      )}
                    </div>
                  </div>
                  <div className="col">
                    <div className="card-body">
                      <h5 className="card-title"> {history.bookAuthor} </h5>
                      <h4>{history.bookTitle}</h4>
                      <p className="card-text">{history.bookDescription}</p>
                      <hr />
                      <p className="card-text">
                        {" "}
                        Checked out on: {history.checkoutDate}
                      </p>
                      <p className="card-text">
                        {" "}
                        Returned on: {history.returnDate}
                      </p>
                    </div>
                  </div>
                </div>
              </div>
              <hr />
            </div>
          ))}
        </>
      ) : (
        <>
          <h3 className="mt-3">Currently no history: </h3>
          <Link className="btn btn-primary" to={"search"}>
            Search for new book
          </Link>
        </>
      )}
      {totalPages > 1 && (
        <Pagination
          currentPage={currentPage}
          totalPages={totalPages}
          paginate={paginate}
        />
      )}
    </div>
  );
};

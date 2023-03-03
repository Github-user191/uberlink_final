import React from 'react'
import { Navigate, useLocation, useNavigate } from "react-router";

import AuthenticationService from "./services/AuthenticationService";


function SecuredRoute({children})  {
    // check if a user is authentication (Whether a token was created)
    const auth = AuthenticationService.getCurrentUserJwt();

    // if authenticated, allow access to secured route, else redirect to signup form
    return auth ? children : <Navigate to="/register" />;
}
export default SecuredRoute


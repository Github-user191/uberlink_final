import React, { useEffect, useState } from "react";
import { Link, useNavigate} from "react-router-dom";
import AuthenticationService from "./services/AuthenticationService";
import UserService from "./services/UserService";
import Hamburger from "hamburger-react";

const Navbar = () => {

  const [auth, setAuth] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const user = AuthenticationService.getCurrentUserJwt();

    if(user) {
      setAuth(true)
    }
    

    

  }, [])


  const logout = () => {
    AuthenticationService.logout();
    navigate('/register');
  }

  return (


    <nav class="navbar navbar-expand-lg navbar-container">
      <div class="container-fluid">
        <Link to="/" className="navbar-brand navbar-logo">
          <h3>Uberlink</h3>

        </Link>
        <button class="navbar-toggler hamburger-menu shadow-none" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
          <Hamburger size={24} direction="right" distance="md" color="#fff" />
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">


        {auth ?  

                      
 



            <div className="text-center">

              <ul className="navbar-nav me-auto mb-2 mb-lg-0" >

                <li className="nav-item shadow-none">
                  <Link to="/my-uberlinks">
                    <button type="button" className="navbar-my-uberlinks-btn">
                      My Uberlinks
                    </button>
                  </Link>

                </li>
                
                <li className="nav-item  shadow-none col">
                  <button type="button" className="navbar-my-uberlinks-btn" onClick={logout}>
                    Logout
                  </button>
                </li>
              </ul>

            </div>



            : 
          <Link to="/register" style={{ textDecoration: "none", margin: "0"}}>
            <p>Register</p>
          </Link>
          }




          <Link to="/contact">
            <button className="contact-btn" type="button" >
              Contact
            </button>
          </Link>



        </div>
      </div>
    </nav>

  )
}

export default Navbar
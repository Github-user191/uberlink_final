import './App.css';
import axios from 'axios';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LandingDetails from './LandingDetails';
import LandingPage from './LandingPage';
import Contact from './Contact';
import Redirect from './Redirect';
import LinkNotFound from './LinkNotFound';
import Login from './Login';
import Register from './Register';
import AuthenticationService from './services/AuthenticationService';
import jwtDecode from 'jwt-decode';
import MyUberlinks from './MyUberlinks';
import SecuredRoute from './SecuredRoute';
import ResetPassword from './ResetPassword';
import EmailConfirmedPage from './EmailConfirmedPage';


axios.interceptors.request.use( config => {
  const user = AuthenticationService.getCurrentUserJwt();

  if(user){
      const token = 'Bearer ' + user.jwt;
      config.headers.Authorization =  token;
  }
  return config;
});


function App() {
  

  const userJwt = AuthenticationService.getCurrentUserJwt().jwt;



  if (userJwt) {
      const decodedToken = jwtDecode(userJwt);
      const currentTime = Date.now() / 1000;

      if (decodedToken.exp < currentTime) {
          AuthenticationService.logout();
          window.location.reload();
          window.location.href = "/register"
      }
  }

  return (
    <Router>

      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/link/:shortenedLink" element={<Redirect />} />
        <Route path="/contact" element={<Contact />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
	    <Route path="/confirm-email/:token" element={<EmailConfirmedPage/>}/>
        <Route path="/reset-password" element={<ResetPassword />} />
        <Route path="/my-uberlinks" element={<SecuredRoute><MyUberlinks/></SecuredRoute> } />
        <Route path="/*" element={<LinkNotFound/>} />
      </Routes>

    </Router>


  );
}

export default App;

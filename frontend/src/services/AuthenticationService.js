import axios from 'axios'
import jwtDecode from 'jwt-decode';
import { webStore } from '../utils/WebStore';


const BASE_URL = `/api/auth`;
// axios.defaults.baseURL = `https://localhost:8080`


class AuthenticationService {

    authenticate = async (user) => {
        return await axios.post(BASE_URL + '/authenticate', user)
            .then(res => {
                const userData = res.data;

                if(userData.jwt) {
                    localStorage.setItem("user", JSON.stringify(userData));
                }
                return userData;
            }).catch(err => {
                throw err;
            })
    }

    register = async (user) => {
        return await axios.post(BASE_URL + "/register", user)
    }

    logout = () => {
        localStorage.removeItem("user");
    }


    getCurrentUserJwt  = () => {
        let jwt = JSON.parse(localStorage.getItem("user"))

        if(jwt === null || jwt === undefined) {
            return false;
        } else {
            return jwt;
        }
    }

    async getUserInfo() {
        return await axios.get("/api/user/info");
    }

    getCurrentUserSubject = () => {
        let token = this.getCurrentUserJwt().jwt;
        let decodedToken
        if(token) {
            decodedToken = jwtDecode(token)
      
        } else {
            return token
        }

        return decodedToken.sub
    }

    confirmEmail = async (token) => {
        return await axios.get(BASE_URL + `/confirm?token=${token}`);
    }
    

    resendConfirmationEmail = async (token) => {
        return await axios.get(BASE_URL + `/resendConfirmationToken?token=${token}`);
    }

    confirmPasswordReset = async (token) => {
        return await axios.get(BASE_URL + `/confirmPasswordReset?token=${token}`)
    }

    
    
    forgotPassword = async (emailAddress) => {
        return await axios.post(BASE_URL + "/forgotPassword", emailAddress)
    }

    resetPassword = async (newPassword, token) => {
        return await axios.post(BASE_URL + `/resetPassword?token=${token}`, newPassword)
    }



    changePassword = async (user) => {
        return await axios.post(BASE_URL + `/changePassword`, user)
    }
}


export default new AuthenticationService();

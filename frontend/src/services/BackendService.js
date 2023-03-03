import axios from "axios";
import AuthenticationService from "./AuthenticationService";

axios.interceptors.request.use( config => {
    const user = AuthenticationService.getCurrentUserJwt();
    if(user){
        const token = 'Bearer ' + user.jwt;
        config.headers.Authorization =  token;
    }
    return config;
});


class BackendService {



}

export default new BackendService();

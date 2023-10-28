import axios from "axios";
import { webStore } from "../utils/WebStore";

const BASE_URL = `${webStore.APP_URL}/api/user`;

class UserService {

    async getUserInfo() {
        return await axios.get(BASE_URL + "/info");
    }

    async getUserInfoByEmailAddress(emailAddress) {
        return await axios.get(BASE_URL + "/info/" + emailAddress);
    }

    async updateUserInfo(user, avatar) {
        const formData = new FormData();
        const json = JSON.stringify(user);
        const blob = new Blob([json], {
            type: "application/json"
        });

        formData.append("avatar", avatar)

        // appending Post information to formData
        formData.append("user", blob);
        
        return await axios.post(BASE_URL + "/update", formData, {headers:{'Content-Type':'multipart/form-data'}});
    }
}

export default new UserService();

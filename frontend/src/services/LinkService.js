import axios from "axios";
import { webStore } from "../utils/WebStore";
// import.meta.env.VITE_REACT_APP_URI

const BASE_URL = `${webStore.APP_URL}/api/link/`;

class LinkService {

    async generateShortenedLink(originalLink) {
        return await axios.post(BASE_URL, {originalLink});
    } 

    async getAllUserLinks(params) {
        return await axios.get(BASE_URL + "all", {params})
    }

    async getAllActiveUserLinks() {
        return await axios.get(BASE_URL + "all/active")
    }

    async expireShortenedLink(shortenedLink) {
        return await axios.post(BASE_URL + shortenedLink)
    }
    
    async getOriginalLink(shortenedLink) {
        return await axios.get(BASE_URL + shortenedLink);
    } 

    async deleteLink(shortenedLink) {
        return await axios.delete(BASE_URL + shortenedLink)
    }


}

export default new LinkService();

import axios from "axios";

const BASE_URL = "/api/contact/send"


class ContactFormService {

    async sendContactForm(contactForm) {
        return await axios.post(BASE_URL, contactForm);
    } 


}

export default new ContactFormService();

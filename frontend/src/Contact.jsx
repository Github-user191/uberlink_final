import React, { useState } from 'react'
import Navbar from './Navbar'
import contactUsIllustration from './assets/contact-us-illustration.svg'
import Footer from './Footer'
import ContactFormService from './services/ContactFormService'
import { SyncLoader } from 'react-spinners'
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const Contact = () => {

    const [contactForm, setContactForm] = useState({
        fullName: "",
        emailAddress: "",
        message: ""
    })

    const [errors, setErrors] = useState({
        fullNameError: "",
        emailAddressError: "",
        messageError: ""
    })

    const clearInput = () => {
        setContactForm({
            fullName: "",
            emailAddress: "",
            message: ""
        })
    }

    const handleSubmit = (event) => {
        event.preventDefault();

        setLoading(true)

        ContactFormService.sendContactForm(contactForm)
            .then(res => {

                setLoading(false)
                setErrors({
                    fullNameError: "",
                    emailAddressError: "",
                    messageError: ""
                })

                toast.success(res.data)
                clearInput();
            }).catch(err => {
                const {fullName, emailAddress, message} = err.response.data;
                setErrors({
                    fullNameError: fullName,
                    emailAddressError: emailAddress,
                    messageError: message
                })
                setLoading(false)
            })
          
     
    }

    const handleChange = (event) => {
        const {name, value} = event.target;
        setContactForm(prev => ({
            ...prev, [name]: value
        }))
    }

    const [loading, setLoading] = useState(false);


    return (
        <div className="contact-page">
            <Navbar />
            <div className="content">

                <ToastContainer />
                <div className="left-section">
                    <div className="main-section">
                        <h1>Contact Us</h1>
                        <p>Lorem ipsum dolor sit amet consectetur. Ut consequat blandit mi cursus eu mauris eu est. Sit nec condimentum dignissim enim tempus.</p>
                    </div>

                    <form className="contact-form" onSubmit={handleSubmit}>

                        <div className="form-group">


                            <div className="mb-1">
                              
                                {contactForm.fullName ? "" : <label id="form-error-status">{errors.fullNameError}</label>}
                                <label>Full name</label>
                                <input className={errors.fullNameError ? "form-control form-control-error" : "form-control"} type="text" placeholder="Full name" name="fullName" value={contactForm.fullName} onChange={handleChange}/>
                            </div>


                            <div className="mb1">
                                {errors.emailAddressError ? <label id="form-error-status">{errors.emailAddressError}</label> : ""}
                                <label>Email address</label>
                                <input className={errors.emailAddressError ? "form-control form-control-error" : "form-control"} type="text" placeholder="Email address" name="emailAddress" value={contactForm.emailAddress} onChange={handleChange}/>
                            </div>

                     
                            <div className="mb-3">
                                {contactForm.message.length > 20 ? "" : <label id="form-error-status">{errors.messageError}</label>}
                                <label>Message</label>
                                <textarea className={errors.messageError ? "form-control contact-message-control form-control-error" : "form-control contact-message-control"} type="text" placeholder="Your message" name="message" value={contactForm.message} onChange={handleChange}/>
                            </div>


                            <button className="send-message-btn">{loading ? <SyncLoader color='white' size={6}/> : "Send message"}</button>
                        </div>
                    </form>


                </div>



                <div className="right-section">
                    <img src={contactUsIllustration} alt="contact us image" />
                </div>
            </div>

       
            <Footer/>
        </div>
    )
}

export default Contact
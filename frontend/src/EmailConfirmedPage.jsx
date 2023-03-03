import React from "react";

import axios from 'axios';
import {useParams } from 'react-router';
import { Link } from 'react-router-dom';
import AuthenticationService from './services/AuthenticationService';
import { Bounce, Slide, toast, ToastContainer } from "react-toastify";
import { useState } from "react";
import Navbar from "./Navbar";
import { useEffect } from "react";
import emailConfirmedIllustration from "./assets/email-confirmed-illustration.svg";
import { SyncLoader } from "react-spinners";
import Footer from "./Footer";

const EmailConfirmedPage = () => {

    const [loading, setLoading] = useState(false);

    const [accountVerifiedStatus, setAccountVerifiedStatus] = useState("");
    const [accountVerified, setAccountVerified] = useState(false);


    const {token} = useParams();

    const resendConfirmationEmail = () => {
        setLoading(true)
        AuthenticationService.resendConfirmationEmail(token)
            .then(res=> {
                toast.success(res.data.message)
                setLoading(false)
            }).catch(err => {
                setLoading(false)
                toast.warn(err.response.data.invalidLinkError);
            })
    }
    useEffect(() => {
        AuthenticationService.confirmEmail(token)
        .then((res) => {
            setAccountVerified(true)
            setAccountVerifiedStatus(res.data.message)
        })
        .catch((err) => {
            setAccountVerified(false);
            err.response.data.message ? setAccountVerifiedStatus(err.response.data.message) : setAccountVerifiedStatus(err.response.data.invalidLinkError);
 
        });
    
    }, []);


  return (
    <div className="email-confirmed-page">
    <Navbar />

    <div className="content">

        
    
        <div className="main-section">
            <h1>{accountVerifiedStatus}</h1>
        </div>


        <img src={emailConfirmedIllustration} alt="email confirmed image"/>
        <div className="btn-container">
            <Link to="/login">
                <button className="proceed-login-btn">Proceed to login</button>
            </Link>
            <br/>

            <button className="resend-confirmation-btn" onClick={resendConfirmationEmail}>{loading ? <SyncLoader color='white' size={6} /> : 'Resend email'}</button>
        </div>
 


    </div>
    <ToastContainer />
    <Footer  />
</div>
  );
};

export default EmailConfirmedPage;


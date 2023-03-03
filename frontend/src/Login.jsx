import React, {useState} from 'react'
import {Link, useNavigate} from 'react-router-dom'
import ForgotPasswordModal from './ForgotPasswordModal'
import Navbar from './Navbar'
import {Bounce, Slide, toast, ToastContainer} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import AuthenticationService from './services/AuthenticationService'
import {IoMdEye} from 'react-icons/io'
import {IoMdEyeOff} from 'react-icons/io'
import SyncLoader from 'react-spinners/SyncLoader'

const Login = () => {

    const [user, setUser] = useState({
        emailAddress: "",
        password: ""
    });

    const [loading, setLoading] = useState(false);


    const [errors, setErrors] = useState({
        emailAddressError: '',
        passwordError: '',
        authenticationError: ''
    })

    const handleChange = (event) => {
        const {name, value} = event.target;
        setUser((prev) => ({
            ...prev,
            [name]: value,
        }));
    }

    const clearErrors = () => {
        setErrors({
            emailAddressError: '',
            passwordError: '',
            authenticationError: ''
        })
    }

    const navigate = useNavigate();


    const clearInput = () => {
        setUser({
            emailAddress: "",
            password: ""
        });
    }

    const handleSubmit = (event) => {
        event.preventDefault();
        setLoading(true);

        AuthenticationService.authenticate(user)
            .then(res => {
                clearErrors();
                navigate("/");
            }).catch(err => {
            const {accountNotVerified, emailAddress, password, invalidToken, authenticationError} = err.response.data;
            setErrors({
                emailAddressError: emailAddress,
                passwordError: password,
                accountNotVerifiedError: accountNotVerified,
                forgotEmailAddressError: null,
                authenticationError: authenticationError
            })
            setLoading(false)
            if (accountNotVerified) {
                toast.error(accountNotVerified)
            }


        }).finally(() => setLoading(false))
    }

    const [showPassword, setShowPassword] = useState(false);

    const togglePassword = () => {
        setShowPassword(!showPassword);
    };


    return (
        <div className="login-page">
            <Navbar/>
            <ToastContainer/>
            <div className="content">
                <form className="login-form" onSubmit={handleSubmit}>

                    <h2 className="mb-4">Login</h2>

                    <div className="form-group">


                        <div className="mb-1">
                            {errors.authenticationError ?
                                <label id="form-error-status">{errors.authenticationError}</label> : ''}
                            {errors.emailAddressError ?
                                <label id="form-error-status">{errors.emailAddressError}</label> : ''}
                            <label>Email address</label>
                            <input
                                className={errors.emailAddressError ? "form-control form-control-error" : "form-control"}
                                type="text" placeholder="Email address"
                                name="emailAddress" value={user.emailAddress} onChange={handleChange}/>

                        </div>


                        <div className="mb-3">

                            <Link to='' onClick={togglePassword}>
                                {showPassword ? <IoMdEyeOff id="password-icon"/> : <IoMdEye id="password-icon"/>}
                            </Link>
                            {errors.passwordError ? <label id="form-error-status">{errors.passwordError}</label> : ''}
                            <label>Password</label>
                            <input className={errors.passwordError ? "form-control form-control-error" : "form-control"}
                                   type={showPassword ? "text" : "password"} placeholder="Password" name="password"
                                   value={user.password} onChange={handleChange}/>


                        </div>


                        <button className="login-btn">{loading ?
                            <SyncLoader color='white' size={6}/> : 'Log in'}</button>

                        <ForgotPasswordModal className="forgot-password mt-3 mb-4"/>

                        <Link to="/register" style={{textDecoration: "none", margin: "0"}}>
                            <p className="register-prompt">Don’t have an account? <span id="login-register-prompt">Create one</span>
                            </p>
                        </Link>

                    </div>
                </form>
            </div>
        </div>
    )
}

export default Login
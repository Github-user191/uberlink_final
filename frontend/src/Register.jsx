import React, { useState } from 'react'
import { Link } from 'react-router-dom'
import Navbar from './Navbar'
import { IoMdEye } from 'react-icons/io'
import { IoMdEyeOff } from 'react-icons/io'
import AuthenticationService from './services/AuthenticationService'
import { toast, ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import { SyncLoader } from 'react-spinners'

const Register = () => {

    const [user, setUser] = useState({
        fullName: "",
        emailAddress: "",
        password: "",
        confirmPassword: ""
      });
    
      const [loading, setLoading] = useState(false);
    
    
      const [errors, setErrors] = useState({
        fullNameError: '',
        emailAddressError: '',
        passwordError: '',
        confirmPasswordError: '',
        emailAlreadyExistsError: ''
      })

      const handleChange = (event) => {
        const { name, value } = event.target;
        setUser((prev) => ({
          ...prev,
          [name]: value,
        }));
      }

      const clearErrors = () => {
        setErrors({
          fullNameError: '',
          emailAddressError: '',
          passwordError: '',
          confirmPasswordError: '',
          emailAlreadyExistsError: ''
        })
      }
    
    
      const clearInput = () => {
        setUser({
          fullName: "",
          emailAddress: "",
          password: "",
          confirmPassword: ""
        });
      }
    
      const handleSubmit = (event) => {
        event.preventDefault();
    
        setLoading(true);
        AuthenticationService.register(user)
          .then(res => {
     
    
    
            toast.success(res.data.message);
    
            clearInput();
            clearErrors();
            setLoading(false)
          }).catch(err => {
            setLoading(false);
            setErrors({
              fullNameError: err.response.data.fullName,
              emailAddressError: err.response.data.emailAddress,
              mobileNumberError: err.response.data.mobileNumber,
              passwordError: err.response.data.password,
              confirmPasswordError: err.response.data.confirmPassword,
              emailAlreadyExistsError: err.response.data.emailAlreadyExists
            })
          })

      }
    
      const [showPassword, setShowPassword] = useState(false);
      const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    
      const togglePassword = () => {
        setShowPassword(!showPassword);
      };
    
      const toggleConfirmPassword = () => {
        setShowConfirmPassword(!showConfirmPassword);
      };


    return (
        <div className="register-page">
        <Navbar />

        <ToastContainer />
            <div className="content">
                <form className="register-form" onSubmit={handleSubmit}>

                    <h2 className="mb-4">Register with <span id="login-register-prompt">Uberlink</span></h2>

                    <div className="form-group">


                        <div className="mb-1">
                            {errors.fullNameError ? <label id="form-error-status">{errors.fullNameError}</label> : ''}
                            <label>Full name</label>
                       
                            <input className={errors.fullNameError ? "form-control form-control-error" : "form-control"} type="text" placeholder="Full name" 
                              name="fullName" value={user.fullName} onChange={handleChange} />
                        </div>


                        <div className="mb-1">
                            {errors.emailAddressError ? <label id="form-error-status">{errors.emailAddressError}</label> : ''}
                            <label>Email address</label>
                            <input className={errors.emailAddressError ? "form-control form-control-error" : "form-control"} type="text" placeholder="Email address" name="emailAddress" value={user.emailAddress} onChange={handleChange} />
                        </div>

                        <div className="mb-1">

                        <Link to='' onClick={togglePassword}>
                            {showPassword ? <IoMdEyeOff id="password-icon" /> : <IoMdEye id="password-icon" />}
                        </Link>
                            {errors.passwordError ? <label id="form-error-status">{errors.passwordError}</label> : ''}
                            <label>Password</label>
                            <input className={errors.passwordError ? "form-control form-control-error" : "form-control"} type={showPassword ? "text" : "password"} placeholder="Password" name="password" value={user.password} 
                                onChange={handleChange} />
                        </div>

                        <div className="mb-3">

                        <Link to='' onClick={toggleConfirmPassword}>
                            {showConfirmPassword ? <IoMdEyeOff id="password-icon" /> : <IoMdEye id="password-icon" />}
                        </Link>
                            {errors.confirmPasswordError ? <label id="form-error-status">{errors.confirmPasswordError}</label> : ''}
                            <label>Confirm password</label>
                            <input className={errors.confirmPasswordError ? "form-control form-control-error" : "form-control"} type={showConfirmPassword ? "text" : "password"} placeholder="Confirm password" name="confirmPassword" value={user.confirmPassword} onChange={handleChange} />
                        </div>

                        <button className="register-btn">{loading ? <SyncLoader color='white' size={6}/> : 'Register'}</button>

                        <Link to="/login" style={{ textDecoration: "none" }}>
                            <p className="login-prompt">Already have an account? <span id="login-register-prompt">Log in</span></p>
                        </Link>
                      
                    </div>
                </form>
            </div>
        </div>
    )
}

export default Register

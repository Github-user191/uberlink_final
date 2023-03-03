import React, {useEffect, useState} from 'react'
import { Link } from 'react-router-dom'
import SyncLoader from 'react-spinners/SyncLoader'
import { Bounce, Slide, toast, ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import AuthenticationService from './services/AuthenticationService'

const ForgotPasswordModal = () => {



    const [user, setUser] = useState({
        forgotPasswordEmail: ''
    })

    const [errors, setErrors] = useState({
        forgotPasswordEmailError: ''
    })

    const [loading, setLoading] = useState(false);

    const notifySuccess = (message) => {
        toast.success(message);
    }


    const handleChange = (event) => {
        const { name, value } = event.target;

        setUser(prev => ({
            ...prev, [name]: value
        }))
    }

    const handleForgotPasswordSubmit = (event) => {
        event.preventDefault();
       
        setLoading(true);

        AuthenticationService.forgotPassword(user)
            .then(res => {
                setLoading(false)
                setUser({ forgotPasswordEmail: '' })
                toast.success(res.data.message)
            }).catch(err => {
                setLoading(false)
                setErrors({
                    forgotPasswordEmailError: "Account does not exist"
                })

            })


    }


    return (
        <div className="forgot-password-page">
          <ToastContainer style={{marginTop: "5rem"}}/>
            <Link to="/forgot-password" data-bs-toggle="modal" data-bs-target="#staticBackdrop" style={{ textDecoration: "none" }}>
                <p className="forgot-password-prompt">Forgot your password?</p>
            </Link>


            <div class="modal fade forgot-password-modal" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true" >
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header" style={{ border: "none" }}>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            <h5 class="modal-title" id="staticBackdropLabel">Forgot password?</h5>
                            <hr />
                            <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Perferendis, dolorem? Sequi, perspiciatis!</p>

                        </div>
                        <div class="modal-body mb-2">

                            <label>Email address</label>

                            <input className="form-control shadow-none" type="text" placeholder="Email address" name="forgotPasswordEmail" value={user.forgotPasswordEmail}
                                onChange={handleChange}/>
                        </div>
                        <div class="modal-footer" style={{ border: "none" }}>

                            
                            <button type="button" class="btn send-forgot-password-btn" onClick={handleForgotPasswordSubmit}>
                                {loading ? <SyncLoader color='white' size={6}/> : 'Send email'}
                            </button>
                        </div>
                    </div>
                </div>
      
            </div>

        </div>
    )
}

export default ForgotPasswordModal
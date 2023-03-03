import React from 'react'
import { useState } from 'react'
import { SyncLoader } from 'react-spinners'
import Navbar from './Navbar'
import resetPasswordIllustration from './assets/reset-password-illustration.svg'
import Footer from './Footer'

const ResetPassword = () => {

    const [resetPasswordForm, setResetPasswordForm] = useState({
        previousPassword: "",
        newPassword: ""
    })

    const [errors, setErrors] = useState({
        previousPasswordError: "",
        newPasswordError: ""
    })

    const handleChange = (event) => {
        const {value, name} = event.target;

        setResetPasswordForm(prev => ({
            ...prev, [name]:value
        }))
    }

    const [loading, setLoading] = useState(false)

    const handleSubmit = () => {

    }

    return (
        <div className="reset-password-page">
            <Navbar/>
            <div className="content">

            <div className="left-section">
                <div className="main-section">
                    <h1>Reset password</h1>
                    <p>Change or reset your current password </p>
                </div>

                <form className="reset-password-form" onSubmit={handleSubmit}>

                    <div className="form-group">


                        <div className="mb-1">

                            {resetPasswordForm.previousPassword ? "" : <label id="form-error-status">{errors.previousPasswordError}</label>}
                            <label>Previous password</label>
                            <input className={errors.previousPasswordError ? "form-control form-control-error" : "form-control"} type="text" placeholder="Previous password" 
                                name="fullName" value={resetPasswordForm.previousPassword} onChange={handleChange} />
                        </div>


                        <div className="mb-3">
                            {resetPasswordForm.newPassword ? "" : <label id="form-error-status">{errors.newPasswordError}</label>}    
                            <label>New password</label>
                            <input className={errors.newPasswordError ? "form-control form-control-error" : "form-control"} type="text" placeholder="New password"
                                name="emailAddress" value={resetPasswordForm.newPassword} onChange={handleChange} />
                        </div>

                        <button className="reset-password-btn">{loading ? <SyncLoader color='white' size={6} /> : "Reset password"}</button>
                    </div>
                </form>
                </div>

                <div className="right-section">
                    <img src={resetPasswordIllustration} alt="reset password image" />
                </div>

            </div>
            <Footer />
        </div>
    )
}

export default ResetPassword
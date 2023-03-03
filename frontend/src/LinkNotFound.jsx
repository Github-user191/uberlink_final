import React from 'react'
import Navbar from './Navbar'
import LinkNotFoundIllustration from './assets/link-not-found-illustration.svg'
import { Link } from 'react-router-dom'

const LinkNotFound = () => {
  return (
    <div className="link-not-found-page">
        <Navbar />
        <div className="content">
            <div className="main-section">
                <h1>Whoops! This Uberlink does not exist</h1>
                <p>Looks like this Uberlink does not exist, please try creating a new one using your full-length link.</p>
            </div>

            <div className="image-container">
                <img src={LinkNotFoundIllustration} alt="Link not found illustration"/>
            </div>
      
            <Link to={"/"} >
              <button className="back-to-home-btn">Back to home</button>
            </Link>
         
        </div>

    </div>
  )
}

export default LinkNotFound
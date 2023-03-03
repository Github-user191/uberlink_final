import React from 'react'
import webIcon from './assets/web-icon.svg'
import graphIcon from './assets/graph-icon.svg'
import clockIcon from './assets/clock-icon.svg'

const LandingDetails = () => {
  return (
    <div className="landing-details-page">
        <div className="content">
            <div class="row">
                <div className="details-container col-lg">
                    <img src={webIcon} alt="web icon" className="icon"/>
                    <h3>Convenient link sharing</h3>
                    <p>Uberlinks are created to be shared seamlessly on platforms without worrying about character limits.</p>
                </div>

                <div className="details-container col-lg ">
                    <img src={clockIcon} alt="clock icon" className="icon"/>
                    <h3>Expire your links</h3>
                    <p>Modify the expiration time of your Uberlinks as required. </p>
                </div>

                <div className="details-container col-lg">
                    <img src={graphIcon} alt="graph-icon" className="icon"/>
                    <h3>View all previous links</h3>
                    <p>All of your previous uberlinks are available to use for 30 days after they are created.</p>
                </div>
            </div>
        </div>
    </div>
  )
}

export default LandingDetails

import React, { useState } from 'react'
import Footer from './Footer'
import LandingDetails from './LandingDetails'
import Navbar from './Navbar'
import LinkService from './services/LinkService'
import { FaCopy } from 'react-icons/fa';
import CopyToClipboard from 'react-copy-to-clipboard'
import SyncLoader from 'react-spinners/SyncLoader'
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useEffect } from 'react'
import AuthenticationService from './services/AuthenticationService'
import UserService from './services/UserService'


const LandingPage = () => {

  const [isCopied, setIsCopied] = useState(false);

  const [loading, setLoading] = useState(false);

  const [errors, setErrors] = useState({
    linkError: ""
  })

  const [link, setLink] = useState({
    originalLink: "",
    shortenedLink: ""
  });

  const [status, setStatus] = useState(false);

  useEffect(() => {
    const user = AuthenticationService.getCurrentUserJwt();

    if (user) {
      setAuth(true)
    }




  }, [])

  const [auth, setAuth] = useState(false);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setIsCopied(true);
    setLink(prev => ({
      ...prev, [name]: value
    }))
  }


  const handleSubmit = (event) => {
    event.preventDefault();
    setLoading(true)

    if(!auth) {
      toast.error("Please make sure you are logged in before perfoming this action")
    }

    LinkService.generateShortenedLink(link.originalLink)
    .then(res => {

      setLink({
        originalLink: link.originalLink,
        shortenedLink: res.data.shortenedLink
      })
      if (res.status === 201) {
        setStatus(true)
        setLoading(true)

        setErrors({})
      }
    }).catch(err => {
      setStatus(false)
      setLoading(false)

      if (err.response.data.invalidLinkError) {
        setErrors({
          linkError: err.response.data.invalidLinkError
        })
      }

      if (err.response.data.originalLink) {
        setErrors({
          linkError: err.response.data.originalLink
        })
      }
    }).finally(() => setLoading(false))
  }

  const onCopy = () => {
    toast.success("Link was copied to clipboard")
  }

  const reset = () => {

    setLink({ originalLink: "" })
    setStatus(false)
  }

  return (
    <div className="landing-page">
      <Navbar />

      <div className="content">
        <ToastContainer />
        <div className="main-section">
        <div>

        </div>
          <h1>Create short and easy to share links</h1>
	  <p>Uberlink creates short versions of links you provide to avoid character limit restrictions and make your life easier.</p>
        </div>

        <div className="search-section">

          <form className="search-form" onSubmit={handleSubmit}>

            <div className="form-group">
              <div className="mb-1">

                {status === true && (
                  <CopyToClipboard text={`${process.env.REACT_APP_BASE_URL + "/link/" + link.shortenedLink}`} onCopy={onCopy}>
                    <div className="icon">
                      <FaCopy style={{ fill: "var(--dark-gray)", position: "absolute" }} />
                    </div>
                  </CopyToClipboard>

                )}



                {status && (
                  <p className="try-again-prompt" onClick={reset}>Try again with a new link</p>
                )}

                {errors.linkError ? <label id="form-error-status">{errors.linkError}</label> : ""}
                <input className={errors.linkError ? "form-control form-control-error" : "form-control"} type="text" placeholder="Enter your Link" name="originalLink"
                  value={status === true ? `${process.env.REACT_APP_BASE_URL + "/link/" + link.shortenedLink}` : link.originalLink} onChange={handleChange} />

                <button className="search-btn" disabled={status ? true : false}>{loading ? <SyncLoader color='white' size={6} /> : "Get Short link"}</button>



                {status === true && (
                  <p className="original-link-result"><span style={{ fontWeight: "600", fontSize: "var(--mobile-input)" }}>Original link:</span> {link.originalLink}</p>
                )}

              </div>
            </div>
          </form>
        </div>


        <LandingDetails />


      </div>

      <Footer />
    </div>
  )
}

export default LandingPage

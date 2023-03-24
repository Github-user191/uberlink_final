import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router'
import { Link } from 'react-router-dom';
import LinkNotFound from './LinkNotFound';
import LinkService from './services/LinkService';

const Redirect = () => {

    const { shortenedLink } = useParams();

    const [error, setError] = useState({
        linkNotExistError: ""
    })

    useEffect(() => {

        getOriginalLink();

    }, [])



    const getOriginalLink = () => {

        LinkService.getOriginalLink(shortenedLink)
            .then(res => {
                window.location.href = res.data;
            }).catch(err => {
                setError({
                    linkNotExistError: "Error"
                })
    
            })


    }

  return (
    <div>

    {error.linkNotExistError && <LinkNotFound/> }

    </div>
  )
}

export default Redirect
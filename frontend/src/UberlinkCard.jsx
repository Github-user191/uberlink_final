import React from 'react'
import { useEffect } from 'react';
import CopyToClipboard from 'react-copy-to-clipboard'
import { FaCopy } from 'react-icons/fa'
import LinkService from './services/LinkService';
import dateFormat from 'dateformat';
import { MdDelete } from 'react-icons/md';
import { SlOptionsVertical } from 'react-icons/sl'


const UberlinkCard = ({ lightColor, darkColor, shortenedLink, originalLink, createdAt, deleteUberlink, expireUberlink}) => {

  return (
    <div className="uberlink-card" style={{ backgroundColor: lightColor }}>

      <div className="header">
        <h3>{shortenedLink}</h3>


        <CopyToClipboard text={shortenedLink}>
          <div className="copy-icon" style={{marginTop: "-.2rem"}}>
            <FaCopy size={15} style={{ fill: "#fff" }} />
          </div>
        </CopyToClipboard>


        <div class="dropdown" style={{marginTop: "-.2rem"}}>
          <div className="options-icon" type="button" id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">
            <SlOptionsVertical size={15} fill="#fff" />
          </div>

          <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
            <li onClick={deleteUberlink}><a class="dropdown-item " href="#">Delete uberlink</a></li>
            <li onClick={expireUberlink}><a class="dropdown-item " href="#">Expire uberlink</a></li>
            {/* <li><a class="dropdown-item" href="#">Something else here</a></li> */}
          </ul>
        </div>

      </div>

  



      <p className="original-link">{originalLink}</p>

      <div className="metadata">
        <div className="date-section" style={{ backgroundColor: darkColor }}>
          <p>
            Created {dateFormat(createdAt, "dd mmmm yyyy")}
          </p>
        </div>

        {/* <div className="delete-section" style={{ backgroundColor: darkColor }} onClick={deleteUberlink}>
         <p style={{float:"right"}}> Delete this item <MdDelete size={20} fill={"#fff"} /></p> 
        </div> */}
      </div>
    </div>

  )
}

export default UberlinkCard
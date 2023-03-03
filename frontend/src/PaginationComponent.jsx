
import React, {useState} from 'react'
import { makeStyles } from '@mui/styles';
import {BiRightArrow, BiLeftArrow} from 'react-icons/bi'
import Pagination from "@material-ui/lab/Pagination";


const PaginationComponent = (props) => {

  const useStyles = makeStyles((theme) =>({
    root: {
        '& .Mui-selected': {
          backgroundColor: 'var(--primary-purple)',
          color: "#fff",
          fontFamily: "Poppins",

          height: "42px",
          width: "42px",
          fontSize: "var(--mobile-input)",
          border: "none",
   
          borderRadius: "var(--default-border-radius)"
         },
         '& .Mui-selected:hover': {
          backgroundColor: 'var(--hover-purple)',
           border: "none"
         },
         '& ul > li:not(:first-child):not(:last-child) > button:not(.Mui-selected)': {
          height: "38px",
          width: "38px",
          fontFamily: "Poppins",
 
          border: "1px solid var(--input-gray)",
          fontSize: "var(--mobile-input)",
     
          borderRadius: "var(--default-border-radius)"
         },
         '& ul > li:not(:first-child):not(:last-child) > button:not(.Mui-selected):hover': {
 
          border: "none",
          borderRadius: "var(--default-border-radius)"
         },
         
    }
  }));


  const classes = useStyles();

    return (
        <div className="pagination-container">
          <div className="content">

            <div className="pagination mt-2">


              <Pagination
                count={props.count} // amount of pages there are
                page={props.page} // current page we are on
                siblingCount={1}
                boundaryCount={1} 
                variant="text"
                shape="rounded"
                components={{ previous: '<BiLeftArrow/>', next:<BiRightArrow/> }}
                onChange={props.onPageChange}
                className={classes.root}
                classes={{selected:classes.selected}}
              />
            </div>
      
          </div>
        </div>
        
    )
}

export default PaginationComponent

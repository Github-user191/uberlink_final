import React from 'react'
import SkeletonElement from './SkeletonElement'


const SkeletonUberlinkCard = () => {
    return (
      <div className="wrapper">
        <div className="uberlink-card-skeleton">
          <SkeletonElement type="shortened-link-text" />
          <SkeletonElement type="original-link-text" />
          <SkeletonElement type="metadata-text" />
         
        </div>
      </div>
    );
  };

export default SkeletonUberlinkCard

import React from "react";
import {useEffect} from "react";
import {useState} from "react";
import {ToastContainer, toast} from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Footer from "./Footer";
import Navbar from "./Navbar";
import PaginationComponent from "./PaginationComponent";
import LinkService from "./services/LinkService";
import UberlinkCard from "./UberlinkCard";
import noUberlinkIllustration from "./assets/no-uberlinks-illustration.svg";
import SkeletonUberlinkCard from "./skeleton/SkeletonUberlinkCard";
import {Link} from "react-router-dom";

const MyUberlinks = () => {
    const [uberlinks, setUberlinks] = useState([]);

    const [loading, setLoading] = useState(false);
    const [totalLinkCount, setTotalLinkCount] = useState(0);
    const [pageCount, setPageCount] = useState(1);
    const [linksPageSize, setLinksPageSize] = useState(10); // total elements to display per page (DEFAULT)
    const [linksPageNo, setLinksPageNo] = useState(1); // start at the first page (index of 0 from backend)

    const getAllUserLinks = (requestParams) => {
        setLoading(true);

        LinkService.getAllUserLinks(requestParams)
            .then((res) => {
                const {totalItems, totalPages, links} = res.data;
                setUberlinks(links);
                setTotalLinkCount(totalItems);
                setPageCount(totalPages);
                setLoading(false);
            })
            .catch((err) => {
                setLoading(false);
            });
    };

    const deleteUberlink = (shortenedLink) => {
        LinkService.deleteLink(shortenedLink)
            .then((res) => {
                //setUberlinks(uberlinks.filter(link => link.shortenedLink !== shortenedLink))
                toast.success("Uberlink was deleted successfully!");
                setUberlinks(res.data.links);
                setLinksPageNo(0)
            })
            .catch((err) => {
                if (err.response.data.invalidLinkError) {
                    toast.success("Uberlink was deleted successfully!");
                    setUberlinks([]);
                }
            });
    };

    const expireUberlink = (shortenedLink) => {

        LinkService.expireShortenedLink(shortenedLink)
            .then((res) => {

                toast.success("Uberlink was expired successfully!");
                window.location.reload()
            })
            .catch((err) => {
                toast.error("This Uberlink is already expired!");

            });
    };

    const getRequestParams = (linksPageNo, linksPageSize, sortBy, sortDir) => {
        let params = {};

        if (linksPageNo) params["pageNo"] = linksPageNo - 1;
        if (linksPageSize) params["pageSize"] = linksPageSize;
        if (sortBy) params["sortBy"] = sortBy;
        if (sortDir) params["sortDir"] = sortDir;

        return params;
    };

    const onPageChange = (event, value) => {
        setLinksPageNo(value);
    };

    useEffect(() => {
        getAllUserLinks(getRequestParams(linksPageNo, linksPageSize, "id", "desc"));
    }, [linksPageNo, linksPageSize]);

    return (
        <div className="my-uberlinks-page">
            <Navbar/>
            <ToastContainer/>
            <div className="content">
                <div className="main-section">
                    <h1>Your previous Uberlinks</h1>
                    <p>Here you can find the uberlinks you created previously</p>
                </div>

                <div className="uberlink-card-section">
                    {loading ? (
                        <>
                            <SkeletonUberlinkCard/>
                            <SkeletonUberlinkCard/>
                            <SkeletonUberlinkCard/>
                            <SkeletonUberlinkCard/>
                            <SkeletonUberlinkCard/>
                        </>
                    ) : (
                        uberlinks &&
                        (uberlinks.length > 0 ? (
                            uberlinks.map((link, idx) => {
                                return (
                                    <UberlinkCard
                                        shortenedLink={process.env.REACT_APP_BASE_URL + "/link/" + link.shortenedLink}
                                        originalLink={link.originalLink}
                                        lightColor={
                                            link.active
                                                ? "var(--success-green-light)"
                                                : "var(--failure-red-light)"
                                        }
                                        darkColor={
                                            link.active
                                                ? "var(--success-green-dark)"
                                                : "var(--failure-red-dark)"
                                        }
                                        createdAt={link.createdAt}
                                        deleteUberlink={() => deleteUberlink(link.shortenedLink)}
                                        expireUberlink={() => expireUberlink(link.shortenedLink)}
                                    />
                                );
                            })
                        ) : (
                            <div className="no-uberlink-section">
                                <img src={noUberlinkIllustration} alt="vector image"/>
                                <h1>You do not have any Uberlinks yet</h1>
                                <p className="mb-3">Create your first uberlink to get started</p>
                                <Link to="/">
                                    <button>Create</button>
                                </Link>

                            </div>
                        ))
                    )}

                    {uberlinks.length > 0 && (
                        <PaginationComponent
                            className="my-3 pagination"
                            count={pageCount}
                            page={linksPageNo}
                            siblingCount={1}
                            boundaryCount={1}
                            variant="outlined"
                            pageSize={totalLinkCount}
                            onPageChange={onPageChange}
                            shape="rounded"
                        />
                    )}
                </div>
            </div>
            <Footer/>
        </div>
    );
};

export default MyUberlinks;

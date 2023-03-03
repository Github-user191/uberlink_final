package com.springboot.app.uberlink.repository;


import com.springboot.app.uberlink.model.Link;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    //@Query(value = "select original_link from Link where shortened_link = ?1", nativeQuery = true)
    Link findByShortenedLinkAndLinkCreatorEmailAddress(String shortenedLink, String emailAddress);
    Link findByOriginalLinkAndLinkCreatorEmailAddress(String originalLink, String emailAddress);
    Page<Link> findAllByLinkCreatorEmailAddress(String emailAddress, Pageable pageable);
    Page<Link> findAllByLinkCreatorEmailAddressAndActiveIsTrue(String emailAddress, Pageable pageable);

    Boolean existsByOriginalLinkAndLinkCreatorEmailAddressEquals(String originalLink, String emailAddress);

}

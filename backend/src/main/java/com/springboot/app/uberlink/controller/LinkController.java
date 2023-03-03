package com.springboot.app.uberlink.controller;


import com.springboot.app.uberlink.model.Link;
import com.springboot.app.uberlink.service.LinkService;
import com.springboot.app.uberlink.service.impl.ErrorValidationServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/link/")
@CrossOrigin(origins = "*")
public class LinkController {

    private final String MAX_PAGE_COUNT = "10";

    private final LinkService linkService;
    private final ErrorValidationServiceImpl errorValidationService;

    public LinkController(LinkService linkService, ErrorValidationServiceImpl errorValidationService) {
        this.linkService = linkService;
        this.errorValidationService = errorValidationService;
    }


    @GetMapping("/{shortenedLink}")
    public String getOriginalLink(@PathVariable String shortenedLink, Principal principal) {
        return linkService.getOriginalLink(shortenedLink, principal.getName()).getOriginalLink();
    }

    @PostMapping
    public ResponseEntity<?> createShortenedLink(@Valid @RequestBody Link link, BindingResult result, Principal principal) {
        ResponseEntity<?> errorMap = errorValidationService.validationService(result);
        if(errorMap != null) return errorMap;
        return new ResponseEntity<>(linkService.createShortenedLink(link.getOriginalLink(), principal.getName()), HttpStatus.CREATED);
    }

    @PostMapping("/{shortenedLink}")
    public ResponseEntity<?> expireShortenedLink(@PathVariable String shortenedLink, Principal principal) {

        Boolean result = linkService.expireShortenedLink(principal.getName(),shortenedLink);


        if(result) {
            return new ResponseEntity<>("Uberlink was expired successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("This Uberlink is already expired", HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUserLinks(Principal principal,
         @RequestParam(defaultValue = "0", required = false) int pageNo,
         @RequestParam(defaultValue = MAX_PAGE_COUNT, required = false) int pageSize,
         @RequestParam(defaultValue = "id", required = false) String sortBy,
         @RequestParam(defaultValue = "desc", required = false) String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable linkPageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Link> page = linkService.getAllUserLinksByEmailAddress(principal.getName(), linkPageable);

        return new ResponseEntity<>(createMapForPageContents(page), HttpStatus.OK);
    }


    @DeleteMapping("/{shortenedLink}")
    public ResponseEntity<?> deleteByShortenedLink(Principal principal, @PathVariable String shortenedLink,
       @RequestParam(defaultValue = "0", required = false) int pageNo,
       @RequestParam(defaultValue = MAX_PAGE_COUNT, required = false) int pageSize,
       @RequestParam(defaultValue = "id", required = false) String sortBy,
       @RequestParam(defaultValue = "desc", required = false) String sortDir) {
        Boolean result = linkService.deleteByShortenedLink(shortenedLink, principal.getName());

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable linkPageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Link> page = linkService.getAllUserLinksByEmailAddress(principal.getName(), linkPageable);
        if(result) {
            return new ResponseEntity<>(createMapForPageContents(page), HttpStatus.OK);
        }
        return new ResponseEntity<>("Sorry, this Uberlink does not exist.", HttpStatus.BAD_REQUEST);


    }

    @GetMapping("/all/active")
    public ResponseEntity<?> getAllUserActiveLinks(Principal principal,
        @RequestParam(defaultValue = "0", required = false) int pageNo,
        @RequestParam(defaultValue = MAX_PAGE_COUNT, required = false) int pageSize) {

        Pageable linkPageable = PageRequest.of(pageNo, pageSize);

        Page<Link> page = linkService.getAllUserActiveLinksByEmailAddress(principal.getName(), linkPageable);

        return new ResponseEntity<>(createMapForPageContents(page), HttpStatus.OK);
    }


    private Map<Object ,Object> createMapForPageContents(Page page) {
        Map<Object, Object> pageContentMapResponse = new HashMap<>();
        pageContentMapResponse.put("links", page.getContent());
        pageContentMapResponse.put("currentPage", page.getNumber());
        pageContentMapResponse.put("totalItems", page.getTotalElements());
        pageContentMapResponse.put("totalPages", page.getTotalPages());
        return pageContentMapResponse;
    }

}

package com.springboot.app.uberlink.dto;

import com.springboot.app.uberlink.model.Link;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAndLinkDTO {
    private Long id;
    private String fullName;
    private String emailAddress;
    private LocalDateTime dateJoined;

    private List<Link> links;

}

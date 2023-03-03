package com.springboot.app.uberlink.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @NotBlank(message = "Required")
    private String fullName;

    @NotBlank(message = "Required")
    @Email(message = "Email is invalid")
    @Column(unique = true)
    private String emailAddress;

    @JsonFormat(pattern = "yyyy-mm-dd")
    @Column(nullable = false, updatable = false)
    private Date dateJoined;

    @Column(nullable = false)
    private boolean emailVerified = false;

    @NotBlank(message = "Required")
    @Column(length = 60)
    private String password;

    // Won't be saved to DB, transient variable
    @Transient
    private String confirmPassword;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Link> links = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role> roles = new HashSet<>();


    public User(long id, String fullName, String emailAddress, String password) {
        this.id = id;
        this.fullName = fullName;
        this.emailAddress = emailAddress;
        this.password = password;
        this.dateJoined = new Date();
    }

    @PrePersist
    protected void onCreate() {
        this.dateJoined = new Date();
    }


}

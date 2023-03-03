package com.springboot.app.uberlink.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Link implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shortenedLink;

    @NotBlank(message = "Required")
//    @Column(nullable = false, unique = true, length = 256)
    private String originalLink;


    @Column(name ="created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name="expired_at", updatable = false)
    private LocalDateTime expiredAt;

    @Column(name="active", nullable = false)
    private boolean active = true;

    private String linkCreatorEmailAddress;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    public Link(Long id, String shortenedLink, String originalLink, boolean active, String linkCreatorEmailAddress, User user) {
        this.id = id;
        this.shortenedLink = shortenedLink;
        this.originalLink = originalLink;
        this.active = active;
        this.linkCreatorEmailAddress = linkCreatorEmailAddress;
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.expiredAt = calculateExpiryDate(createdAt);
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.expiredAt = calculateExpiryDate(createdAt);

    }

    private LocalDateTime calculateExpiryDate(LocalDateTime createdAt) {
        return createdAt.plusMonths(1);
    }

    @JsonIgnore
    public boolean isLinkExpired() {
        LocalDateTime current = LocalDateTime.now();
        return current.isAfter(getExpiredAt());
    }


    @Override
    public String toString() {
        return "Link{" +
                "id=" + id +
                ", shortenedLink='" + shortenedLink + '\'' +
                ", originalLink='" + originalLink + '\'' +
                ", createdAt=" + createdAt +
                ", expiredAt=" + expiredAt +
                ", active=" + active +
                ", linkCreatorEmailAddress='" + linkCreatorEmailAddress + '\'' +
                ", user=" + user +
                '}';
    }
}

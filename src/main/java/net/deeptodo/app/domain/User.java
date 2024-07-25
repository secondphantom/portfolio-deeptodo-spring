package net.deeptodo.app.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickName;
    private String email;
    private String billingKey;
    private String oauthServerId;
    @Enumerated(EnumType.STRING)
    private OauthServerType oauthServerType;
    private String oauthToken;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Subscription subscription;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Project> projects = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();

    @Builder
    public User(Long id,
                String nickName,
                String email,
                String billingKey,
                String oauthServerId,
                OauthServerType oauthServerType,
                String oauthToken,
                Subscription subscription) {
        this.id = id;
        this.nickName = nickName;
        this.email = email;
        this.billingKey = billingKey;
        this.oauthServerId = oauthServerId;
        this.oauthServerType = oauthServerType;
        this.oauthToken = oauthToken;
        this.subscription = subscription;
    }

    static public User createNewUser(
            final String nickName,
            final String email,
            final String oauthServerId,
            final OauthServerType oauthServerType
    ) {

        return User.builder()
                .nickName(nickName)
                .email(email)
                .oauthServerId(oauthServerId)
                .oauthServerType(oauthServerType)
                .build();
    }

}

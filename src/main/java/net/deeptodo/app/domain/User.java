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
    private String avatarUrl;
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
                String avatarUrl,
                String billingKey,
                String oauthServerId,
                OauthServerType oauthServerType,
                String oauthToken,
                Subscription subscription) {
        this.id = id;
        this.nickName = nickName;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.billingKey = billingKey;
        this.oauthServerId = oauthServerId;
        this.oauthServerType = oauthServerType;
        this.oauthToken = oauthToken;
        this.subscription = subscription;
    }

    static public User createNewUser(
            final String nickName,
            final String email,
            final String avatarUrl,
            final String oauthServerId,
            final OauthServerType oauthServerType,
            final SubscriptionPlan plan
    ) {
        User user = User.builder()
                .nickName(nickName)
                .email(email)
                .avatarUrl(avatarUrl)
                .billingKey("")
                .oauthServerId(oauthServerId)
                .oauthServerType(oauthServerType)
                .oauthToken("")
                .build();

        Subscription subscription = Subscription.builder()
                .user(user)
                .plan(plan)
                .status(SubscriptionStatus.ACTIVE)
                .startDate(LocalDateTime.now())
                .expiredDate(LocalDateTime.now().plusDays(plan.getDurationDays()))
                .build();

        user.subscription = subscription;
        return user;
    }

    public boolean canCreateProject(Long currentProjectCount) {
        Integer maxProjectCount = subscription.getPlan().getMaxProjectCount();
        if (currentProjectCount >= maxProjectCount) {
            return false;
        }
        return true;
    }

    public void updateProfile(String nickName, String avatarUrl) {
        System.out.println("nickName = " + nickName);
        System.out.println("avatarUrl = " + avatarUrl);
        this.nickName = nickName;
        this.avatarUrl = avatarUrl;
    }
}

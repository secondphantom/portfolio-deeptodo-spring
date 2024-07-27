package net.deeptodo.app.testutils;

import net.deeptodo.app.domain.*;

import java.util.ArrayList;
import java.util.HashMap;


public class EntityUtils {
    public static User createDefaultUser(User user) {
        return User.builder()
                .id(user.getId() != null ? user.getId() : null)
                .nickName(user.getNickName() != null ? user.getNickName() : "nickName")
                .email(user.getEmail() != null ? user.getEmail() : "email")
                .billingKey(user.getBillingKey() != null ? user.getBillingKey() : "billingKey")
                .oauthServerId(user.getOauthServerId() != null ? user.getOauthServerId() : "oauthServerId")
                .oauthServerType(user.getOauthServerType() != null ? user.getOauthServerType() : OauthServerType.GOOGLE)
                .oauthToken(user.getOauthToken() != null ? user.getOauthToken() : "oauthToken")
                .subscription(user.getSubscription())
                .build();
    }

    public static User createNewUser(SubscriptionPlan plan) {
        return User.createNewUser(
                "nickName",
                "email@email.com",
                "oauthServerId",
                OauthServerType.GOOGLE,
                plan
        );
    }

    public static SubscriptionPlan createDefaultPlan(SubscriptionPlan plan, Long id) {
        return SubscriptionPlan.builder()
                .id(id)
                .type(plan.getType() != null ? plan.getType() : PlanType.FREE)
                .description(plan.getDescription() != null ? plan.getDescription() : "")
                .price(plan.getPrice() != null ? plan.getPrice() : 0.0)
                .durationDays(plan.getDurationDays() != null ? plan.getDurationDays() : 300)
                .maxProjectCount(plan.getMaxProjectCount() != null ? plan.getMaxProjectCount() : 300)
                .maxTodoCount(plan.getMaxTodoCount() != null ? plan.getMaxTodoCount() : 300)
                .enabled(plan.isEnabled())
                .build();
    }


    public static Project createDefaultProject(Project project, User user) {
        return Project.builder()
                .id(project.getId() != null ? project.getId() : null)
                .user(user)
                .title(project.getTitle() != null ? project.getTitle() : "")
                .version(project.getVersion() != null ? project.getVersion() : 0)
                .root(project.getRoot() != null ? project.getRoot() : new ArrayList<>())
                .boards(project.getBoards() != null ? project.getBoards() : new HashMap<>())
                .todos(project.getTodos() != null ? project.getTodos() : new HashMap<>())
                .enabled(project.isEnabled())
                .build();
    }
}

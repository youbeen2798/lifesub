package com.unicorn.lifesub.mysub.infra.gateway.entity;

import com.unicorn.lifesub.mysub.biz.domain.MySubscription;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "my_subscriptions")
@Getter
@NoArgsConstructor
public class MySubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private SubscriptionEntity subscription;

    @Builder
    public MySubscriptionEntity(Long id, String userId, SubscriptionEntity subscription) {
        this.id = id;
        this.userId = userId;
        this.subscription = subscription;
    }

    public MySubscription toDomain() {
        return MySubscription.builder()
                .id(id)
                .userId(userId)
                .subscription(subscription.toDomain())
                .build();
    }
}

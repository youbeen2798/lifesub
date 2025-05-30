package com.unicorn.lifesub.mysub.infra.gateway.entity;

import com.unicorn.lifesub.common.entity.BaseTimeEntity;
import com.unicorn.lifesub.mysub.biz.domain.Subscription;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subscriptions")
@Getter
@NoArgsConstructor
public class SubscriptionEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String category;
    private int price;
    private int maxSharedUsers;
    private String logoUrl;

    @Builder
    public SubscriptionEntity(Long id, String name, String description, String category,
                            int price, int maxSharedUsers, String logoUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.maxSharedUsers = maxSharedUsers;
        this.logoUrl = logoUrl;
    }

    public Subscription toDomain() {
        return Subscription.builder()
                .id(id)
                .name(name)
                .description(description)
                .category(category)
                .price(price)
                .maxSharedUsers(maxSharedUsers)
                .logoUrl(logoUrl)
                .build();
    }
}

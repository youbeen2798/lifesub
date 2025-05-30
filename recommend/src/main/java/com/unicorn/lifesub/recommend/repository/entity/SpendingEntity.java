package com.unicorn.lifesub.recommend.repository.entity;

import com.unicorn.lifesub.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "spending_history")
@Getter
@NoArgsConstructor
public class SpendingEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String category;
    private Long amount;
    private LocalDate spendingDate;

    @Builder
    public SpendingEntity(Long id, String userId, String category, Long amount, LocalDate spendingDate) {
        this.id = id;
        this.userId = userId;
        this.category = category;
        this.amount = amount;
        this.spendingDate = spendingDate;
    }
}

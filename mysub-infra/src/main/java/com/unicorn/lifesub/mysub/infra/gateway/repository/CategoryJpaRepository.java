package com.unicorn.lifesub.mysub.infra.gateway.repository;

import com.unicorn.lifesub.mysub.infra.gateway.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, String> {
}

package com.unicorn.lifesub.member.repository.jpa;

import com.unicorn.lifesub.member.repository.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, String> {
    Optional<MemberEntity> findByUserId(String userId);
}

// File: lifesub/mysub-infra/src/main/java/com/unicorn/lifesub/mysub/infra/config/DataLoader.java
package com.unicorn.lifesub.mysub.infra.config;

import com.unicorn.lifesub.mysub.infra.gateway.entity.CategoryEntity;
import com.unicorn.lifesub.mysub.infra.gateway.entity.SubscriptionEntity;
import com.unicorn.lifesub.mysub.infra.gateway.repository.CategoryJpaRepository;
import com.unicorn.lifesub.mysub.infra.gateway.repository.SubscriptionJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final CategoryJpaRepository categoryRepository;
    private final SubscriptionJpaRepository subscriptionRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            loadCategories();
        }

        if (subscriptionRepository.count() == 0) {
            loadSubscriptions();
        }
    }

    private void loadCategories() {
        log.info("Loading sample categories...");

        List<CategoryEntity> categories = Arrays.asList(
                CategoryEntity.builder()
                        .categoryId("OTT")
                        .name("OTT/동영상")
                        .build(),
                CategoryEntity.builder()
                        .categoryId("MUSIC")
                        .name("음악")
                        .build(),
                CategoryEntity.builder()
                        .categoryId("FOOD")
                        .name("식품")
                        .build(),
                CategoryEntity.builder()
                        .categoryId("LIFE")
                        .name("생활")
                        .build(),
                CategoryEntity.builder()
                        .categoryId("BEAUTY")
                        .name("뷰티")
                        .build(),
                CategoryEntity.builder()
                        .categoryId("EDU")
                        .name("교육")
                        .build()
        );

        categoryRepository.saveAll(categories);
        log.info("Sample categories loaded");
    }

    private void loadSubscriptions() {
        log.info("Loading sample subscriptions...");

        // 카테고리별 서비스 매핑
        Map<String, List<SubscriptionEntity>> subscriptionsByCategory = Map.of(
                "OTT", Arrays.asList(
                        createSubscription("넷플릭스", "전세계 최대 OTT 서비스", "OTT", 17000, 4, "/images/netflix.png"),
                        createSubscription("티빙", "국내 실시간 방송과 예능/드라마 VOD", "OTT", 13900, 4, "/images/tving.png"),
                        createSubscription("디즈니플러스", "디즈니, 픽사, 마블, 스타워즈 콘텐츠", "OTT", 9900, 4, "/images/disney.png")
                ),
                "MUSIC", Arrays.asList(
                        createSubscription("멜론", "국내 최대 음원 스트리밍", "MUSIC", 10900, 1, "/images/melon.png"),
                        createSubscription("스포티파이", "전세계 음악 스트리밍", "MUSIC", 10900, 6, "/images/spotify.png"),
                        createSubscription("유튜브 뮤직", "유튜브 음원 스트리밍", "MUSIC", 8900, 5, "/images/youtube-music.png")
                ),
                "FOOD", Arrays.asList(
                        createSubscription("쿠팡이츠", "식품 정기배송", "FOOD", 4900, 1, "/images/coupang-eats.png"),
                        createSubscription("마켓컬리", "신선식품 새벽배송", "FOOD", 4900, 1, "/images/kurly.png"),
                        createSubscription("배민", "배달음식 구독", "FOOD", 5900, 1, "/images/baemin.png")
                ),
                "LIFE", Arrays.asList(
                        createSubscription("당근", "중고거래 프리미엄", "LIFE", 3900, 1, "/images/karrot.png"),
                        createSubscription("쿠팡 로켓와우", "무료배송 구독", "LIFE", 4900, 4, "/images/coupang.png"),
                        createSubscription("밀리의 서재", "전자책 구독", "LIFE", 9900, 1, "/images/millie.png")
                ),
                "BEAUTY", Arrays.asList(
                        createSubscription("올리브영", "뷰티 정기구독", "BEAUTY", 15900, 1, "/images/oliveyoung.png"),
                        createSubscription("시코르", "화장품 구독박스", "BEAUTY", 29900, 1, "/images/chicor.png"),
                        createSubscription("롭스", "뷰티 멤버십", "BEAUTY", 19900, 1, "/images/lohbs.png")
                ),
                "EDU", Arrays.asList(
                        createSubscription("클래스101", "취미/교양 클래스", "EDU", 19900, 1, "/images/class101.png"),
                        createSubscription("탈잉", "원데이 클래스", "EDU", 29900, 1, "/images/taling.png"),
                        createSubscription("캐치", "IT 실무 교육", "EDU", 99000, 1, "/images/catch.png")
                )
        );

        // 모든 서비스 저장
        subscriptionsByCategory.values().stream()
                .flatMap(List::stream)
                .forEach(subscriptionRepository::save);

        log.info("Sample subscriptions loaded");
    }

    private SubscriptionEntity createSubscription(String name, String description, String category,
                                                  int price, int maxSharedUsers, String logoUrl) {
        return SubscriptionEntity.builder()
                .name(name)
                .description(description)
                .category(category)
                .price(price)
                .maxSharedUsers(maxSharedUsers)
                .logoUrl(logoUrl)
                .build();
    }
}

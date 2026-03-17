package com.optimax.pms.config;

import com.optimax.pms.tenancy.TenancyLevel;
import com.optimax.pms.tenancy.TenancyNode;
import com.optimax.pms.tenancy.TenancyService;
import com.optimax.pms.user.User;
import com.optimax.pms.user.UserRepository;
import com.optimax.pms.user.UserTenancyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class TenancyBootstrap {

    private static final Logger log = LoggerFactory.getLogger(TenancyBootstrap.class);

    @Bean
    CommandLineRunner createApplicationRoot(TenancyService tenancyService,
                                            UserRepository userRepository,
                                            UserTenancyService userTenancyService) {
        return args -> {
            List<TenancyNode> apps = tenancyService.findByLevel(TenancyLevel.APPLICATION);
            if (!apps.isEmpty()) {
                return;
            }

            TenancyNode app = TenancyNode.builder()
                    .name("Optimax d.o.o.")
                    .code("OPTIMAX")
                    .description("Optimax d.o.o.")
                    .level(TenancyLevel.APPLICATION)
                    .build();
            TenancyNode saved = tenancyService.save(app);
            log.info("Created application root tenancy node with id {}", saved.getId());

            userRepository.findByEmail("admin@optimax.com").ifPresent(admin -> {
                userTenancyService.assignUserToTenancy(
                        admin,
                        saved,
                        TenancyLevel.APPLICATION,
                        null
                );
                log.info("Assigned admin@optimax.com to application tenancy.");
            });
        };
    }
}


package com.example.onetomany.config;

import com.example.onetomany.domain.EventEntity;
import com.example.onetomany.domain.UserEntity;
import com.example.onetomany.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

@Component
@Transactional
@Slf4j
public class InitialUserData implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Override
    public void run(String... args) throws Exception {

        if (!userRepository.findByEmail("test1@domain.com").isPresent()) {

            UserEntity userEntity = userRepository.save(UserEntity.builder()
                    .email("test1@domain.com")
                    .firstName("Richard")
                    .lastName("Thaler")
                    .build());

            userEntity.addEvent(EventEntity.builder()
                    .description("Login successfully")
                    .creationTime(Instant.now())
                    .build());

            userRepository.save(userEntity);

        }

        // Retrieve all events
        Optional.ofNullable(userRepository.findByEmail("test1@domain.com")).ifPresent(
                t -> t.get().getEventEntities().forEach(System.out::println)
        );

        UserEntity userEntity = userRepository.findByEmail("test1@domain.com")
                .orElseThrow(() -> new EntityNotFoundException());

        Set<EventEntity> eventEntities = userEntity.getEventEntities();

        if (!CollectionUtils.isEmpty(eventEntities)) {
            Iterator<EventEntity> iterator = eventEntities.iterator();
            while (iterator.hasNext()) {
                userEntity.removeEvent(iterator.next());
            }
        }
        userRepository.save(userEntity);

    }
}

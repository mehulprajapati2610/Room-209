package com.room209.backend.config;

import com.room209.backend.entity.Room;
import com.room209.backend.entity.User;
import com.room209.backend.repository.RoomRepository;
import com.room209.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(RoomRepository roomRepository, UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Room room = roomRepository.findByRoomNumber("209").orElseGet(() -> {
            logger.info("Initializing Room 209 in PostgreSQL...");
            Room r = new Room("209", "Room 209", LocalTime.of(23, 0), LocalTime.of(7, 0));
            return roomRepository.save(r);
        });

        // Initialize Room 209 roommates if empty
        if (userRepository.findByRoomId(room.getId()).isEmpty()) {
            logger.info("Seeding initial roommates for Room 209...");

            User marcus = new User("marcus@room209.internal", passwordEncoder.encode("pass123"),
                    "Marcus Reed", "Bed 1", "Room Lead", room);
            marcus.setPresenceStatus(User.PresenceStatus.IN_ROOM);
            userRepository.save(marcus);

            User alex = new User("alex@room209.internal", passwordEncoder.encode("pass123"),
                    "Alex Chen", "Bed 2", "Resident", room);
            alex.setPresenceStatus(User.PresenceStatus.STUDYING_QUIET);
            userRepository.save(alex);

            User dev = new User("dev@room209.internal", passwordEncoder.encode("pass123"),
                    "Dev Patel", "Bed 3", "Resident", room);
            dev.setPresenceStatus(User.PresenceStatus.AWAY);
            userRepository.save(dev);

            User sam = new User("sam@room209.internal", passwordEncoder.encode("pass123"),
                    "Sam Taylor", "Bed 4", "Resident", room);
            sam.setPresenceStatus(User.PresenceStatus.IN_ROOM);
            userRepository.save(sam);

            logger.info("Roommates seeded. Feed, Chores, Plans, and Polls remain empty for dynamic live usage.");
        }
    }
}

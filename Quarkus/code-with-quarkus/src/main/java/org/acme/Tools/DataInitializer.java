package org.acme.Tools;

import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.Model.*;
import org.acme.Repositories.EventTypeRepository;
import org.acme.Repositories.RoleRepository;
import org.acme.Repositories.TicketTypeRepository;
import org.acme.Repositories.UserRepository;

@ApplicationScoped
public class DataInitializer {

    @Inject
    EventTypeRepository eventTypeRepository;

    @Inject
    TicketTypeRepository ticketTypeRepository;

    @Inject
    RoleRepository roleRepository;

    @Inject
    UserRepository userRepository;

    @Transactional
    public void init(@Observes StartupEvent e) {
        // Insert EventTypeEnum values
        if (EventType.count() == 0) {
            for (EventTypeEnum eType : EventTypeEnum.values()) {
                EventType eventType = new EventType();
                eventType.setEType(eType);
                eventTypeRepository.persist(eventType);
            }
        }

        // Insert TicketTypeEnum values
        if (TicketType.count() == 0) {
            for (TicketTypeEnum tType : TicketTypeEnum.values()) {
                TicketType ticketType = new TicketType();
                ticketType.setTType(tType);
                ticketTypeRepository.persist(ticketType);
            }
        }

        // Insert RoleEnum values
        if (Role.count() == 0) {
            for (RoleEnum role : RoleEnum.values()) {
                Role roleEntity = new Role();
                roleEntity.setRole(role);
                roleRepository.persist(roleEntity);

                // Insert Admin user
                /*if (roleEntity.getRole().equals(RoleEnum.ADMIN)) {
                    User admin = new User();
                    admin.setUsername("admin");
                    admin.setPassword("admin"); // Consider hashing this in production!
                    admin.setRole(roleEntity);
                    userRepository.persist(admin);
                }*/
            }
        }
    }
}
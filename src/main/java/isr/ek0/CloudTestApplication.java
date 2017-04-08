package isr.ek0;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Collection;

import static java.util.Arrays.*;

@SpringBootApplication
public class CloudTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudTestApplication.class, args);
    }

    @Bean
    CommandLineRunner runer(ReservationRepository rr) {
        return args -> {
            asList("Me", "Myself", "AND I").forEach(s -> rr.save(new Reservation(s)));
            rr.findAll().forEach(System.out::println);
        };
    }
}

@RefreshScope
@RestController
class MessageRestController {
    @Value("${message}")
    private String message;

    @GetMapping("/message")
    public String getMessage() {
        return message;
    }
}

@RepositoryRestResource
interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @RestResource(path = "/by-name")
    Collection<Reservation> findByReservationName(@Param("name") String name);
}

@Entity
@Data
@NoArgsConstructor
class Reservation {
    @Id
    @GeneratedValue
    private Long id;
    private String reservationName;

    public Reservation(String reservationName) {
        this.reservationName = reservationName;
    }
}
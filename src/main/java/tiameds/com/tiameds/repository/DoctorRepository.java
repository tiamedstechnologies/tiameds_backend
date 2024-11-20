package tiameds.com.tiameds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tiameds.com.tiameds.entity.Doctors;

public interface DoctorRepository extends JpaRepository<Doctors, Long> {
    boolean existsByEmail(String email);

    Doctors findByEmail(String email);
}
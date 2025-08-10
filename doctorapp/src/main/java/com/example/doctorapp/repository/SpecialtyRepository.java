    package com.example.doctorapp.repository;

    import com.example.doctorapp.model.Specialty;
    import org.springframework.data.jpa.repository.JpaRepository;
    import java.util.Optional;
    import org.springframework.stereotype.Repository;

    @Repository
    public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {
        Optional<Specialty> findByName(String name);
    }

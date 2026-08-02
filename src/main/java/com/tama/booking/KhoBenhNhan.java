package com.tama.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KhoBenhNhan extends JpaRepository<Benhnhan, Long> {
    List<Benhnhan> findByEmail(String email);
}
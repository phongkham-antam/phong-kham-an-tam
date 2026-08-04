package com.tama.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KhoNguoiDungBenhNhan extends JpaRepository<NguoiDungBenhNhan, Long> {

    Optional<NguoiDungBenhNhan> findByEmail(String email);

}
package com.tama.booking;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "nguoidungbenhnhan")
public class NguoiDungBenhNhan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "ho_ten")
    private String hoTen;


    @Column(name = "so_dien_thoai")
    private String soDienThoai;


    @Column(name = "email", unique = true)
    private String email;


    @Column(name = "mat_khau")
    private String matKhau;


    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;


    // ===== Getter Setter =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }


    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }


    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }
}

package com.tama.booking;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Controller
public class WebController {

    @Autowired
    private KhoBenhNhan khoBenhNhan;

    // --- CÁC TRANG GIAO DIỆN TĨNH ---
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/trang_chu")
    public String trangchu() {
        return "trang_chu"; 
    }

    @GetMapping("/gioi_thieu")
    public String gioithieu() {
        return "gioi_thieu"; 
    }

    @GetMapping("/khoa_noi")
    public String khoanoi() {
        return "khoa_noi"; 
    }

    @GetMapping("/khoa_ngoai")
    public String khoangoai() {
        return "khoa_ngoai"; 
    }

    @GetMapping("/khoa_mat")
    public String khoamat() {
        return "khoa_mat"; 
    }

    @GetMapping("/khoa_da_lieu")
    public String khoadalieu() {
        return "khoa_da_lieu"; 
    }

    @GetMapping("/khoa_rang_ham_mat")
    public String khoaranghammat() {
        return "khoa_rang_ham_mat"; 
    }

    @GetMapping("/doi_ngu_bac_si")
    public String doingubacsi() {
        return "doi_ngu_bac_si"; 
    }

    // --- ĐĂNG KÝ BỆNH NHÂN ---
    @GetMapping("/dang_ky")
    public String trangDangKy() {
        return "dang_ky"; 
    }

    @PostMapping("/dang_ky")
    public String xuLyDangKy(@RequestParam("fullName") String fullName,
                             @RequestParam("email") String email,
                             @RequestParam("password") String password,
                             Model model) {
        
        List<Benhnhan> existingUsers = khoBenhNhan.findByEmail(email);
        if (!existingUsers.isEmpty()) {
            model.addAttribute("error", "Email này đã được sử dụng!");
            return "dang_ky"; 
        }

        Benhnhan bn = new Benhnhan();
        bn.setHoTen(fullName);
        bn.setEmail(email);
        bn.setMatKhau(password);
        bn.setNgayTao(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        
        khoBenhNhan.save(bn);

        return "redirect:/dang_nhap";
    }

    // --- ĐĂNG NHẬP BỆNH NHÂN ---
    @GetMapping("/dang_nhap")
    public String trangDangNhapBenhNhan() {
        return "dang_nhap"; 
    }

    @PostMapping("/dang_nhap")
    public String xuLyDangNhapBenhNhan(@RequestParam("email") String email,
                                      @RequestParam("password") String password,
                                      HttpSession session, 
                                      Model model) {
        
        // Kiểm tra thông tin đăng nhập trong DB
        List<Benhnhan> users = khoBenhNhan.findByEmail(email);
        if (users.isEmpty()) {
            model.addAttribute("error", "Email không tồn tại trong hệ thống!");
            return "dang_nhap";
        }

        // Kiểm tra mật khẩu (Giả sử lấy user đầu tiên tìm thấy)
        Benhnhan user = users.get(0);
        if (user.getMatKhau() != null && !user.getMatKhau().equals(password)) {
            model.addAttribute("error", "Mật khẩu không chính xác!");
            return "dang_nhap";
        }

        // Lưu session userEmail
        session.setAttribute("userEmail", email);
        
        return "redirect:/"; 
    }

    // --- ĐẶT LỊCH KHÁM BỆNH ---
    @GetMapping("/dat_lich_kham_benh")
    public String dat_lich_kham_benh(HttpSession session, Model model) {
        String emailDangNhap = (String) session.getAttribute("userEmail");
        if (emailDangNhap == null) {
            return "redirect:/dang_nhap"; 
        }
        return "dat_lich_kham_benh"; 
    }

    @PostMapping("/dat-lich/luu")
    public String xuLyDatLich(@RequestParam("fullName") String fullName,
                              @RequestParam("phone") String phone,
                              @RequestParam(value = "department", required = false) String department,
                              @RequestParam(value = "doctorName", required = false) String doctorName,
                              @RequestParam(value = "appointmentDate", required = false) String appointmentDate,
                              @RequestParam(value = "timeSlot", required = false) String timeSlot,
                              @RequestParam(value = "note", required = false) String note,
                              HttpSession session,
                              Model model) {
        
        String emailDangNhap = (String) session.getAttribute("userEmail");
        if (emailDangNhap == null) {
            return "redirect:/dang_nhap"; 
        }
        
        Benhnhan bn = new Benhnhan();
        bn.setEmail(emailDangNhap); 
        bn.setHoTen(fullName);
        bn.setSoDienThoai(phone);
        bn.setChuyenKhoaId(department); 
        bn.setBacSiId(doctorName);    
        bn.setNgayKham(appointmentDate);
        bn.setGioKham(timeSlot); 
        bn.setTrieuChung(note);
        bn.setStatus("CHO_XAC_NHAN"); // Trạng thái mặc định khi đặt lịch
        bn.setNgayTao(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        
        khoBenhNhan.save(bn);

        model.addAttribute("successMessage", true);
        model.addAttribute("datKham", bn);

        return "dat_lich_kham_benh";
    }

    // --- XEM LỊCH SỬ ---
    @GetMapping("/lich_su_dang_ky")
    public String lichsudangky(HttpSession session, Model model) {
        String emailDangNhap = (String) session.getAttribute("userEmail"); 

        if (emailDangNhap == null || emailDangNhap.isEmpty()) {
            return "redirect:/dang_nhap";
        }

        List<Benhnhan> danhSachLich = khoBenhNhan.findByEmail(emailDangNhap);
        
        model.addAttribute("danhSachLich", danhSachLich);
        model.addAttribute("userEmail", emailDangNhap);
        return "lich_su_dang_ky"; 
    }

    // --- XỬ LÝ ĐỔI LỊCH ---
    @PostMapping("/doi-lich")
    public String doiLich(@RequestParam("id") Long id,
                          @RequestParam("ngayKhamMoi") String ngayKhamMoi,
                          @RequestParam("gioKhamMoi") String gioKhamMoi,
                          @RequestParam(value = "lyDoDoi", required = false) String lyDoDoi,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        
        String emailDangNhap = (String) session.getAttribute("userEmail");
        if (emailDangNhap == null) {
            return "redirect:/dang_nhap";
        }

        Benhnhan bn = khoBenhNhan.findById(id).orElse(null);
        if (bn != null) {
            if (!emailDangNhap.equals(bn.getEmail())) {
                redirectAttributes.addFlashAttribute("error", "Bạn không có quyền thay đổi lịch khám này!");
                return "redirect:/lich_su_dang_ky";
            }

            bn.setNgayKham(ngayKhamMoi);
            bn.setGioKham(gioKhamMoi);
            bn.setStatus("DA_DOI_LICH");
            bn.setLyDoDoi(lyDoDoi);
            
            khoBenhNhan.save(bn);
            redirectAttributes.addFlashAttribute("message", "Đổi lịch khám thành công!");
        }
        return "redirect:/lich_su_dang_ky";
    }

    // --- XỬ LÝ HỦY LỊCH ---
    @PostMapping("/huy-lich")
    public String huyLich(@RequestParam("id") Long id,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        
        String emailDangNhap = (String) session.getAttribute("userEmail");
        if (emailDangNhap == null) {
            return "redirect:/dang_nhap";
        }

        Benhnhan bn = khoBenhNhan.findById(id).orElse(null);
        if (bn != null) {
            if (!emailDangNhap.equals(bn.getEmail())) {
                redirectAttributes.addFlashAttribute("error", "Bạn không có quyền hủy lịch khám này!");
                return "redirect:/lich_su_dang_ky";
            }

            bn.setStatus("DA_HUY");
            khoBenhNhan.save(bn);
            redirectAttributes.addFlashAttribute("message", "Hủy lịch khám thành công!");
        }
        return "redirect:/lich_su_dang_ky";
    }
}
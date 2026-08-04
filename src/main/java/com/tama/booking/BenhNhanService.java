package com.tama.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BenhNhanService {

    @Autowired
    private KhoBenhNhan khoBenhNhan;

    public boolean saveBenhNhan(Benhnhan benhnhan) {
        // Kiểm tra xem đã tồn tại lịch trùng của Bác sĩ + Ngày khám + Giờ khám này chưa
        boolean isTrung = khoBenhNhan.existsByBacSiIdAndNgayKhamAndGioKham(
            benhnhan.getBacSiId(), 
            benhnhan.getNgayKham(), 
            benhnhan.getGioKham()
        );

        if (isTrung) {
            return false; // Đã có người đặt -> Trả về false để Controller báo lỗi
        }

        // Chưa có thì tiến hành lưu bình thường
        khoBenhNhan.save(benhnhan);
        return true;
    }
}
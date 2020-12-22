package model;

import java.time.LocalDate;

public class Reader implements Comparable<Reader>{
    private String maDG;
    private String tenDG;
    private String gioiTinh;
    private LocalDate ngaySinh;
    private String CMND;
    private String email;
    private String dienThoai;

    public Reader() {
    }

    public String getMaDG() {
        return maDG;
    }

    public void setMaDG(String maDG) {
        this.maDG = maDG;
    }

    public String getTenDG() {
        return tenDG;
    }

    public void setTenDG(String tenTT) {
        this.tenDG = tenTT;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getCMND() {
        return CMND;
    }

    public void setCMND(String CMND) {
        this.CMND = CMND;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDienThoai() {
        return dienThoai;
    }

    public void setDienThoai(String dienThoai) {
        this.dienThoai = dienThoai;
    }

    public Reader(String maTT, String tenTT, String gioiTinh, LocalDate ngaySinh, String CMND, String email, String dienThoai) {
        this.maDG = maTT;
        this.tenDG = tenTT;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.CMND = CMND;
        this.email = email;
        this.dienThoai = dienThoai;
    }

    @Override
    public int compareTo(Reader l) {
        return this.getMaDG().compareTo(l.getMaDG());
    }
}

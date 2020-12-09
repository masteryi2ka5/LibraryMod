package model;

import java.time.LocalDate;

public class Librarian implements Comparable<Librarian> {
    private String maTT;
    private String tenTT;
    private String gioiTinh;
    private LocalDate ngaySinh;
    private String CMND;
    private String email;
    private String dienThoai;

    public Librarian() {
    }

    public String getMaTT() {
        return maTT;
    }

    public void setMaTT(String maTT) {
        this.maTT = maTT;
    }

    public String getTenTT() {
        return tenTT;
    }

    public void setTenTT(String tenTT) {
        this.tenTT = tenTT;
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

    public Librarian(String maTT, String tenTT, String gioiTinh, LocalDate ngaySinh, String CMND, String email, String dienThoai) {
        this.maTT = maTT;
        this.tenTT = tenTT;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.CMND = CMND;
        this.email = email;
        this.dienThoai = dienThoai;
    }

    @Override
    public int compareTo(Librarian l) {
        return this.getMaTT().compareTo(l.getMaTT());
    }
}

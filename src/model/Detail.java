package model;

import java.time.LocalDate;

public class Detail implements Comparable<Detail> {
    private String maMT;
    private String maSach;
    private LocalDate ngayTra;
    private String trangThai;
    private int tienPhat;
    private String ghiChu;

    public Detail() {
    }

    public Detail(String maMT, String maSach, LocalDate ngayTra, String trangThai, int tienPhat, String ghiChu) {
        this.maMT = maMT;
        this.maSach = maSach;
        this.ngayTra = ngayTra;
        this.trangThai = trangThai;
        this.tienPhat = tienPhat;
        this.ghiChu = ghiChu;
    }

    public String getMaMT() {
        return maMT;
    }

    public void setMaMT(String maMT) {
        this.maMT = maMT;
    }

    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public LocalDate getNgayTra() {
        return ngayTra;
    }

    public void setNgayTra(LocalDate ngayTra) {
        this.ngayTra = ngayTra;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public int getTienPhat() {
        return tienPhat;
    }

    public void setTienPhat(int tienPhat) {
        this.tienPhat = tienPhat;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    @Override
    public int compareTo(Detail b) {
        return this.getMaMT().compareTo(b.getMaMT());
    }
}

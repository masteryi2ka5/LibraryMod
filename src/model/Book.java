package model;

public class Book {
    private String maSach;
    private String tenSach;
    private String tacGia;
    private String nhaXB;
    private int namXB;
    private int donGia;
    private int soLuong;

    public Book(String maSach, String tenSach, String tacGia, String nhaXB, int namXB, int donGia, int soLuong) {
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.tacGia = tacGia;
        this.nhaXB = nhaXB;
        this.namXB = namXB;
        this.donGia = donGia;
        this.soLuong = soLuong;
    }

    public String getMaSach() {
        return this.maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public String getTenSach() {
        return this.tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getTacGia() {
        return this.tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public String getNhaXB() {
        return this.nhaXB;
    }

    public void setNhaXB(String nhaXB) {
        this.nhaXB = nhaXB;
    }

    public int getNamXB() {
        return this.namXB;
    }

    public void setNamXB(int namXB) {
        this.namXB = namXB;
    }

    public int getDonGia() {
        return this.donGia;
    }

    public void setDonGia(int donGia) {
        this.donGia = donGia;
    }

    public int getSoLuong() {
        return this.soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}

package model;

import java.time.LocalDate;

public class BorrLend implements Comparable<BorrLend>{
    private String maMT;
    private String maDG;
    private String maTT;
    private LocalDate ngayMuon;
    private LocalDate ngayHenTra;
    private int tienCoc;

    public BorrLend(String maMT, String maDG, String maTT, LocalDate ngayMuon, LocalDate ngayHenTra, int tienCoc) {
        this.maMT = maMT;
        this.maDG = maDG;
        this.maTT = maTT;
        this.ngayMuon = ngayMuon;
        this.ngayHenTra = ngayHenTra;
        this.tienCoc = tienCoc;
    }

    public BorrLend() {
    }

    public String getMaMT() {
        return this.maMT;
    }

    public void setMaMT(String maMT) {
        this.maMT = maMT;
    }

    public String getMaDG() {
        return this.maDG;
    }

    public void setMaDG(String maDG) {
        this.maDG = maDG;
    }

    public String getMaTT() {
        return this.maTT;
    }

    public void setMaTT(String maTT) {
        this.maTT = maTT;
    }

    public LocalDate getNgayMuon() {
        return this.ngayMuon;
    }

    public void setNgayMuon(LocalDate ngayMuon) {
        this.ngayMuon = ngayMuon;
    }

    public LocalDate getNgayHenTra() {
        return this.ngayHenTra;
    }

    public void setNgayHenTra(LocalDate ngayHenTra) {
        this.ngayHenTra = ngayHenTra;
    }

    public int getTienCoc() {
        return this.tienCoc;
    }

    public void setTienCoc(int tienCoc) {
        this.tienCoc = tienCoc;
    }

    @Override
    public int compareTo(BorrLend b) {
        return this.getMaMT().compareTo(b.getMaMT());
    }
}

package com.example.collegemanager.Student;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "student")  // Đánh dấu lớp này là một Entity trong Room Database và đặt tên bảng là "student"
public class student implements Serializable {  // Lớp student thực hiện Serializable để có thể truyền qua Intent hoặc Bundle
    private String hoTen;  // Tên của sinh viên
    @PrimaryKey
    @NonNull
    private String maSV;  // Mã sinh viên, là khóa chính của bảng "student"
    private float diemToan;  // Điểm môn Toán của sinh viên
    private float diemVan;  // Điểm môn Văn của sinh viên
    private float diemAnh;  // Điểm môn Anh của sinh viên

    // Constructor mặc định
    public student() {}

    // Constructor với đầy đủ tham số để khởi tạo một đối tượng student
    public student(String hoTen, String maSV, float diemToan, float diemVan, float diemAnh) {
        this.hoTen = hoTen;
        this.maSV = maSV;
        this.diemToan = diemToan;
        this.diemVan = diemVan;
        this.diemAnh = diemAnh;
    }

    // Constructor với tên sinh viên và mã sinh viên (dành cho các tình huống không cần điểm số)
    public student(String hoTen, String maSV) {
        this.hoTen = hoTen;
        this.maSV = maSV;
    }

    // Getter và Setter cho các trường dữ liệu của sinh viên

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public float getDiemToan() {
        return diemToan;
    }

    public void setDiemToan(float diemToan) {
        this.diemToan = diemToan;
    }

    public float getDiemVan() {
        return diemVan;
    }

    public void setDiemVan(float diemVan) {
        this.diemVan = diemVan;
    }

    public float getDiemAnh() {
        return diemAnh;
    }

    public void setDiemAnh(float diemAnh) {
        this.diemAnh = diemAnh;
    }
}

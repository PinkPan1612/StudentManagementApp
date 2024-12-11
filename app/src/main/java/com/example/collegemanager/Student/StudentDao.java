package com.example.collegemanager.Student;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao  // Đánh dấu interface này là một DAO (Data Access Object) của Room, dùng để thao tác với cơ sở dữ liệu.
public interface StudentDao {

    // Phương thức để chèn một đối tượng student vào bảng student
    @Insert
    void Insert(student student);

    // Phương thức để lấy tất cả danh sách sinh viên trong bảng student
    @Query("SELECT * FROM student")  // SQL query để chọn tất cả các bản ghi từ bảng student
    List<student> getList();

    // Phương thức để xóa một sinh viên khỏi bảng student
    @Delete
    void Delete(student student);

    // Phương thức để cập nhật thông tin của một sinh viên trong bảng student
    @Update
    void update(student student);

    // Phương thức để kiểm tra sinh viên theo mã số sinh viên (maSV)
    @Query("SELECT * FROM student WHERE maSV =:msv")  // SQL query để chọn sinh viên có mã số sinh viên bằng với tham số msv
    List<student> isCheckStudent(String msv);

    // Phương thức để tìm kiếm sinh viên theo mã số sinh viên, sử dụng LIKE để tìm kiếm chuỗi con trong maSV
    @Query("SELECT*FROM student WHERE (maSV LIKE '%' || :msv || '%')")  // SQL query sử dụng LIKE để tìm kiếm mã sinh viên chứa chuỗi msv
    List<student> search(String msv);

    // Phương thức để sắp xếp sinh viên theo điểm tổng hợp (toán + văn + anh) giảm dần
    @Query("SELECT * FROM student ORDER BY (diemToan + diemVan + diemAnh) DESC")  // SQL query sắp xếp sinh viên theo tổng điểm giảm dần
    List<student> orderSort();

    // Phương thức để sắp xếp sinh viên theo điểm tổng hợp (toán + văn + anh) tăng dần
    @Query("SELECT * FROM student ORDER BY (diemToan + diemVan + diemAnh) ASC")  // SQL query sắp xếp sinh viên theo tổng điểm tăng dần
    List<student> orderSortReverse();

}

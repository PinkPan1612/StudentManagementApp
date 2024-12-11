package com.example.collegemanager.Student;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {student.class}, version = 1)  // Đánh dấu lớp này là cơ sở dữ liệu Room, với bảng là 'student' và phiên bản 1
public abstract class StudentDatabase extends RoomDatabase {

    public static final String table_name = "student.db";  // Đặt tên tệp cơ sở dữ liệu là "student.db"
    public static StudentDatabase instance;  // Biến static duy nhất cho cơ sở dữ liệu (Singleton Pattern)

    // Phương thức để lấy thể hiện của cơ sở dữ liệu, nếu chưa tồn tại sẽ tạo mới
    public static synchronized StudentDatabase getInstanceStudent(Context context) {
        if (instance == null) {
            // Tạo cơ sở dữ liệu bằng Room database builder
            instance = Room.databaseBuilder(context, StudentDatabase.class, table_name)
                    .allowMainThreadQueries()  // Cho phép thực hiện truy vấn trên main thread
                    .build();  // Xây dựng cơ sở dữ liệu và trả về thể hiện của nó
        }
        return instance;  // Trả về thể hiện của cơ sở dữ liệu
    }

    // Trả về một thể hiện của StudentDao, cho phép thao tác với bảng student
    public abstract StudentDao studentDao();
}

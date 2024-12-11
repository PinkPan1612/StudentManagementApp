package com.example.collegemanager.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.collegemanager.Student.StudentDatabase;
import com.example.collegemanager.databinding.UpdateFragmentBinding;
import com.example.collegemanager.Student.student;

public class UpdateFragment extends Fragment {
    private UpdateFragmentBinding binding;  // Biến ViewBinding để liên kết với layout của fragment
    private static final String ARG_STUDENT = "student";  // Key để truyền thông tin sinh viên vào fragment
    private HomeFragment homeFragment;  // Fragment quay lại
    private student student;  // Biến để lưu thông tin sinh viên cần cập nhật

    // Phương thức tĩnh để tạo một instance của UpdateFragment với dữ liệu sinh viên
    public static UpdateFragment newInstance(student student) {
        UpdateFragment fragment = new UpdateFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_STUDENT, student);  // Đưa thông tin sinh viên vào Bundle
        fragment.setArguments(args);  // Gán Bundle cho fragment
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Liên kết layout với fragment thông qua ViewBinding
        binding = UpdateFragmentBinding.inflate(inflater, container, false);

        // Kiểm tra và nhận dữ liệu sinh viên nếu có
        if (getArguments() != null) {
            student = (student) getArguments().getSerializable(ARG_STUDENT);  // Lấy thông tin sinh viên từ Bundle
            receiveDataFromFragmentHome(student);  // Hiển thị thông tin sinh viên trên giao diện
        }

        homeFragment = new HomeFragment();  // Khởi tạo HomeFragment

        // Xử lý sự kiện khi nhấn nút "Cập nhật"
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStudent();  // Gọi phương thức cập nhật thông tin sinh viên
            }
        });

        // Xử lý sự kiện khi nhấn nút "Quay lại"
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();  // Quay lại fragment trước đó
            }
        });

        return binding.getRoot();  // Trả về root view của fragment
    }

    // Phương thức hiển thị thông tin sinh viên trên giao diện
    public void receiveDataFromFragmentHome(student student) {
        if (student != null) {
            binding.edtName.setText(student.getHoTen());  // Hiển thị tên sinh viên
            binding.edtMSV.setText(student.getMaSV());  // Hiển thị mã số sinh viên
            binding.edtMath.setText(String.valueOf(student.getDiemToan()));  // Hiển thị điểm Toán
            binding.edtVan.setText(String.valueOf(student.getDiemVan()));  // Hiển thị điểm Văn
            binding.edtAnh.setText(String.valueOf(student.getDiemAnh()));  // Hiển thị điểm Anh
        }
    }

        // Phương thức để cập nhật thông tin sinh viên
    private void updateStudent() {
        // Lấy dữ liệu từ các trường nhập liệu
        String name = binding.edtName.getText().toString().trim();
        String maSV = binding.edtMSV.getText().toString().trim();
        String diemToan = binding.edtMath.getText().toString().trim();
        String diemVan = binding.edtVan.getText().toString().trim();
        String diemAnh = binding.edtAnh.getText().toString().trim();

        // Kiểm tra nếu thông tin không đầy đủ, hiển thị thông báo
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(maSV) || TextUtils.isEmpty(diemToan) || TextUtils.isEmpty(diemVan) || TextUtils.isEmpty(diemAnh)) {
            Toast.makeText(getContext(), "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Chuyển đổi điểm thành kiểu dữ liệu float
        float toan = Float.parseFloat(diemToan);
        float van = Float.parseFloat(diemVan);
        float anh = Float.parseFloat(diemAnh);

        // Cập nhật thông tin sinh viên vào đối tượng student
        student.setHoTen(name);
        student.setMaSV(maSV);
        student.setDiemToan(toan);
        student.setDiemVan(van);
        student.setDiemAnh(anh);

        // Cập nhật thông tin sinh viên trong cơ sở dữ liệu
        StudentDatabase.getInstanceStudent(getContext()).studentDao().update(student);

        // Hiển thị thông báo cập nhật thành công
        Toast.makeText(getContext(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
    }
}

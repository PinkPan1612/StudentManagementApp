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

import com.example.collegemanager.MainActivity;
import com.example.collegemanager.R;
import com.example.collegemanager.Student.StudentDatabase;
import com.example.collegemanager.Student.student;
import com.example.collegemanager.databinding.CreateFragmentBinding;

import java.util.List;

public class CreateFragment extends Fragment {
    private CreateFragmentBinding binding; // Kết nối với layout XML qua ViewBinding
    private MainActivity mMainActivity;   // Lưu tham chiếu đến MainActivity để tương tác giữa các Fragment
    private List<student> myList;         // Danh sách học sinh để kiểm tra và thêm mới

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CreateFragmentBinding.inflate(inflater, container, false);

        mMainActivity = (MainActivity) getActivity(); // Lấy MainActivity hiện tại

        // Xử lý sự kiện khi nhấn nút "Thêm học sinh"
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniUI(); // Thực hiện kiểm tra và thêm học sinh
            }
        });

        // Xử lý sự kiện khi nhấn nút "Quay lại"
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay về HomeFragment và cập nhật thanh điều hướng
                mMainActivity.replaceFragment(new HomeFragment(), null);
                mMainActivity.binding.bottomNav.setItemSelected(R.id.Home, true);
            }
        });

        return binding.getRoot(); // Trả về View gốc của layout
    }

    // Phương thức kiểm tra thông tin nhập và thêm học sinh mới
    private void iniUI() {
        // Lấy giá trị từ các trường nhập liệu
        String name = binding.edtName.getText().toString().trim();
        String MSV = binding.edtMSV.getText().toString().trim();
        String diem_toan = binding.edtMath.getText().toString().trim();
        String diem_van = binding.edtVan.getText().toString().trim();
        String diem_anh = binding.edtAnh.getText().toString().trim();

        // Kiểm tra nếu thông tin bị bỏ trống
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(MSV) ||
                TextUtils.isEmpty(diem_toan) || TextUtils.isEmpty(diem_van) ||
                TextUtils.isEmpty(diem_anh)) {
            Toast.makeText(getContext(), "Mời nhập thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Chuyển đổi điểm từ String sang float
        float diemToan = Float.parseFloat(diem_toan);
        float diemVan = Float.parseFloat(diem_van);
        float diemAnh = Float.parseFloat(diem_anh);

        // Kiểm tra điểm có nằm trong khoảng 0 - 10 hay không
        if (!(checkScore(diemToan) && checkScore(diemVan) && checkScore(diemAnh))) {
            Toast.makeText(getContext(), "Xin nhập điểm trong thang điểm 10", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng học sinh mới
        student obj = new student(name, MSV, diemToan, diemVan, diemAnh);

        // Kiểm tra xem học sinh đã tồn tại trong cơ sở dữ liệu chưa
        myList = StudentDatabase.getInstanceStudent(getContext()).studentDao().isCheckStudent(MSV);
        for (student it : myList) {
            if (MSV.equals(it.getMaSV())) {
                Toast.makeText(getContext(), "Học sinh đã tồn tại", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Chèn học sinh vào cơ sở dữ liệu
        StudentDatabase.getInstanceStudent(getContext()).studentDao().Insert(obj);
        Toast.makeText(getContext(), "Thêm học sinh thành công", Toast.LENGTH_SHORT).show();
    }

    // Kiểm tra điểm có nằm trong thang điểm 10 hay không
    private boolean checkScore(float score) {
        return score >= 0 && score <= 10;
    }
}

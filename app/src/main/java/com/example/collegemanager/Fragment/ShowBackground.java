package com.example.collegemanager.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.collegemanager.MainActivity;
import com.example.collegemanager.Student.student;
import com.example.collegemanager.databinding.FragmentshowbackgroundBinding;

public class ShowBackground extends Fragment {
    private FragmentshowbackgroundBinding binding;  // Biến để làm việc với ViewBinding của layout
    private static final String ARG_STUDENT = "student";  // Key để truyền thông tin sinh viên
    private MainActivity mMainActivity;  // Biến tham chiếu đến MainActivity

    // Phương thức tĩnh để tạo một instance của fragment và truyền thông tin sinh viên vào fragment
    public static ShowBackground getInstance(student student) {
        ShowBackground fragment = new ShowBackground();
        Bundle args = new Bundle();
        args.putSerializable(ARG_STUDENT, student);  // Đưa thông tin sinh viên vào Bundle
        fragment.setArguments(args);  // Gán Bundle cho fragment
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Liên kết layout với fragment thông qua ViewBinding
        binding = FragmentshowbackgroundBinding.inflate(inflater, container, false);

        // Kiểm tra nếu có đối số (student) được truyền vào fragment
        if (getArguments() != null) {
            student student = (student) getArguments().getSerializable(ARG_STUDENT);  // Lấy đối tượng student từ Bundle
            showInfor(student);  // Hiển thị thông tin sinh viên
        }

        mMainActivity = (MainActivity) getActivity();  // Lấy tham chiếu đến MainActivity

        // Xử lý sự kiện khi nhấn nút quay lại
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainActivity.replaceFragment(new QueryFragment(), null);  // Quay lại fragment QueryFragment
            }
        });

        return binding.getRoot();  // Trả về root view của fragment
    }

    // Phương thức để hiển thị thông tin sinh viên
    public void showInfor(student student) {
        if (student != null) {
            // Hiển thị tên sinh viên, mã số sinh viên, và điểm các môn học
            binding.txtTen.setText(student.getHoTen());
            binding.txtMSV.setText(student.getMaSV());

            // Tính trung bình cộng điểm các môn
            double tbc = 1.0 * (student.getDiemToan() + student.getDiemAnh() + student.getDiemVan()) / 3;

            // Xếp loại sinh viên dựa trên điểm trung bình
            if (tbc >= 8.5) {
                binding.txtXepLoai.setText("Giỏi");
            } else if (tbc >= 7 && tbc < 8.5) {
                binding.txtXepLoai.setText("Khá");
            } else if (tbc >= 5 && tbc < 7) {
                binding.txtXepLoai.setText("TB");
            } else {
                binding.txtXepLoai.setText("Yếu");
            }

            // Hiển thị điểm từng môn
            binding.txtDiemToan.setText(String.valueOf(student.getDiemToan()));
            binding.txtDiemVan.setText(String.valueOf(student.getDiemVan()));
            binding.txtDiemAnh.setText(String.valueOf(student.getDiemAnh()));
        }
    }

}

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
import com.example.collegemanager.databinding.ChangepasswordFragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordFragment extends Fragment {
    // Khai báo biến binding để làm việc với View Binding
    private ChangepasswordFragmentBinding binding;

    // Biến tham chiếu đến MainActivity để tương tác với các phương thức của Activity
    private MainActivity mMainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Liên kết layout XML với Fragment thông qua View Binding
        binding = ChangepasswordFragmentBinding.inflate(inflater, container, false);

        // Tham chiếu đến MainActivity để sử dụng các phương thức như `replaceFragment`
        mMainActivity = (MainActivity) getActivity();

        // Gán sự kiện click cho nút "Cập nhật mật khẩu"
        binding.btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitUi(); // Xử lý logic thay đổi mật khẩu
            }
        });

        // Gán sự kiện click cho nút "Quay lại"
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay lại Fragment cài đặt
                mMainActivity.replaceFragment(new SettingFragment(), null);
            }
        });

        // Hiển thị thông tin người dùng từ MainActivity
        binding.txtUsername.setText(mMainActivity.getHoTen());
        binding.txtEmail.setText(mMainActivity.getEmail());

        return binding.getRoot(); // Trả về View gốc của Fragment
    }

    // Phương thức kiểm tra dữ liệu và xử lý logic đổi mật khẩu
    private void InitUi() {
        // Lấy thông tin từ các trường nhập liệu
        String passwordOld = binding.edtPasswordOld.getText().toString().trim();
        String passwordNew = binding.edtPasswordNew.getText().toString().trim();
        String passwordNewAgain = binding.edtChpassAgain.getText().toString().trim();

        // Kiểm tra các trường nhập liệu có bị bỏ trống không
        if (TextUtils.isEmpty(passwordOld) || TextUtils.isEmpty(passwordNew) || TextUtils.isEmpty(passwordNewAgain)) {
            Toast.makeText(getContext(), "Hãy nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra mật khẩu mới có khớp với mật khẩu nhập lại không
        if (!passwordNew.equals(passwordNewAgain)) {
            Toast.makeText(getContext(), "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy đối tượng người dùng hiện tại từ Firebase Authentication
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        binding.progress.setVisibility(View.VISIBLE); // Hiển thị progress bar trong lúc xử lý

        assert user != null; // Đảm bảo `user` không null trước khi thực hiện
        user.updatePassword(passwordNew)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        binding.progress.setVisibility(View.GONE); // Ẩn progress bar khi hoàn thành
                        if (task.isSuccessful()) {
                            // Thông báo khi đổi mật khẩu thành công
                            Toast.makeText(getContext(), "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Thông báo khi đổi mật khẩu không thành công
                            Toast.makeText(getContext(), "Đổi mật khẩu không thành công!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

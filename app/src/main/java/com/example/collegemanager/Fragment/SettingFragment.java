package com.example.collegemanager.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.collegemanager.LoginActivity;
import com.example.collegemanager.MainActivity;
import com.example.collegemanager.R;
import com.example.collegemanager.databinding.SettingFragmentBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SettingFragment extends Fragment {
    private SettingFragmentBinding binding;  // Biến để làm việc với ViewBinding của layout
    private MainActivity mMainActivity;  // Biến tham chiếu đến MainActivity

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Liên kết layout với fragment thông qua ViewBinding
        binding = SettingFragmentBinding.inflate(inflater, container, false);

        mMainActivity =(MainActivity) getActivity();  // Lấy tham chiếu đến MainActivity

        // Xử lý sự kiện khi nhấn nút quay lại
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainActivity.replaceFragment(new HomeFragment(), null);  // Quay lại fragment Home
                mMainActivity.binding.bottomNav.setItemSelected(R.id.Home, true);  // Đặt tab Home được chọn
            }
        });

        // Xử lý sự kiện khi nhấn nút đăng xuất
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị hộp thoại xác nhận đăng xuất
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle("Đăng xuất")  // Tiêu đề hộp thoại
                        .setMessage("Bạn muốn đăng xuất?")  // Nội dung thông báo
                        .setCancelable(false)  // Không thể thoát hộp thoại khi nhấn bên ngoài
                        .setNegativeButton("Thoát", null)  // Nút hủy
                        .setPositiveButton("Tiếp tục", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Đăng xuất người dùng khỏi Firebase
                                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                                firebaseAuth.signOut();
                                Toast.makeText(getContext(), "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();  // Hiển thị thông báo đăng xuất thành công

                                // Mở màn hình đăng nhập
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);

                                // Đóng tất cả các Activity trước đó
                                if (getActivity() != null) {
                                    getActivity().finishAffinity();
                                }
                            }
                        });
                // Tạo và hiển thị hộp thoại
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // Xử lý sự kiện khi nhấn nút thay đổi mật khẩu
        binding.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thay thế fragment hiện tại bằng fragment ChangePasswordFragment
                mMainActivity.replaceFragment(new ChangePasswordFragment(), null);
            }
        });

        // Hiển thị thông tin người dùng (tên và email)
        binding.txtUsername.setText(mMainActivity.getHoTen());
        binding.txtEmail.setText(mMainActivity.getEmail());

        return binding.getRoot();  // Trả về root view của fragment
    }
}

package com.example.collegemanager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.collegemanager.User.User;
import com.example.collegemanager.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;  // Biến để liên kết với view trong layout
    private int cnt = 0; // Biến đếm người dùng (dùng để tạo ID khi lưu vào Firebase)
    private int cnt1 = 0; // Biến đếm cho việc hiển thị mắt (ẩn/hiện mật khẩu)
    private int count = 0; // Biến đếm cho việc ghi nhận số lần nhấn nút đăng ký

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());  // Gán layout vào Activity
        setContentView(binding.getRoot());  // Thiết lập view cho Activity

        // Xử lý khi trường "Tài khoản" (email) nhận được hoặc mất focus
        binding.edtAccount.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.edtAccount.setTextColor(Color.parseColor("#000000"));
            } else {
                binding.edtAccount.setTextColor(Color.parseColor("#aaaaaa"));
            }
        });

        // Xử lý khi trường "Mật khẩu" nhận được hoặc mất focus
        binding.edtPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.edtPassword.setTextColor(Color.parseColor("#000000"));
                binding.imgEyes.setColorFilter(ContextCompat.getColor(SignupActivity.this, R.color.black), PorterDuff.Mode.SRC_IN);
            } else {
                binding.edtPassword.setTextColor(Color.parseColor("#aaaaaa"));
                binding.imgEyes.setColorFilter(ContextCompat.getColor(SignupActivity.this, R.color.gray), PorterDuff.Mode.SRC_IN);
            }
        });

        // Xử lý khi trường "Nhập lại mật khẩu" nhận được hoặc mất focus
        binding.edtPasswordA.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.edtPasswordA.setTextColor(Color.parseColor("#000000"));
                binding.imgEyese.setColorFilter(ContextCompat.getColor(SignupActivity.this, R.color.black), PorterDuff.Mode.SRC_IN);
            } else {
                binding.edtPasswordA.setTextColor(Color.parseColor("#aaaaaa"));
                binding.imgEyese.setColorFilter(ContextCompat.getColor(SignupActivity.this, R.color.black), PorterDuff.Mode.SRC_IN);
            }
        });

        // Xử lý sự kiện khi nhấn nút Đăng ký
        binding.btnSignup.setOnClickListener(v -> {
            onClickSignup();  // Gọi hàm xử lý đăng ký
            count++;  // Tăng biến đếm khi nhấn nút
        });

        // Xử lý sự kiện khi nhấn biểu tượng "mắt" để ẩn/hiện mật khẩu
        binding.imgEyes.setOnClickListener(v -> {
            if (cnt % 2 == 0) {
                binding.imgEyes.setImageResource(R.drawable.show);
                binding.edtPassword.setTransformationMethod(null);
            } else {
                binding.imgEyes.setImageResource(R.drawable.hide);
                binding.edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            cnt++;  // Tăng biến đếm khi nhấn
        });

        // Xử lý sự kiện khi nhấn biểu tượng "mắt" cho mật khẩu nhập lại
        binding.imgEyese.setOnClickListener(v -> {
            if (cnt1 % 2 == 0) {
                binding.imgEyese.setImageResource(R.drawable.show);
                binding.edtPasswordA.setTransformationMethod(null);
            } else {
                binding.imgEyese.setImageResource(R.drawable.hide);
                binding.edtPasswordA.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            cnt1++;  // Tăng biến đếm khi nhấn
        });

        // Xử lý sự kiện khi nhấn nút hủy để quay lại màn hình đăng nhập
        binding.btnHuy.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);  // Mở Activity đăng nhập
            finish();  // Kết thúc hiện tại Activity
        });
    }

    // Hàm xử lý khi người dùng nhấn nút Đăng ký
    private void onClickSignup() {
        String hoTen = binding.edtName.getText().toString().trim();  // Lấy tên người dùng
        String email = binding.edtAccount.getText().toString().trim();  // Lấy email
        String password = binding.edtPassword.getText().toString().trim();  // Lấy mật khẩu
        String passwordA = binding.edtPasswordA.getText().toString().trim();  // Lấy mật khẩu nhập lại

        // Kiểm tra xem có trường nào trống không
        if (TextUtils.isEmpty(hoTen) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordA)) {
            Toast.makeText(SignupActivity.this, "Hãy nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra định dạng email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(SignupActivity.this, "Tài khoản email không đúng định dạng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra mật khẩu nhập lại có trùng khớp không
        if (!passwordA.equals(password)) {
            Toast.makeText(SignupActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra mật khẩu có chứa ít nhất một ký tự đặc biệt không
        if (!(isCheckPassword(password) && isCheckPassword(passwordA))) {
            Toast.makeText(SignupActivity.this, "Mật khẩu phải chứa ít nhất 1 kí tự đặc biệt", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hiển thị ProgressBar khi đăng ký
        binding.progress.setVisibility(View.VISIBLE);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        binding.progress.setVisibility(View.GONE);
                        Toast.makeText(SignupActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();

                        // Chuyển qua màn hình chính
                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("email", email);
                        bundle.putString("name", hoTen);
                        intent.putExtra("user", bundle);
                        pushData(email, hoTen, count);  // Lưu thông tin người dùng vào Firebase
                        startActivity(intent);
                        finishAffinity();  // Kết thúc tất cả các Activity trước đó
                    } else {
                        binding.progress.setVisibility(View.GONE);
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(SignupActivity.this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignupActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Hàm kiểm tra mật khẩu có chứa ký tự đặc biệt không
    private boolean isCheckPassword(String password) {
        for (int i = 0; i < password.length(); i++) {
            if ((password.charAt(i) < 'a' || password.charAt(i) > 'z') && (password.charAt(i) < '0' || password.charAt(i) > '9')) {
                return true;  // Nếu có ký tự đặc biệt
            }
        }
        return false;  // Nếu không có ký tự đặc biệt
    }

    // Hàm lưu dữ liệu người dùng vào Firebase
    private void pushData(String email, String hoTen, int cnt) {
        try {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("User");
            User user = new User(email, hoTen);
            String id = String.valueOf(cnt);
            myRef.child(id).setValue(user);  // Lưu thông tin vào Firebase
        } catch (Exception e) {
            e.printStackTrace();  // In ra lỗi nếu có
        }
    }
}

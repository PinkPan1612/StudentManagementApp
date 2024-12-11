package com.example.collegemanager;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.collegemanager.databinding.ActivityLoginBinding;
import com.github.ybq.android.spinkit.style.Circle;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);  // Kích hoạt tính năng giao diện cạnh đến cạnh trên Android
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Xử lý sự kiện click vào nút Đăng nhập
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButtonLogin();
            }
        });

        // Xử lý sự kiện click vào nút Đăng ký
        binding.imgSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSignup();
            }
        });

        // Xử lý sự kiện click vào liên kết Quên mật khẩu
        binding.txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickForgotPassword();
            }
        });

        // Xử lý sự kiện hiển thị hoặc ẩn mật khẩu khi click vào mắt
        binding.imgEyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cnt % 2 == 0) {
                    binding.imgEyes.setImageResource(R.drawable.show);  // Hiển thị mật khẩu
                    binding.edtPassword.setTransformationMethod(null);  // Mật khẩu không mã hóa
                } else {
                    binding.imgEyes.setImageResource(R.drawable.hide);  // Ẩn mật khẩu
                    binding.edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());  // Mã hóa mật khẩu
                }
                cnt++;
            }
        });

        // Thay đổi màu chữ khi EditText tài khoản có hoặc mất focus
        binding.edtAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    binding.edtAccount.setTextColor(Color.parseColor("#000000"));  // Màu đen khi có focus
                } else {
                    binding.edtAccount.setTextColor(Color.parseColor("#aaaaaa"));  // Màu xám khi mất focus
                }
            }
        });

        // Thay đổi màu chữ và màu mắt khi EditText mật khẩu có hoặc mất focus
        binding.edtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    binding.edtPassword.setTextColor(Color.parseColor("#000000"));  // Màu đen khi có focus
                    binding.imgEyes.setColorFilter(ContextCompat.getColor(LoginActivity.this, R.color.black), PorterDuff.Mode.SRC_IN);
                } else {
                    binding.edtPassword.setTextColor(Color.parseColor("#aaaaaa"));  // Màu xám khi mất focus
                    binding.imgEyes.setColorFilter(ContextCompat.getColor(LoginActivity.this, R.color.gray), PorterDuff.Mode.SRC_IN);
                }
            }
        });
    }

    // Xử lý khi người dùng nhấn nút đăng nhập
    private void onClickButtonLogin() {
        String email = binding.edtAccount.getText().toString().trim();  // Lấy email từ EditText
        String password = binding.edtPassword.getText().toString().trim();  // Lấy mật khẩu từ EditText

        // Kiểm tra tính hợp lệ của email và mật khẩu
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(LoginActivity.this, "Bạn chưa nhập tài khoản", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Bạn chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra định dạng email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Không đúng định dạng email", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra độ dài mật khẩu
        if (password.length() < 8) {
            Toast.makeText(LoginActivity.this, "Mật khẩu phải lớn hơn 8 kí tự", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra mật khẩu có chứa ít nhất một ký tự đặc biệt
        if (!isCheckPassword(password)) {
            Toast.makeText(LoginActivity.this, "Mật khẩu phải chứa ít nhất 1 kí tự đặc biệt", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progress.setVisibility(View.VISIBLE);  // Hiển thị progress bar khi đang đăng nhập

        // Đăng nhập Firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            binding.progress.setVisibility(View.GONE);  // Ẩn progress bar nếu đăng nhập thành công
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);  // Chuyển đến MainActivity
                            Bundle bundle = new Bundle();
                            bundle.putString("email", email);
                            intent.putExtra("user", bundle);
                            startActivity(intent);
                            finishAffinity();  // Kết thúc tất cả các Activity trước đó
                        } else {
                            binding.progress.setVisibility(View.GONE);  // Ẩn progress bar nếu đăng nhập thất bại
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Xử lý khi người dùng nhấn vào nút Đăng ký
    private void onClickSignup() {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));  // Chuyển đến Activity đăng ký
    }

    // Xử lý khi người dùng nhấn vào quên mật khẩu
    private void onClickForgotPassword() {
        View alertDialog = getLayoutInflater().inflate(R.layout.layout_dialog_password, null);  // Tạo Layout cho Dialog

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);  // Không thể hủy bỏ Dialog khi bấm ngoài
        alert.setView(alertDialog);  // Thiết lập Layout cho Dialog

        AlertDialog dialog = alert.create();
        dialog.show();  // Hiển thị Dialog

        EditText edtEmail = alertDialog.findViewById(R.id.edt_email);  // Lấy EditText để nhập email
        Button btnBack = alertDialog.findViewById(R.id.btnBack);  // Nút quay lại
        Button btnReset = alertDialog.findViewById(R.id.btnReset);  // Nút reset mật khẩu

        // Xử lý sự kiện nhấn nút Quay lại
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();  // Đóng Dialog
            }
        });

        // Xử lý sự kiện nhấn nút Reset mật khẩu
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(alert.getContext(), "Tài khoản email không đúng định dạng", Toast.LENGTH_SHORT).show();
                    return;
                }
                ActionResetPassword(email);  // Gửi yêu cầu reset mật khẩu
                dialog.dismiss();  // Đóng Dialog
            }
        });
    }

    // Gửi yêu cầu reset mật khẩu tới Firebase
    private void ActionResetPassword(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        binding.progress.setVisibility(View.VISIBLE);  // Hiển thị progress bar khi đang xử lý
        binding.progressBar.setIndeterminateDrawable(new FoldingCube());  // Thêm hiệu ứng vòng quay cho progress bar

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                binding.progress.setVisibility(View.GONE);  // Ẩn progress bar khi xử lý xong
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Email gửi để reset mật khẩu", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Lỗi: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Kiểm tra mật khẩu có chứa ít nhất một ký tự đặc biệt
    private boolean isCheckPassword(String password) {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        return pattern.matcher(password).find();
    }
}

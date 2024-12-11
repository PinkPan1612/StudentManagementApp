package com.example.collegemanager;

import android.content.Intent;  // Import Intent để chuyển đổi giữa các Activity
import android.os.Bundle;  // Import Bundle để chuyển dữ liệu giữa các Activity
import android.os.Handler;  // Import Handler để thực hiện tác vụ trì hoãn

import androidx.activity.EdgeToEdge;  // Import để hỗ trợ giao diện edge-to-edge (toàn màn hình)
import androidx.appcompat.app.AppCompatActivity;  // Import AppCompatActivity để sử dụng tính năng của Activity
import androidx.core.graphics.Insets;  // Import Insets để xử lý các phần mở rộng (padding) trên màn hình
import androidx.core.view.ViewCompat;  // Import ViewCompat để tương thích với các yếu tố giao diện
import androidx.core.view.WindowInsetsCompat;  // Import để xử lý insets cho giao diện

import com.google.firebase.auth.FirebaseAuth;  // Import FirebaseAuth để xác thực người dùng
import com.google.firebase.auth.FirebaseUser;  // Import FirebaseUser để truy xuất thông tin người dùng đã đăng nhập

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  // Gọi phương thức của lớp cha để thiết lập Activity
        EdgeToEdge.enable(this);  // Bật tính năng edge-to-edge cho Activity (giao diện toàn màn hình)
        setContentView(R.layout.activity_splash);  // Thiết lập giao diện cho Activity từ layout XML

        // Tạo một Handler để trì hoãn việc chuyển màn hình sang Activity tiếp theo
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();  // Gọi phương thức chuyển sang Activity tiếp theo sau 2 giây
            }
        }, 2000);  // Chờ 2000 ms (2 giây) trước khi gọi phương thức nextActivity
    }

    // Phương thức chuyển sang Activity tiếp theo (LoginActivity hoặc MainActivity)
    private void nextActivity() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  // Lấy người dùng hiện tại từ Firebase

        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        if (user == null) {
            // Nếu người dùng chưa đăng nhập, chuyển đến màn hình đăng nhập
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        } else {
            // Nếu người dùng đã đăng nhập, chuyển đến màn hình chính (MainActivity)
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("email", user.getEmail());  // Lưu email của người dùng vào bundle
            intent.putExtra("user", bundle);  // Truyền bundle vào Intent để chuyển qua Activity mới
            startActivity(intent);
        }
        finish();  // Kết thúc Activity hiện tại (SplashActivity) sau khi chuyển sang Activity mới
    }
}

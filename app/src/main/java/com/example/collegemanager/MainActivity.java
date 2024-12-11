package com.example.collegemanager;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.collegemanager.Fragment.CreateFragment;
import com.example.collegemanager.Fragment.HomeFragment;
import com.example.collegemanager.Fragment.QueryFragment;
import com.example.collegemanager.Fragment.SettingFragment;
import com.example.collegemanager.Fragment.ShowBackground;
import com.example.collegemanager.Fragment.UpdateFragment;
import com.example.collegemanager.Student.student;
import com.example.collegemanager.User.User;
import com.example.collegemanager.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements HomeFragment.ISendData , QueryFragment.IClickItemBackground {

    public ActivityMainBinding binding;
    public static final int FRAGMENT_HOME =0;  // Mã cho HomeFragment
    public static final int FRAGMENT_QUERY =1; // Mã cho QueryFragment
    public static final int FRAGMENT_CREATE =2; // Mã cho CreateFragment
    public static final int FRAGMENT_SETTING =3; // Mã cho SettingFragment

    public static int CURRENT_FRAGMENT = FRAGMENT_HOME; // Biến theo dõi fragment hiện tại

    private String hoTen, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Bật chế độ hiển thị toàn màn hình
        binding = ActivityMainBinding.inflate(getLayoutInflater()); // Tạo layout từ binding
        setContentView(binding.getRoot());

        // Nhận thông tin người dùng từ Bundle khi khởi tạo activity
        Bundle bundle1 = getIntent().getBundleExtra("user");
        Bundle bundle2 = getIntent().getBundleExtra("user");
        Bundle bundle3 = getIntent().getBundleExtra("user");

        // Lấy thông tin email và tên từ Bundle
        if(bundle1 != null) {
            email = bundle1.getString("email");
            hoTen = "NGUYỄN KHÁNH HUY";
        }
        if(bundle2 != null) {
            email = bundle2.getString("email");
            hoTen = bundle2.getString("name");
        }
        if(bundle3 != null) {
            email = bundle3.getString("email");
            hoTen = "NGUYỄN KHÁNH HUY";
        }

        // Gọi phương thức lấy thông tin người dùng từ Firebase
        getValue();

        // Thay thế fragment mặc định là HomeFragment
        replaceFragment(new HomeFragment(), null);

        // Chọn item Home trong navigation bar mặc định
        binding.bottomNav.setItemSelected(R.id.Home, true);

        // Xử lý sự kiện khi người dùng chọn các item trong navigation bar
        onClickBottomNavigation();
    }

    private void onClickBottomNavigation() {
        binding.bottomNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                // Thay đổi fragment khi người dùng chọn các item trong bottom navigation
                if(i == R.id.Home) {
                    if(CURRENT_FRAGMENT != FRAGMENT_HOME) {
                        replaceFragment(new HomeFragment(),null);
                        CURRENT_FRAGMENT = FRAGMENT_HOME;
                    }
                } else if(i == R.id.Query) {
                    if(CURRENT_FRAGMENT != FRAGMENT_QUERY) {
                        replaceFragment(new QueryFragment(),null);
                        CURRENT_FRAGMENT = FRAGMENT_QUERY;
                    }
                } else if(i == R.id.Insert) {
                    if(CURRENT_FRAGMENT != FRAGMENT_CREATE) {
                        replaceFragment(new CreateFragment(),null);
                        CURRENT_FRAGMENT = FRAGMENT_CREATE;
                    }
                } else if(i == R.id.Setting) {
                    if(CURRENT_FRAGMENT != FRAGMENT_SETTING) {
                        replaceFragment(new SettingFragment(),null);
                        CURRENT_FRAGMENT = FRAGMENT_SETTING;
                    }
                }
            }
        });
    }

    // Phương thức thay thế fragment hiện tại
    public void replaceFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(tag == null) {
            fragmentTransaction.replace(R.id.frameLayout, fragment);
        } else {
            fragmentTransaction.replace(R.id.frameLayout, fragment, tag);
        }

        fragmentTransaction.addToBackStack(null); // Thêm vào back stack để có thể quay lại
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        // Nếu đang ở fragment khác Home, quay lại Home
        if(CURRENT_FRAGMENT != FRAGMENT_HOME) {
            replaceFragment(new HomeFragment(), null);
            binding.bottomNav.setItemSelected(R.id.Home, true);
        }
        if(CURRENT_FRAGMENT == FRAGMENT_HOME) {
            super.onBackPressed();
        }
    }

    // Gửi dữ liệu student từ HomeFragment đến UpdateFragment
    @Override
    public void senData(student student) {
        UpdateFragment fragment = (UpdateFragment) getSupportFragmentManager().findFragmentByTag("UPDATE_FRAGMENT_TAG");
        if (fragment != null) {
            fragment.receiveDataFromFragmentHome(student);
        }
    }

    // Gửi dữ liệu student từ QueryFragment đến ShowBackground
    @Override
    public void sendData(student student) {
        ShowBackground fragment = (ShowBackground) getSupportFragmentManager().findFragmentByTag("SHOW_FRAGMENT_TAG");
        if (fragment != null) {
            fragment.showInfor(student);
        }
    }

    // Phương thức getter cho hoTen và email
    public String getHoTen() {
        return hoTen;
    }

    public String getEmail() {
        return email;
    }

    // Lấy giá trị người dùng từ Firebase
    private void getValue() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("User");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> users = new ArrayList<>();
                for(DataSnapshot snapshot1: snapshot.getChildren()) {
                    User user = snapshot1.getValue(User.class);
                    users.add(user);
                }

                // Cập nhật tên người dùng nếu email khớp
                for(User user : users) {
                    if(email.equals(user.getEmail())) {
                        hoTen = user.getName();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Chưa cập nhật tên người dùng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.example.collegemanager.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegemanager.IClickItembackground;
import com.example.collegemanager.MainActivity;
import com.example.collegemanager.R;
import com.example.collegemanager.Student.QueryAdapter;
import com.example.collegemanager.Student.StudentDatabase;
import com.example.collegemanager.Student.student;
import com.example.collegemanager.databinding.QueryFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class QueryFragment extends Fragment {
    private QueryFragmentBinding binding;  // Biến để làm việc với View Binding
    private QueryAdapter adapter;  // Adapter để gán dữ liệu sinh viên vào RecyclerView
    private List<student> myList;  // Danh sách lưu trữ dữ liệu sinh viên từ cơ sở dữ liệu
    private MainActivity mMainActivity;  // Tham chiếu đến MainActivity
    public IClickItemBackground iClickItemBackground;  // Giao diện để giao tiếp với MainActivity

    // Giao diện để xử lý sự kiện khi click vào mục sinh viên
    public interface IClickItemBackground {
        void sendData(student student);  // Phương thức gửi dữ liệu sinh viên
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Liên kết layout với fragment thông qua ViewBinding
        binding = QueryFragmentBinding.inflate(inflater, container, false);

        myList = new ArrayList<>();  // Khởi tạo danh sách sinh viên
        adapter = new QueryAdapter(new IClickItembackground() {
            @Override
            public void actionIntent(student student) {
                // Khi click vào một sinh viên, thay thế fragment hiển thị chi tiết
                MainActivity mainActivity = (MainActivity)getActivity();
                assert mainActivity != null;
                ShowBackground fragment = ShowBackground.getInstance(student);  // Tạo fragment hiển thị chi tiết sinh viên
                mainActivity.replaceFragment(fragment, "SHOW_FRAGMENT_TAG");  // Thay thế fragment
                iClickItemBackground.sendData(student);  // Gửi dữ liệu sinh viên qua giao diện
            }
        });

        mMainActivity =(MainActivity) getActivity();  // Lấy tham chiếu đến MainActivity
        showItemInList();  // Hiển thị danh sách sinh viên

        // Xử lý sự kiện khi nhấn nút "back"
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainActivity.replaceFragment(new HomeFragment(), null);  // Quay lại fragment Home
                mMainActivity.binding.bottomNav.setItemSelected(R.id.Home, true);  // Đặt tab Home được chọn
            }
        });

        return binding.getRoot();  // Trả về root view của fragment
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Gán giao diện IClickItemBackground khi fragment được đính kèm vào activity
        iClickItemBackground = (IClickItemBackground) getActivity();
    }

    // Phương thức hiển thị danh sách sinh viên lên RecyclerView
    private void showItemInList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);  // Định dạng cho RecyclerView
        binding.rcvList.setLayoutManager(linearLayoutManager);  // Gán LayoutManager cho RecyclerView
        myList = StudentDatabase.getInstanceStudent(getContext()).studentDao().orderSort();  // Lấy danh sách sinh viên đã sắp xếp từ cơ sở dữ liệu
        adapter.loadData(myList);  // Gán dữ liệu cho adapter
        binding.rcvList.setAdapter(adapter);  // Gán adapter vào RecyclerView
    }
}

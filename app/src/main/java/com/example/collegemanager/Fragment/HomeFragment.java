package com.example.collegemanager.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegemanager.IClickItem;
import com.example.collegemanager.MainActivity;
import com.example.collegemanager.R;
import com.example.collegemanager.Student.StudentAdapter;
import com.example.collegemanager.Student.StudentDatabase;
import com.example.collegemanager.databinding.HomeFragmentBinding;

import java.util.ArrayList;
import java.util.List;

import com.example.collegemanager.Student.student;

public class HomeFragment extends Fragment {

    private HomeFragmentBinding binding; // Binding cho layout HomeFragment
    private StudentAdapter adapter; // Adapter để quản lý danh sách hiển thị trên RecyclerView
    private List<student> myList; // Danh sách các đối tượng sinh viên
    public ISendData iSendData; // Interface để gửi dữ liệu giữa các Fragment
    private MainActivity mainActivity; // Tham chiếu đến MainActivity

    // Interface để gửi dữ liệu đến Fragment khác
    public interface ISendData {
        void senData(student student); // Phương thức gửi dữ liệu
    }

    // Kết nối giao diện Fragment với HomeFragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HomeFragmentBinding.inflate(inflater, container, false); // Khởi tạo binding

        autoTextViewSearch(); // Cài đặt AutoCompleteTextView
        myList = new ArrayList<>(); // Khởi tạo danh sách sinh viên
        mainActivity = (MainActivity) getActivity(); // Gán MainActivity cho biến mainActivity
        adapter = new StudentAdapter(new IClickItem() { // Gắn adapter cho RecyclerView
            @Override
            public void actionIntent(student student) {
                // Thực hiện chuyển sang UpdateFragment để chỉnh sửa thông tin sinh viên
                assert mainActivity != null;
                UpdateFragment fragment = UpdateFragment.newInstance(student);
                mainActivity.replaceFragment(fragment, "UPDATE_FRAGMENT_TAG");
                iSendData.senData(student); // Gửi dữ liệu qua interface
            }

            @Override
            public void showDialogDelete(student student) {
                // Hiển thị hộp thoại xác nhận xóa sinh viên
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle("Xóa học sinh")
                        .setMessage("Bạn chắc chắn muốn xóa học sinh này?")
                        .setCancelable(false)
                        .setNegativeButton("Quay lại", null)
                        .setPositiveButton("Tiếp tục", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Xóa sinh viên khỏi cơ sở dữ liệu và cập nhật danh sách
                                StudentDatabase.getInstanceStudent(getContext()).studentDao().Delete(student);
                                Toast.makeText(getContext(), "Xóa Học sinh thành công!", Toast.LENGTH_SHORT).show();
                                loadData(); // Làm mới danh sách
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // Gắn sự kiện tìm kiếm bằng nút Enter
        binding.autoCompleteSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchStudent(); // Gọi hàm tìm kiếm
                }
                return true;
            }
        });

        showItemOnRecyclerView(); // Hiển thị danh sách trên RecyclerView
        binding.txtName.setText(mainActivity.getHoTen()); // Hiển thị tên từ MainActivity
        return binding.getRoot(); // Trả về giao diện
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iSendData = (ISendData) getActivity(); // Gắn interface iSendData với Activity
    }

    // Cài đặt AutoCompleteTextView
    private void autoTextViewSearch() {
        String[] arrays = getResources().getStringArray(R.array.automatic); // Lấy dữ liệu từ resource
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.layout_custom_complete, R.id.txtComplete, arrays);
        binding.autoCompleteSearch.setAdapter(adapter); // Gắn adapter cho AutoCompleteTextView
    }

    // Hiển thị danh sách sinh viên trên RecyclerView
    private void showItemOnRecyclerView() {
        myList = StudentDatabase.getInstanceStudent(getContext()).studentDao().getList(); // Lấy danh sách từ cơ sở dữ liệu
        adapter.setData(myList); // Gán dữ liệu vào adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false); // Layout ngang
        binding.rcvItems.setLayoutManager(linearLayoutManager); // Gán layout cho RecyclerView
        binding.rcvItems.setAdapter(adapter); // Gắn adapter cho RecyclerView
    }

    // Tìm kiếm sinh viên dựa trên các tiêu chí
    private void searchStudent() {
        String student = binding.autoCompleteSearch.getText().toString().trim(); // Lấy nội dung tìm kiếm
        List<student> lst = new ArrayList<>();
        if (TextUtils.isEmpty(student)) { // Kiểm tra trường hợp rỗng
            Toast.makeText(getContext(), "Mời nhập thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tìm kiếm theo các tiêu chí cụ thể
        if (student.equals("Học sinh có điểm cao nhất")) {
            myList = StudentDatabase.getInstanceStudent(getContext()).studentDao().orderSort(); // Lấy danh sách sắp xếp theo điểm giảm dần
            lst.add(myList.get(0)); // Lấy sinh viên có điểm cao nhất
            adapter.setData(lst);
            hideSoftKeyboard(); // Ẩn bàn phím
        } else if (student.equals("Học sinh có điểm thấp nhất")) {
            myList = StudentDatabase.getInstanceStudent(getContext()).studentDao().orderSortReverse(); // Lấy danh sách sắp xếp theo điểm tăng dần
            lst.add(myList.get(0)); // Lấy sinh viên có điểm thấp nhất
            adapter.setData(lst);
            hideSoftKeyboard();
        } else {
            myList = StudentDatabase.getInstanceStudent(getContext()).studentDao().search(student); // Tìm kiếm theo từ khóa
            adapter.setData(myList);
            hideSoftKeyboard();
        }
    }

    // Ẩn bàn phím khi tìm kiếm xong
    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
        }
    }

    // Làm mới danh sách sinh viên
    private void loadData() {
        myList = StudentDatabase.getInstanceStudent(getContext()).studentDao().getList(); // Lấy danh sách từ cơ sở dữ liệu
        adapter.setData(myList); // Gán lại dữ liệu cho adapter
    }

}

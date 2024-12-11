package com.example.collegemanager.Student;

import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegemanager.IClickItem;
import com.example.collegemanager.R;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private List<student> lst;  // Danh sách các sinh viên
    private final IClickItem iClickItem;  // Interface để xử lý các hành động khi click vào item

    // Phương thức tạo ViewHolder từ layout của item
    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Lấy LayoutInflater để chuyển đổi layout XML thành đối tượng View
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item, parent, false);
        return new StudentViewHolder(view);  // Trả về ViewHolder với view đã được tạo
    }

    // Constructor của StudentAdapter nhận vào một đối tượng implement IClickItem
    public StudentAdapter(IClickItem iClickItem) {
        this.iClickItem = iClickItem;  // Gán đối tượng IClickItem vào biến iClickItem
    }

    // Phương thức gán dữ liệu vào các View trong ViewHolder
    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        student student = lst.get(position);  // Lấy đối tượng student tại vị trí hiện tại
        if(student == null) {
            return;  // Nếu student null thì không thực hiện gì thêm
        }

        // Gán dữ liệu cho các TextView trong ViewHolder
        holder.txtName.setText(student.getHoTen());
        holder.txtMSV.setText(student.getMaSV());

        // Đặt sự kiện click cho biểu tượng dấu ba chấm (ImageButton)
        holder.imgBtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo một PopupMenu với các tùy chọn
                PopupMenu popupMenu = new PopupMenu(v.getContext(), holder.imgBtnMore);
                popupMenu.inflate(R.menu.option);  // Nạp các mục trong menu từ resource "option"
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Kiểm tra xem item nào trong menu được click và thực hiện hành động tương ứng
                        if(item.getItemId() == R.id.update) {
                            iClickItem.actionIntent(student);  // Thực hiện hành động cập nhật thông tin sinh viên
                            return true;
                        }
                        if(item.getItemId() == R.id.delete) {
                            iClickItem.showDialogDelete(student);  // Hiển thị dialog để xác nhận xóa sinh viên
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();  // Hiển thị PopupMenu
            }
        });

        // Đặt sự kiện click cho itemView (toàn bộ phần tử trong RecyclerView)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị thông báo khi người dùng click vào item
                Toast.makeText(v.getContext(), "Click vào biểu tượng dấu ba chấm để chọn chức năng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Phương thức trả về số lượng item trong danh sách
    @Override
    public int getItemCount() {
        if(lst != null){
            return lst.size();  // Trả về kích thước của danh sách sinh viên
        }
        return 0;  // Nếu danh sách null thì trả về 0
    }

    // Phương thức để cập nhật dữ liệu danh sách sinh viên vào adapter
    public void setData(List<student> myList) {
        this.lst = myList;  // Gán danh sách mới vào biến lst
        notifyDataSetChanged();  // Thông báo cho adapter biết dữ liệu đã thay đổi
    }

    // Lớp ViewHolder giữ các tham chiếu đến các view trong mỗi item của RecyclerView
    public class StudentViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgBtnMore;  // Biểu tượng dấu ba chấm
        private TextView txtName, txtMSV;  // Các TextView để hiển thị tên sinh viên và mã sinh viên

        // Constructor của StudentViewHolder để ánh xạ các view từ layout
        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBtnMore = itemView.findViewById(R.id.imgBtnMore);  // Lấy ImageView cho biểu tượng dấu ba chấm
            txtName = itemView.findViewById(R.id.txtHoten);  // Lấy TextView cho tên sinh viên
            txtMSV = itemView.findViewById(R.id.txtMSV);  // Lấy TextView cho mã sinh viên
        }
    }
}

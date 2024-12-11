package com.example.collegemanager.Student;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegemanager.IClickItembackground;
import com.example.collegemanager.R;

import java.util.List;

public class QueryAdapter extends RecyclerView.Adapter<QueryAdapter.QueryViewHolder> {
    private List<student> lst;  // Danh sách sinh viên cần hiển thị
    private IClickItembackground iClickItembackground;  // Interface để xử lý sự kiện khi click vào một item

    // Constructor nhận vào đối tượng IClickItembackground để xử lý sự kiện
    public QueryAdapter(IClickItembackground iClickItembackground) {
        this.iClickItembackground = iClickItembackground;
    }

    // Tạo view holder cho mỗi item trong RecyclerView
    @NonNull
    @Override
    public QueryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo mới view từ layout_item_list và trả về một QueryViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_list, parent, false);
        return new QueryViewHolder(view);
    }

    // Liên kết dữ liệu với các view trong mỗi item
    @Override
    public void onBindViewHolder(@NonNull QueryViewHolder holder, int position) {
        student student = lst.get(position);  // Lấy đối tượng sinh viên tại vị trí position
        if (student == null) {
            return;
        }

        // Hiển thị thông tin của sinh viên lên các TextView
        holder.txtTen.setText(student.getHoTen());
        holder.txtToan.setText(String.valueOf(student.getDiemToan()));
        holder.txtVan.setText(String.valueOf(student.getDiemVan()));
        holder.txtAnh.setText(String.valueOf(student.getDiemAnh()));

        // Xử lý sự kiện khi click vào item trong RecyclerView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItembackground.actionIntent(student);  // Gọi phương thức actionIntent từ interface để xử lý sự kiện click
            }
        });
    }

    // Trả về số lượng item trong RecyclerView
    @Override
    public int getItemCount() {
        if (lst == null) {
            return 0;  // Nếu danh sách rỗng, trả về 0
        }
        return lst.size();  // Trả về số lượng phần tử trong danh sách
    }

    // Phương thức dùng để load lại dữ liệu vào adapter
    public void loadData(List<student> users) {
        this.lst = users;  // Gán dữ liệu vào danh sách sinh viên
    }

    // ViewHolder dùng để quản lý các view của mỗi item trong RecyclerView
    public class QueryViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTen, txtToan, txtVan, txtAnh;

        // Constructor khởi tạo view holder với các view cần thiết
        public QueryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTen = itemView.findViewById(R.id.txtTen);  // Tìm kiếm TextView hiển thị tên sinh viên
            txtToan = itemView.findViewById(R.id.txtToan);  // Tìm kiếm TextView hiển thị điểm Toán
            txtVan = itemView.findViewById(R.id.txtVan);  // Tìm kiếm TextView hiển thị điểm Văn
            txtAnh = itemView.findViewById(R.id.txtAnh);  // Tìm kiếm TextView hiển thị điểm Anh
        }
    }
}

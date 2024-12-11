import java.io.OutputStream;

public class shutdow {
    public static boolean shutdownDevice() {
        try {
            // Yêu cầu quyền root
            Process process = Runtime.getRuntime().exec("su");
            OutputStream os = process.getOutputStream();
            // Lệnh tắt nguồn
            os.write("reboot -p\n".getBytes());
            os.flush();
            os.close();
            process.waitFor();
            return true; // Tắt nguồn thành công
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Thất bại
        }
    }
}

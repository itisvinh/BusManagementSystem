package Server;
import Entity.*;
import java.util.List;
import java.util.ArrayList;
public interface InterfaceXuLy {
    //TaiKhoan
    public  boolean DangNhap(String user, String pass) throws Exception;
    public Account ThongTin(String taikhoan) throws Exception;

    public List<Bus> DanhSachBus() throws Exception;
    public boolean ThemBus() throws Exception;
    public boolean XoaBus() throws Exception;
    public boolean LuuBus() throws Exception;

    public List<LopGhe> ListSeat(int idBus) throws Exception;
    public ArrayList<Integer> DanhSachGheDaDat(int idBus) throws Exception;
    public int soHang(int idBus) throws Exception;
    public int soCot(int idBus) throws Exception;

    public List<Schedule> DanhSachLich(int idBus, int idDriver) throws Exception;

    public List<Ticket> DanhSachVe(String idKH) throws Exception;
    public void DatVe(String idKH, int idLich, int idGhe) throws Exception;
}

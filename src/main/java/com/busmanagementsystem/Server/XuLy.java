package Server;
import Entity.*;

import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class XuLy implements InterfaceXuLy {
    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet;

    public XuLy() throws Exception{

    }


    @Override
    public boolean DangNhap(String user, String pass) throws Exception {
        return false;
    }

    @Override
    public Account ThongTin(String taikhoan) throws Exception {
        return new LopTaiKhoan().thongTin(taikhoan);
    }

    @Override
    public List<Bus> DanhSachBus() throws Exception {
        return new LopBus().DanhSachBus();
    }

    @Override
    public boolean ThemBus() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean XoaBus() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean LuuBus() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<LopGhe> ListSeat(int idBus) throws Exception {
        return new LopGhe().ListSeat(idBus);
    }

    @Override
    public ArrayList<Integer> DanhSachGheDaDat(int idBus) throws Exception {
        return new LopGhe().DanhSachGheDaDat(idBus);
    }

    @Override
    public int soHang(int idBus) throws Exception {
        return new LopGhe().soHang(idBus);
    }

    @Override
    public int soCot(int idBus) throws Exception {
        return new LopGhe().soHang(idBus);
    }

    @Override
    public List<Schedule> DanhSachLich(int idBus, int idDriver) throws Exception {
        return new LopLich().DanhSachLich(idBus, idDriver);
    }

    @Override
    public List<Ticket> DanhSachVe(String idKH) throws Exception {
        return new LopVe().DanhSachVe(idKH);
    }

    @Override
    public void DatVe(String idKH, int idLich, int idGhe) throws Exception {
        new LopVe().DatVe(idKH, idLich, idGhe);
    }
}

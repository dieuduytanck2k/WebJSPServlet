package controller;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import database.KhachHangDAO;
import model.KhachHang;
import util.Email;
import util.MaHoa;
import util.SoNgauNhien;

/**
 * Servlet implementation class KhachHang
 */
@WebServlet("/khach-hang")
public class KhachHangController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public KhachHangController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		String hanhDong = request.getParameter("hanhDong");
		if(hanhDong.equals("dang-nhap")) {
			dangNhap(request, response);
		}else if(hanhDong.equals("dang-xuat")) {
			dangXuat(request, response);
		}else if(hanhDong.equals("dang-ky")) {
			dangKy(request, response);
		}else if(hanhDong.equals("doi-mat-khau")) {
			doiMatKhau(request, response);
		}else if(hanhDong.equals("thay-doi-thong-tin")) {
			thayDoiThongTin(request, response);
		}else if(hanhDong.equals("xac-thuc")) {
			xacThuc(request, response);
		}
	}
	
	private void thayDoiThongTin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String hoVaTen = request.getParameter("hoVaTen");
		String gioiTinh = request.getParameter("gioiTinh");
		String ngaySinh = request.getParameter("ngaySinh");
		String diaChiKhachHang = request.getParameter("diaChiKhachHang");
		String diaChiMuaHang = request.getParameter("diaChiMuaHang");
		String diaChiNhanHang = request.getParameter("diaChiNhanHang");
		String dienThoai = request.getParameter("dienThoai");
		String email = request.getParameter("email");
		String dongYNhanMail = request.getParameter("dongYNhanMail");
		request.setAttribute("hoVaTen", hoVaTen);
		request.setAttribute("gioiTinh", gioiTinh);
		request.setAttribute("ngaySinh", ngaySinh);
		request.setAttribute("diaChiKhachHang", diaChiKhachHang);
		request.setAttribute("diaChiMuaHang", diaChiMuaHang);
		request.setAttribute("diaChiNhanHang", diaChiNhanHang);
		request.setAttribute("dienThoai", dienThoai);
		request.setAttribute("email", email);
		request.setAttribute("dongYNhanMail", dongYNhanMail);
		
		String url = "";
		String baoLoi = "";
		KhachHangDAO khachHangDAO = new KhachHangDAO();
		
		if(baoLoi.length()>0) {
			url = "/khachhang/dangky.jsp";
		}else {
			Object obj = request.getSession().getAttribute("khachHang");
			KhachHang khachHang = null;
			if(obj!=null) {
				khachHang = (KhachHang)obj;
				if(khachHang!=null) {
					// lấy mã khách hàng trong session
					String maKhachHang = khachHang.getMaKhachHang();
					KhachHang kh = new KhachHang(maKhachHang, "", "", hoVaTen, gioiTinh, diaChiKhachHang, diaChiNhanHang, diaChiMuaHang, Date.valueOf(ngaySinh), dienThoai, email, dongYNhanMail!=null);
					//update khách hàng trong database
					khachHangDAO.updateInfor(kh);
					// truyền khách hàng đã update lên session hiện tại
					KhachHang kh2 = khachHangDAO.selectById(kh);
					request.getSession().setAttribute("khachHang", kh2);
					baoLoi="Lưu thông tin thành công!";
					url="/khachhang/thaydoithongtin.jsp";
				}
			}
			
		}
		request.setAttribute("baoLoi", baoLoi);
		RequestDispatcher rd = getServletContext().getRequestDispatcher(url);
		rd.forward(request, response);
	}

	private void doiMatKhau(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String matKhauHienTai = request.getParameter("matKhauHienTai");
		String matKhauMoi = request.getParameter("matKhauMoi");
		String matKhauMoiNhapLai = request.getParameter("matKhauMoiNhapLai");
		
		String matKhauHienTai_Sha1 = MaHoa.toSHA1(matKhauHienTai);
		
		String baoLoi = "";
		String url = "/khachhang/doimatkhau.jsp";
		String chuyenTrang ="0";
		
		// Kiểm tra mật khẩu cũ có giống mật khẩu mới hay không
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("khachHang");
		KhachHang khachHang = null;
		if(obj!=null) {
			khachHang = (KhachHang) obj;
		}
		// Kiểm tra xem khách hàng đã đăng nhập hay chưa, sai nếu sai thì báo lỗi
		if(khachHang==null) {
			baoLoi = "Bạn chưa đăng nhập vào hệ thống!";
		}else {
			// Kiểm tra mật khẩu hiện tại, nếu sai thì báo lỗi
			if(!matKhauHienTai_Sha1.equals(khachHang.getMatKhau())) {
				baoLoi = "Mật khẩu hiện tại không chính xác!";
			}else {
				String matKhauMoi_Sha1 = MaHoa.toSHA1(matKhauMoi);
				// Kiểm tra mật khẩu mới và mật khẩu mới nhập lại, nếu sai thì báo lỗi
				if(!matKhauMoi.equals(matKhauMoiNhapLai)) {
					baoLoi = "Mật khẩu nhập lại không khớp!";
				}else if(matKhauMoi_Sha1.equals(khachHang.getMatKhau())) {
					baoLoi = "Mật khẩu mới bị trùng mật khẩu cũ";
				// Nếu đúng thì đưa mật khẩu mới vào database, đưa vào thành công hay thất bại đều có thông báo
				}else {
					khachHang.setMatKhau(matKhauMoi_Sha1);
					KhachHangDAO khd = new KhachHangDAO();
					
					if(khd.changePassword(khachHang)) {
						request.setAttribute("next", "1");
						baoLoi = "Mật khẩu đã thay đổi thành công! Bạn sẽ trở lại trang chủ sau 5 giây";
						response.setHeader("Refresh", "5; URL=index.jsp");
						session.invalidate();
					}else {
						baoLoi = "Quá trình thay đổi mật khẩu không thành công!";
					}
				}
			}
		}
		
		request.setAttribute("baoLoi", baoLoi);
		
		RequestDispatcher rd = getServletContext().getRequestDispatcher(url);
		
		rd.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void dangNhap(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tenDangNhap = request.getParameter("tenDangNhap");
		String matKhau = request.getParameter("matKhau");
		matKhau = MaHoa.toSHA1(matKhau);
		
		KhachHang kh = new KhachHang();
		kh.setTenDangNhap(tenDangNhap);
		kh.setMatKhau(matKhau);
		
		KhachHangDAO khd = new KhachHangDAO();
		KhachHang khachHang = khd.selectByUsernameAndPassword(kh);
		String url = "";
		if(khachHang!=null && khachHang.isTrangThaiXacThuc()) {
			HttpSession session = request.getSession();
			session.setAttribute("khachHang", khachHang);
			url = "/index.jsp";
		}else {
			request.setAttribute("baoLoi", "Tên đăng nhập hoặc mật khẩu không đúng hoặc tài khoản chưa xác thực");
			url = "/khachhang/dangnhap.jsp";
		}
		RequestDispatcher rd = getServletContext().getRequestDispatcher(url);
		rd.forward(request, response);
	}
	
	private void dangXuat(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.invalidate();
		
		String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		
		response.sendRedirect(url+"/index.jsp");
	}
	private void dangKy(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tenDangNhap = request.getParameter("tenDangNhap");
		String matKhau = request.getParameter("matKhau");
		String matKhauNhapLai = request.getParameter("matKhauNhapLai");
		String hoVaTen = request.getParameter("hoVaTen");
		String gioiTinh = request.getParameter("gioiTinh");
		String ngaySinh = request.getParameter("ngaySinh");
		String diaChiKhachHang = request.getParameter("diaChiKhachHang");
		String diaChiMuaHang = request.getParameter("diaChiMuaHang");
		String diaChiNhanHang = request.getParameter("diaChiNhanHang");
		String dienThoai = request.getParameter("dienThoai");
		String email = request.getParameter("email");
		String dongYNhanMail = request.getParameter("dongYNhanMail");
		request.setAttribute("tenDangNhap", tenDangNhap);		
		request.setAttribute("hoVaTen", hoVaTen);
		request.setAttribute("gioiTinh", gioiTinh);
		request.setAttribute("ngaySinh", ngaySinh);
		request.setAttribute("diaChiKhachHang", diaChiKhachHang);
		request.setAttribute("diaChiMuaHang", diaChiMuaHang);
		request.setAttribute("diaChiNhanHang", diaChiNhanHang);
		request.setAttribute("dienThoai", dienThoai);
		request.setAttribute("dongYNhanMail", dongYNhanMail);
		
		String url = "";
		
		String baoLoi = "";
		KhachHangDAO khachHangDAO = new KhachHangDAO();

		if(khachHangDAO.kiemTraTenDangNhap(tenDangNhap)) {
			baoLoi +="Tên đăng nhập đã tồn tại, vui lòng chọn tên đăng nhập khác.<br/>";
		}
		
		if(!matKhau.equals(matKhauNhapLai)) {
			baoLoi +="Mẫu khẩu không khớp.<br/>";
		}else {
			matKhau = MaHoa.toSHA1(matKhau);
		}
		
		request.setAttribute("baoLoi", baoLoi);
		
		
		if(baoLoi.length()>0) {
			url = "/khachhang/dangky.jsp";
		}else {
			Random rd = new Random();
			String maKhachHang = System.currentTimeMillis() + rd.nextInt(1000) +"";
			KhachHang kh = new KhachHang(maKhachHang, tenDangNhap, matKhau, hoVaTen, gioiTinh, diaChiKhachHang, diaChiNhanHang, diaChiMuaHang, Date.valueOf(ngaySinh), dienThoai, email, dongYNhanMail!=null);
			if(khachHangDAO.insert(kh)>0) {
				// Dãy số xác thực
				String soNgauNhien = SoNgauNhien.getSoNgauNhien();
				
				// Quy định thời gian hiệu lực
				Date todaysDate = new Date(new java.util.Date().getTime());
				Calendar c = Calendar.getInstance();
				c.setTime(todaysDate);
				c.add(Calendar.DATE, 1);
				Date thoiGianHieuLucXacThuc = new Date(c.getTimeInMillis());
				
				// Trạng thái xác thực = false
				boolean trangThaiXacThuc = false;
				kh.setMaXacThuc(soNgauNhien);
				kh.setThoiGianHieuLucCuaMaXacThuc(thoiGianHieuLucXacThuc);
				kh.setTrangThaiXacThuc(trangThaiXacThuc);
				
				if(khachHangDAO.updateVerifyInformation(kh)>0) {
					// Gửi email cho khách hàng
					Email.sendEmail(kh.getEmail(), "Xác thực tài khoản tại TITV.vn", getNoiDung(kh));
				}
			}
			url = "/khachhang/thanhcong.jsp";
		}
		RequestDispatcher rd = getServletContext().getRequestDispatcher(url);
		rd.forward(request, response);
	}
	
	private void xacThuc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String maKhachHang = request.getParameter("maKhachHang");
		String maXacThuc = request.getParameter("maXacThuc");
		
		KhachHangDAO khachHangDAO = new KhachHangDAO();
		KhachHang kh = new KhachHang();
		kh.setMaKhachHang(maKhachHang);
		KhachHang khachHang = khachHangDAO.selectById(kh);
		String msg = "";
		
		if(khachHang != null) {
			// Kiểm tra mã xác thực có giống nhau hay không // Kiểm tra xem mã xác thực còn hiệu lực hay không
			if(khachHang.getMaXacThuc().equals(maXacThuc)) {
				// Thành công
				khachHang.setTrangThaiXacThuc(true);
				khachHangDAO.updateVerifyInformation(khachHang);
				msg = "Xác thực thành công!";
			}else {
				// Thất bại
				msg = "Xác thực không thành công!";
			}
		}else {
			msg = "Tài khoản không tồn tại!";
		}
		String url = "/khachhang/thongbao.jsp";
		request.setAttribute("baoLoi", msg);
		RequestDispatcher rd = getServletContext().getRequestDispatcher(url);
		rd.forward(request, response);
	}
	
	public static String getNoiDung(KhachHang kh) {
		String link = "http://localhost:8080/Bai4_Boostrap2/khach-hang?hanhDong=xac-thuc&maKhachHang="+kh.getMaKhachHang()+"&maXacThuc="+kh.getMaXacThuc();
		String noiDung ="<p>Xin ch&agrave;o b&#7841;n<strong> "+kh.getHoVaTen()+" </strong>,</p>\r\n"
				+ "<p>Vui l&ograve;ng x&aacute;c th&#7921;c t&agrave;i kho&#7843;n c&#7911;a b&#7841;n b&#7857;ng c&aacute;ch nh&#7853;p m&atilde; <strong>"+ kh.getMaXacThuc() +" </strong>, Ho&#7863;c click tr&#7921;c ti&#7871;p b&#7857;ng &#273;&#432;&#7901;ng link sau &#273;&acirc;y</p>\r\n"
				+ "<p>&nbsp;</p>\r\n"
				+ "<p><a href=\""+link+"\">"+link+"</a></p>\r\n"
				+ "<p>&#272;&acirc;y l&agrave; email t&#7921; &#273;&#7897;ng, tui l&ograve;ng kh&ocirc;ng ph&#7843;n h&#7891;i email n&agrave;y</p>\r\n"
				+ "<p>Tr&acirc;n tr&#7885;ng c&#7843;m &#417;n</p>";
		return noiDung;
	}
}

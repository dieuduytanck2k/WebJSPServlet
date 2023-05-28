package controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import database.KhachHangDAO;
import model.KhachHang;

/**
 * Servlet implementation class KhachHangThayDoiAnh
 */
@WebServlet("/khach-hang-thay-doi-anh")
public class KhachHangThayDoiAnh extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public KhachHangThayDoiAnh() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Object obj = request.getSession().getAttribute("khachHang");
		KhachHang khachHang = null;
		if (obj != null) {
			khachHang = (KhachHang) obj;
			if (khachHang != null) {
				try {
					String folder = getServletContext().getRealPath("avatar");
					System.out.println(folder);
					File file;
					int maxFileSize = 5000 * 1024;
					int maxMemSize = 5000 * 1024;

					String contentType = request.getContentType();

					if (contentType.indexOf(contentType) >= 0) {
						DiskFileItemFactory factory = new DiskFileItemFactory();

						// Quy định dung lượng tối đa cho 1 file
						factory.setSizeThreshold(maxMemSize);

						// Tạo file upload
						ServletFileUpload upload = new ServletFileUpload(factory);

						upload.setSizeMax(maxFileSize);

						List<FileItem> files = upload.parseRequest(request);
						
						for (FileItem fileItem : files) {
							if(!fileItem.isFormField()) {
								String fileName = System.currentTimeMillis()+fileItem.getName();
								String path = folder+"\\"+fileName;
								
								// Tạo ra file để lưu đường dẫn
								file = new File(path);
								
								// Lưu nội dung xuống file
								fileItem.write(file);
								
								khachHang.setDuongDanAnh(fileName);
								KhachHangDAO khachHangDAO = new KhachHangDAO();
								khachHangDAO.updateImage(khachHang);
								khachHang = khachHangDAO.selectById(khachHang);
							}else {
								// Trường thông tin
								String name = fileItem.getFieldName();
								String value = fileItem.getString();
								System.out.println(name+" : "+value);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
	}

}

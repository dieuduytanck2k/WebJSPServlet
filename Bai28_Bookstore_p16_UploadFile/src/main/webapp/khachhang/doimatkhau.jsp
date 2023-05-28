<%@page import="model.KhachHang"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT"
	crossorigin="anonymous">
<script
	src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"
	integrity="sha384-oBqDVmMz9ATKxIep9tiCxS/Z9fNfEXiDAYTujMAeBAsjFuCZSmKbSSUnQlmh/jp3"
	crossorigin="anonymous"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/js/bootstrap.min.js"
	integrity="sha384-7VPbUDkoPSGFnVtYi0QogXtr74QeVeeIs99Qfg5YCF+TidwNdjvaKZX19NZ/e6oz"
	crossorigin="anonymous"></script>
<style>
	.red{
		color: red;
	}
</style>
</head>
<body>
	<jsp:include page="../header.jsp"/>
	<%
		String next = request.getAttribute("next")+"";
		Object obj = session.getAttribute("khachHang");
		KhachHang khachHang = null;
		if(obj!=null){
			khachHang = (KhachHang)obj;
		}
		if(khachHang==null && !next.equals("1")){ 
	%>
		<h1>Bạn chưa đăng nhập vào hệ thống. Vui lòng quay lại trang chủ!</h1>
	<%
		}else{
		String baoLoi = request.getAttribute("baoLoi")+"";
		if(baoLoi.equals("null")){
			baoLoi = "";
		}
	%>
		<div class="container">
			<p><span><h1 class="text-center">ĐỔI MẬT KHẨU</h1></span></p>
			<span class="red">
				<%=baoLoi %>
			</span>
			<%
			String url = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
			%>
			<form action="<%=url%>/khach-hang" method="post">
			<input type="hidden" name="hanhDong" value="doi-mat-khau"/>
			  <div class="mb-3">
			    <label for="exampleInputEmail1" class="form-label">Mật khẩu hiện tại</label>
			    <input type="password" class="form-control" id="matKhauHienTai" name="matKhauHienTai">
			  </div>
			  <div class="mb-3">
			    <label for="exampleInputEmail1" class="form-label">Mật khẩu mới</label>
			    <input type="password" class="form-control" id="matKhauMoi" name="matKhauMoi">
			  </div>
			  <div class="mb-3">
			    <label for="exampleInputEmail1" class="form-label">Nhập lại mật khẩu mới</label>
			    <input type="password" class="form-control" id="matKhauMoiNhapLai" name="matKhauMoiNhapLai">
			  </div>
			  <button type="submit" class="btn btn-primary">Lưu mật khẩu</button>
			</form>
		</div>
	<%}%>
	<jsp:include page="../footer.jsp"/>
</body>
</html>
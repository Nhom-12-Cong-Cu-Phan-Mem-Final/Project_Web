// Hàm tải công việc với tham số lọc và phân trang
function loadJobs(page) {
    // Lấy giá trị của các trường lọc và tìm kiếm
    var linhVuc = $('#linhVucFilter').val();
    var tinhThanh = $('#tinhThanhFilter').val();
    var ten = $('#tenFilter').val();  // Lấy giá trị từ ô tìm kiếm theo tên công việc

    $.ajax({
        url: 'CongViecServlet',  // URL của servlet hoặc API
        method: 'GET',
        data: { 
            page: page, 
            linhVuc: linhVuc,  // Thêm tham số lĩnh vực
            tinhThanh: tinhThanh,  // Thêm tham số tỉnh thành
            ten: ten,  // Thêm tham số tìm kiếm theo tên công việc
			kinhNghiem: kinhNghiem,
			luongKhoiDiemHienTai: luongKhoiDiemHienTai,
			LuongKetThucHienTai: LuongKetThucHienTai,
            ajax: true  // Đánh dấu là yêu cầu AJAX
        },
        success: function(response) {
            console.log("Dữ liệu trả về từ server: ", response);
            if (!response || !response.congViecs || !response.totalPages) {
                /*alert('Dữ liệu không hợp lệ!');
                return;*/
            }

			let jobListHtml = '';
			response.congViecs.forEach(function(congViec) {
			    let maxTitleLength = 35;  // Giới hạn độ dài tên công việc
			    let jobTitle = congViec.ten;
			    if (jobTitle.length > maxTitleLength) {
			        jobTitle = jobTitle.substring(0, maxTitleLength) + '...';  // Cắt và thêm ba chấm
			    }

			    // Định dạng lương (format float thành kiểu có dấu phân cách hàng nghìn)
			    let formattedSalary = congViec.luong.toLocaleString() + ' VND';

			    // Kiểm tra năm kinh nghiệm và thay đổi giá trị nếu cần
			    let experience = congViec.namKinhNghiem === 0 ? 'Không yêu cầu kinh nghiệm' : congViec.namKinhNghiem + ' năm';

			    jobListHtml += `
			    <div class="col-12 col-md-4 mb-4 py-0">
			        <a href="ChiTietCongViecServlet?id=${congViec.idCongViec}" class="text-decoration-none text-dark">
			            <div class="py-3 px-3 bg-light shadow rounded">
							<h5 class="card-title">
		                        <strong>${jobTitle}</strong>
		                    </h5>
							<div class="d-flex mt-2"> 
							<img src="${congViec.logo}"
							     class="card-img-top img-fluid job-img" alt="Công việc">
				                <div class="card-body ms-3">
				                    
								<h6 class="font-weight-bold font-rem-1-1">${congViec.tenCongTy}</h6>
				                    <p class="card-text">
				                        <strong><i class="bi bi-currency-dollar text-warning"></i> </strong> 
				                        ${formattedSalary} <br>
				                        <strong><i class="bi bi-briefcase text-success"></i> </strong> 
				                        ${experience} <br>
				                        <strong><i class="bi bi-geo-alt text-primary"></i></strong> 
				                        ${congViec.diaDiem}
				                    </p>
				                </div>
							</div>
			            </div>
			        </a>  
			    </div>
			    `;
			});

            $('#job-list').fadeOut(300, function() {
                $(this).html(jobListHtml).fadeIn(500);
            });

            // Xử lý phân trang
            let totalPages = response.totalPages;
            let paginationHtml = '<ul class="pagination justify-content-center">';
            for (let i = 1; i <= totalPages; i++) {
				paginationHtml += `
				        <li class="page-item ${i == response.currentPage ? 'active' : ''}">
				            <a href="#" class="page-link" data-page="${i}">${i}</a>
				        </li>
				    `;
            }
			paginationHtml += '</ul>';
            $('#pagination').fadeOut(300, function() {
                $(this).html(paginationHtml).fadeIn(500);
				$('.page-link').off('click').on('click', function (e) {
				        e.preventDefault();
				        const page = $(this).data('page');
				        loadJobs(page);
				    });
            });
        },
        error: function(xhr, status, error) {
            console.error("Error details:", status, error);  // In ra chi tiết lỗi
            alert('Lỗi tải dữ liệu nè!');
        }
    });
}

// Gán sự kiện click cho nút tìm kiếm
$(document).ready(function() {
    $('#searchBtn').click(function() {
		console.log("Bộ lọc thay đổi:", $(this).val());  // Kiểm tra giá trị của bộ lọc
        loadJobs(1);  // Tải dữ liệu cho trang đầu tiên sau khi nhấn nút tìm kiếm
    });

    // Tải trang đầu tiên khi trang được load
    loadJobs(1); // Tải dữ liệu cho trang đầu tiên
});

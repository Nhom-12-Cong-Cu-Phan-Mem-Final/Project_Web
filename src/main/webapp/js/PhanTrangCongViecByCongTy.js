function loadJobs(page) {
    $.ajax({
        url: 'CongViecByCongTyServlet?id=' + idCT,  // URL của servlet hoặc API
        method: 'GET',
        data: { page: page, ajax: true },  // Truyền tham số page vào backend
        success: function(response) {
            // Kiểm tra dữ liệu trả về
            console.log("Dữ liệu trả về từ server: ", response);
            if (!response || !response.congViecs || !response.totalPages) {
                /*alert('Dữ liệu không hợp lệ!');
                return;*/
            }

            // In ra thông tin phản hồi để kiểm tra
            console.log(response);  

            // Cập nhật danh sách công việc
            let jobListHtml = '';
            response.congViecs.forEach(function(congViec) {
				let maxTitleLength = 20;  // Giới hạn độ dài tên công việc
				let jobTitle = congViec.ten;
				if (jobTitle.length > maxTitleLength) {
				    jobTitle = jobTitle.substring(0, maxTitleLength) + '...';  // Cắt và thêm ba chấm
				}
				
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
				                     class="card-img-top img-fluid job-img" alt="Công việc" >
				                <div class="card-body ms-3">
				                    
				                    <h6 class="font-weight-bold font-rem-1-1" >${congViec.tenCongTy}</h6>
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

// Tải trang đầu tiên khi trang được load
$(document).ready(function() {
    loadJobs(1); // Tải dữ liệu cho trang đầu tiên
});

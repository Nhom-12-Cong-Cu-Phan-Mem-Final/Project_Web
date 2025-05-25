function loadHoSos(page) { 
    let linhVuc = $('#linhVucFilter').val();  // Lấy giá trị lĩnh vực
    let thoiGian = $('#thoiGianFilter').val();  // Lấy giá trị thời gian
    let trangThai = $('#trangThaiFilter').val();  // Lấy giá trị lượt xem
    let searchText = $('#search-input').val();  // Lấy giá trị từ input tìm kiếm

    $.ajax({
        url: 'CongViecDaUngTuyenServlet',  // URL của servlet hoặc API
        method: 'GET',
        data: { 
            page: page,  // Truyền tham số page
            linhVuc: linhVuc,  // Truyền tham số lọc lĩnh vực
            thoiGian: thoiGian,  // Truyền tham số lọc thời gian
            trangThai: trangThai,  // Truyền tham số lọc lượt xem
            searchText: searchText,
            ajax: true  // Để xác định là AJAX request
        },  
        success: function(response) {
            // Kiểm tra dữ liệu trả về
            console.log("Dữ liệu trả về từ server: ", response);
           /* if (!response || !response.hoSos || !response.totalPages) {
                alert(response);
                return;
            }*/

            // In ra thông tin phản hồi để kiểm tra
            console.log(response);  
            if (!Array.isArray(response.hoSos)) {
                console.warn("Dữ liệu hoSos không hợp lệ:", response.hoSos);
                $('#hoSo-list').html('<tr><td colspan="7">Không có dữ liệu để hiển thị.</td></tr>');
                return;
            }

            // Cập nhật danh sách hồ sơ
            let hoSoListHtml = '';
            response.hoSos.forEach(function(hoSo) {
                let maxTitleLength = 30;  
                let hoSoTitle = hoSo.congViec.ten;  // Cập nhật theo cấu trúc JSON
                if (hoSoTitle.length > maxTitleLength) {
                    hoSoTitle = hoSoTitle.substring(0, maxTitleLength) + '...';  // Cắt và thêm ba chấm
                }
                let thoiGianGui = new Date(hoSo.thoiGianGui).toLocaleDateString('vi-VN');  // Định dạng thời gian

                hoSoListHtml += `
                    <tr>
                        <td> <strong>${hoSoTitle}</strong><br>Mã CongViec: ${hoSo.idCongViec}</td>
                        <td class="text-muted">${hoSo.congViec.tenCongTy}</td>
                        <td class="text-muted">${hoSo.congViec.linhVuc}</td>
                        <td class="text-muted">${thoiGianGui}</td>
                        <td class="text-muted">${hoSo.trangThai}</td>
						<td>
							<button type="button" class="btn btn-outline-coral btn-sm btn-chi-tiet" data-idcongviec="${hoSo.idCongViec}">
							    Chi tiết
							</button>
						</td>
                        <td>
							<button type="button" 
								class="btn btn-primary btn-xem-cv" 
								data-idcv="${hoSo.idCV}"
								data-bs-toggle="modal" 
								data-bs-target="#cvModal">
							  Xem CV
							</button>
                        </td>
                    </tr>
                `;
            });

            // Cập nhật danh sách hồ sơ vào trang
            $('#hoSo-list').fadeOut(300, function() {
                $(this).html(hoSoListHtml).fadeIn(500);
            });

            // Cập nhật phân trang
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
						        loadHoSos(page);
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
    $('#linhVucFilter, #thoiGianFilter, #trangThaiFilter').change(function() {
        loadHoSos(1);  // Tải lại dữ liệu khi có thay đổi
    });

    // Lắng nghe sự kiện thay đổi của input tìm kiếm
    $('#search-input').keyup(function() {
        loadHoSos(1);  // Tải lại dữ liệu khi có thay đổi
    });

    loadHoSos(1);  // Tải dữ liệu cho trang đầu tiên
});

function showJobDetail(idCongViec) {
    $.ajax({
        url: 'ChiTietCongViecServlet',  // Đường dẫn servlet
        method: 'GET',
        data: { id: idCongViec, ajax: true },  // Gửi ID công việc
        success: function(response) {
            if (!response) {
                alert('Không thể tải dữ liệu chi tiết!');
                return;
            }

            try {
                // Hiển thị dữ liệu vào modal với kiểm tra null/undefined
                $('#job-name').text(response.congViec.ten || 'Không có tên công việc');
                $('#job-location').text(response.congViec.diaDiem || 'Không có địa điểm');
                $('#job-salary').text(
                    response.congViec.luong !== undefined && response.congViec.luong !== null
                        ? response.congViec.luong.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })
                        : 'Không có lương'
                );
                $('#job-experience').text(
                    response.congViec.namKinhNghiem !== undefined && response.congViec.namKinhNghiem !== null
                        ? response.congViec.namKinhNghiem + ' năm'
                        : 'Không yêu cầu kinh nghiệm'
                );
                $('#job-field').text(response.congViec.linhVuc || 'Không có lĩnh vực');
                $('#job-post-time').text(
                    response.congViec.thoiGianDang
                        ? new Date(response.congViec.thoiGianDang).toLocaleDateString('vi-VN')
                        : 'Không rõ'
                );
                $('#job-expiry-time').text(
                    response.congViec.thoiGianHetHan
                        ? new Date(response.congViec.thoiGianHetHan).toLocaleDateString('vi-VN')
                        : 'Không rõ'
                );
                $('#job-description').text(response.congViec.moTa || 'Không có mô tả');
                $('#job-requirements').text(response.congViec.yeuCau || 'Không có yêu cầu');
                $('#job-benefits').text(response.congViec.quyenLoi || 'Không có quyền lợi');
                $('#job-views').text(response.congViec.luotXem !== undefined ? response.congViec.luotXem : '0');
                $('#job-applications').text(response.congViec.luotNop !== undefined ? response.congViec.luotNop : '0');

                // Mở modal
                $('#jobDetailModal').modal('show');
            } catch (error) {
                console.error("Lỗi khi xử lý dữ liệu: ", error);
                alert('Dữ liệu trả về không hợp lệ!');
            }
        },
        error: function(xhr, status, error) {
            console.error("Error details:", status, error);
            alert('Lỗi tải chi tiết công việc!');
        }
    });
}

$(document).on('click', '.btn-chi-tiet', function () {
    const idCongViec = $(this).data('idcongviec');
    showJobDetail(idCongViec);
});
$(document).on('click', '.btn-xem-cv', function () {
	const idCV = $(this).data('idcv');
	loadCVContent(idCV);
});



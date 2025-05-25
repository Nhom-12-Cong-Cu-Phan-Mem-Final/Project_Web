function loadHoSos(page) { 
    let linhVuc = $('#linhVucFilter').val();  // Lấy giá trị lĩnh vực
    let thoiGian = $('#thoiGianFilter').val();  // Lấy giá trị thời gian
    let trangThai = $('#trangThaiFilter').val();  // Lấy giá trị lượt xem
    let luotNop = $('#luotNopFilter').val();  // Lấy giá trị lượt nộp
    let searchText = $('#search-input').val();  // Lấy giá trị từ input tìm kiếm

    $.ajax({
        url: 'HoSoUngTuyenServlet',  // URL của servlet hoặc API
        method: 'GET',
        data: { 
            page: page,  // Truyền tham số page
            linhVuc: linhVuc,  // Truyền tham số lọc lĩnh vực
            thoiGian: thoiGian,  // Truyền tham số lọc thời gian
            trangThai: trangThai,  // Truyền tham số lọc lượt xem
            luotNop: luotNop,  // Truyền tham số lọc lượt nộp
            searchText: searchText,
            ajax: true  // Để xác định là AJAX request
        },  
        success: function(response) {
            // Kiểm tra dữ liệu trả về
            console.log("Dữ liệu trả về từ server: ", response);
            /*if (!response || !response.hoSos || !response.totalPages) {
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
                let hoSoTitle = hoSo.cv.ungvien.fullName;  // Cập nhật theo cấu trúc JSON
                if (hoSoTitle.length > maxTitleLength) {
                    hoSoTitle = hoSoTitle.substring(0, maxTitleLength) + '...';  // Cắt và thêm ba chấm
                }
                let thoiGianGui = new Date(hoSo.thoiGianGui).toLocaleDateString('vi-VN');  // Định dạng thời gian

                hoSoListHtml += `
                    <tr>
                        <td> <strong>${hoSoTitle}</strong><br>Mã CV: ${hoSo.idCV}</td>
                        <td class="text-muted">${hoSo.cv.position}</td>
                        <td class="text-muted">${hoSo.congViec.ten}</td>
                        <td class="text-muted">${thoiGianGui}</td>
						<td>

							<select class="form-select status-dropdown" 
							        data-idcv="${hoSo.idCV}" 
							        data-idcongviec="${hoSo.idCongViec}" 
							        data-original-value="${hoSo.trangThai}">
						        <option value="Chờ" ${hoSo.trangThai == 'Chờ' ? 'selected' : ''} >Chờ</option>
						        <option value="Phỏng vấn" ${hoSo.trangThai == 'Phỏng vấn' ? 'selected' : ''} >Phỏng vấn</option>
						        <option value="Chấp nhận" ${hoSo.trangThai == 'Chấp nhận' ? 'selected' : ''} >Chấp nhận</option>
						        <option value="Từ chối" ${hoSo.trangThai == 'Từ chối' ? 'selected' : ''} >Từ chối</option>
						    </select>
						</td>
                        <td>
							<button type="button" 
								class="btn btn-primary btn-view-cv" data-idcv="${hoSo.idCV}"
								data-bs-toggle="modal" 
								data-bs-target="#cvModal" 
								>
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
						        LloadHoSos(page);
						    });
			           });
			       },
        error: function(xhr, status, error) {
            console.error("Error details:", status, error);  // In ra chi tiết lỗi
            alert('Lỗi tải dữ liệu nè!');
        }
    });
}
function updateStatus(selectElement, idCV, idCongViec) {
    const selectedValue = selectElement.value;

    // Hiển thị hộp thoại xác nhận
    const confirmUpdate = confirm(`Bạn có chắc chắn muốn thay đổi trạng thái thành "${selectedValue}" không?`);

    if (confirmUpdate) {
        // Tạo FormData để gửi dữ liệu
		const csrfToken = document.querySelector("meta[name='csrf-token']").content;
		
		const params = new URLSearchParams();
		params.append('idCV', idCV);
		params.append('idCongViec', idCongViec);
		params.append('trangThai', selectedValue);
		params.append('csrfToken', csrfToken); 

		fetch('HoSoUngTuyenServlet', {
		    method: 'POST',
		    body: params,
		})
        .then(response => response.json()) 
        .then(data => {
            // Kiểm tra phản hồi từ server (tùy thuộc server trả về gì)
            if (data.status === "success") { 
				document.querySelector("meta[name='csrf-token']").setAttribute("content", data.newToken);
                alert('Cập nhật trạng thái thành công!');
            } else {
                alert('Cập nhật trạng thái thất bại.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Có lỗi xảy ra khi cập nhật trạng thái.');
        });
    } else {
        // Nếu người dùng hủy, trả lại giá trị ban đầu của dropdown
        selectElement.value = selectElement.getAttribute('data-original-value');
    }
}
// Tải trang đầu tiên khi trang được load
$(document).ready(function() {
    $('#linhVucFilter, #thoiGianFilter, #trangThaiFilter, #luotNopFilter').change(function() {
        loadHoSos(1);  // Tải lại dữ liệu khi có thay đổi
    });

    // Lắng nghe sự kiện thay đổi của input tìm kiếm
    $('#search-input').keyup(function() {
        loadHoSos(1);  // Tải lại dữ liệu khi có thay đổi
    });
	
	// Gán sự kiện khi bấm xem CV
	  $(document).on("click", ".btn-view-cv", function () {
	      const idCV = $(this).data("idcv");
	      if (idCV) {
	          loadCVContent(idCV);
	      }
	  });

	  // Gán sự kiện thay đổi trạng thái dropdown
	  $(document).on("change", ".status-dropdown", function () {
	      const idCV = $(this).data("idcv");
	      const idCongViec = $(this).data("idcongviec");
	      updateStatus(this, idCV, idCongViec);
	  });

	  // Gán sự kiện phân trang
	  $(document).on("click", ".page-link", function (e) {
	      e.preventDefault();
	      const page = $(this).data("page");
	      if (page) loadHoSos(page);
	  });


    loadHoSos(1);  // Tải dữ liệu cho trang đầu tiên
});

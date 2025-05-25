function openCVModal() {
    const idCongViec = document.getElementById("idCongViec").value;

    fetch('QuanLyCVServlet', {
        method: 'GET',
        headers: {
            'X-Requested-With': 'XMLHttpRequest'
        }
    })
    .then(response => response.json())
    .then(data => {
        console.log('Dữ liệu nhận được:', data);
        const cvModalBody = document.getElementById('cvModalBody');
        let htmlContent = "";

        if (Array.isArray(data)) {
            data.forEach(cv => {
                htmlContent += `
                    <div class="col-md-4 mb-3">
                        <div class="card">
                            <div class="card-body">
                                <h5 class="card-title">CV - ${cv.ungvien.fullName}</h5>
                                <p class="card-text">${cv.position}</p>
                                <button type="button"
                                    class="btn btn-info btn-sm btn-eye"
                                    data-id="${cv.idCV}"
                                    data-bs-toggle="modal"
                                    data-bs-target="#cvModal">
                                    Xem CV
                                </button>
                                <button type="button"
                                    class="btn btn-success btn-sm btn-ung-tuyen mt-2"
                                    data-idcv="${cv.idCV}"
                                    data-idcongviec="${idCongViec}">
                                    <i class="bi bi-check"></i> Chọn
                                </button>
                            </div>
                        </div>
                    </div>
                `;
            });
        } else {
            htmlContent = "<p>Không có dữ liệu CV nào.</p>";
        }

        cvModalBody.innerHTML = htmlContent;
    })
    .catch(error => {
        console.error('Lỗi khi tải danh sách CV:', error);
        document.getElementById('cvModalBody').innerHTML = "<p>Đã xảy ra lỗi khi tải dữ liệu.</p>";
    });
}

document.body.addEventListener("click", function (e) {
    // Xem nội dung CV
    if (e.target.classList.contains("btn-eye")) {
        const idCV = e.target.getAttribute("data-id");
        loadCVContent(idCV);
        return;
    }

    // Ứng tuyển
    if (e.target.matches(".btn-ung-tuyen, .btn-ung-tuyen *")) {
        const btn = e.target.closest(".btn-ung-tuyen");
        const idCV = btn.getAttribute("data-idcv");
        const idCongViec = btn.getAttribute("data-idcongviec");

		const csrfToken = document.querySelector('meta[name="csrf-token"]').content;

		const params = new URLSearchParams();
		params.append("idCV", idCV);
		params.append("idCongViec", idCongViec);

		fetch("UngTuyenServlet", {
		    method: "POST",
		    headers: {
		        "Content-Type": "application/x-www-form-urlencoded",
		        "X-CSRF-TOKEN": csrfToken
		    },
		    body: params
		})
        .then(res => {
            if (!res.ok) {
                return res.text().then(text => {
                    console.error("Server trả về HTML lỗi:\n", text);
                    throw new Error("Server lỗi " + res.status);
                });
            }
            return res.json();
        })
        .then(data => {
            if (data.success) {
                alert("Ứng tuyển thành công!");
            } else {
                alert(data.message || "Ứng tuyển thất bại.");
            }

            if (data.newToken) {
                document.querySelector('meta[name="csrf-token"]').setAttribute("content", data.newToken);
            }
        })
        .catch(err => {
            console.error("Lỗi khi ứng tuyển:", err);
            alert("Lỗi hệ thống: " + err.message);
        });
    }
});

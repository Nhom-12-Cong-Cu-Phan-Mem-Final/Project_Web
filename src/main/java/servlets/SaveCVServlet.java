package servlets;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import beans.CV;
import beans.ChungChi;
import beans.HocVan;
import beans.KinhNghiem;
import beans.KyNang;
import beans.TaiKhoan;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.AuthUtil;
import utils.CSRFTokenManager;
import conn.SQLServerConnection;
import dao.CVDAO;
import filters.HTMLSanitizer;
import filters.LengthFilter;

public class SaveCVServlet extends HttpServlet {

	private final ObjectMapper objectMapper = new ObjectMapper();
	private static final Logger LOGGER = LoggerFactory.getLogger(SaveCVServlet.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Thiết lập kiểu trả về là JSON

		if (!AuthUtil.authorizeRole(request, response, "UngVien"))
			return;

		HttpSession session = request.getSession(false);

		TaiKhoan taiKhoan = (TaiKhoan) session.getAttribute("account");

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		// Đọc dữ liệu JSON từ request body
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		try (BufferedReader reader = request.getReader()) {
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
			}
		}
		String jsonString = stringBuilder.toString();

		try {
			// Chuyển chuỗi JSON thành đối tượng JSONObject
			JSONObject jsonObject = new JSONObject(jsonString);

			// Lấy các giá trị từ JSON
			String position = jsonObject.optString("position");
			position = HTMLSanitizer.sanitizeInput(position);
			if (LengthFilter.isTooLong(position, 100)) {
				LOGGER.warn("Position is too long!");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
				response.getWriter().write("Position exceeds maximum length!");
				return;
			}

			String careerGoals = jsonObject.optString("careerGoals");
			careerGoals = HTMLSanitizer.sanitizeInput(careerGoals);
			if (LengthFilter.isTooLong(careerGoals, 500)) {
				LOGGER.warn("Career goals are too long!");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
				response.getWriter().write("Career goals exceed maximum length!");
				return;
			}
			JSONArray educationArray = jsonObject.optJSONArray("educationData");
			JSONArray experienceArray = jsonObject.optJSONArray("experienceData");
			JSONArray certificateArray = jsonObject.optJSONArray("certificateData");
			JSONArray skillArray = jsonObject.optJSONArray("skillData");
			String mode = jsonObject.optString("mode");
			String IdCV = jsonObject.optString("IdCV");

			// Chuyển dữ liệu thành các đối tượng entity
			/*
			 * CareerGoals careerGoalsEntity = new CareerGoals();
			 * careerGoalsEntity.setGoal(careerGoals);
			 */

			List<HocVan> educationList = new ArrayList<>();
			for (int i = 0; i < educationArray.length(); i++) {
				JSONObject educationObject = educationArray.getJSONObject(i);
				HocVan educationEntity = new HocVan();
				String startStr = educationObject.optString("start");
				String endStr = educationObject.optString("end");

				try {
					if (!startStr.isEmpty()) {
						// Chuyển chuỗi start thành java.sql.Date
						java.util.Date startUtilDate = dateFormat.parse(startStr); // Chuyển chuỗi thành java.util.Date
						educationEntity.setStart(new java.sql.Date(startUtilDate.getTime())); // Chuyển thành
																								// java.sql.Date
					}
					if (!endStr.isEmpty()) {
						// Chuyển chuỗi end thành java.sql.Date
						java.util.Date endUtilDate = dateFormat.parse(endStr); // Chuyển chuỗi thành java.util.Date
						educationEntity.setEnd(new java.sql.Date(endUtilDate.getTime())); // Chuyển thành java.sql.Date
					}
				} catch (Exception e) {
					// Xử lý lỗi nếu chuỗi ngày không hợp lệ
					e.printStackTrace();
				}
				educationEntity.setSchool(HTMLSanitizer.sanitizeInput(educationObject.optString("school")));
				if (LengthFilter.isTooLong(educationEntity.getSchool(), 200)) {
					LOGGER.warn("School name is too long!");
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().write("School name exceeds maximum length!");
					return;
				}

				educationEntity.setMajor(HTMLSanitizer.sanitizeInput(educationObject.optString("major")));
				if (LengthFilter.isTooLong(educationEntity.getMajor(), 100)) {
					LOGGER.warn("Major is too long!");
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().write("Major exceeds maximum length!");
					return;
				}

				educationEntity.setDescription(HTMLSanitizer.sanitizeInput(educationObject.optString("description")));
				if (LengthFilter.isTooLong(educationEntity.getDescription(), 500)) {
					LOGGER.warn("Education description is too long!");
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().write("Education description exceeds maximum length!");
					return;
				}
				educationList.add(educationEntity);
				/*
				 * educationEntity.setSchool(HTMLSanitizer.sanitizeInput(educationObject.
				 * optString("school")));
				 * educationEntity.setMajor(HTMLSanitizer.sanitizeInput(educationObject.
				 * optString("major")));
				 * educationEntity.setDescription(HTMLSanitizer.sanitizeInput(educationObject.
				 * optString("description")));
				 * 
				 * educationEntity.setSchool(educationObject.optString("school"));
				 * educationEntity.setMajor(educationObject.optString("major"));
				 * educationEntity.setDescription(educationObject.optString("description"));
				 * 
				 * educationList.add(educationEntity);
				 */
			}

			List<KinhNghiem> experienceList = new ArrayList<>();
			for (int i = 0; i < experienceArray.length(); i++) {
				JSONObject experienceObject = experienceArray.getJSONObject(i);
				KinhNghiem experienceEntity = new KinhNghiem();

				String startStr = experienceObject.optString("start");
				String endStr = experienceObject.optString("end");

				try {
					if (!startStr.isEmpty()) {
						// Chuyển chuỗi start thành java.sql.Date
						java.util.Date startUtilDate = dateFormat.parse(startStr); // Chuyển chuỗi thành java.util.Date
						experienceEntity.setStart(new java.sql.Date(startUtilDate.getTime())); // Chuyển thành
																								// java.sql.Date
					}
					if (!endStr.isEmpty()) {
						// Chuyển chuỗi end thành java.sql.Date
						java.util.Date endUtilDate = dateFormat.parse(endStr); // Chuyển chuỗi thành java.util.Date
						experienceEntity.setEnd(new java.sql.Date(endUtilDate.getTime())); // Chuyển thành java.sql.Date
					}
				} catch (Exception e) {
					// Xử lý lỗi nếu chuỗi ngày không hợp lệ
					e.printStackTrace();
				}
				/*
				 * experienceEntity.setCompany(experienceObject.optString("company"));
				 * experienceEntity.setPosition(experienceObject.optString("position"));
				 * experienceEntity.setDescription(experienceObject.optString("description"));
				 */
				  experienceEntity.setCompany(HTMLSanitizer.sanitizeInput(experienceObject.optString("company")));
			        if (LengthFilter.isTooLong(experienceEntity.getCompany(), 200)) {
			            LOGGER.warn("Company name is too long!");
			            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			            response.getWriter().write("Company name exceeds maximum length!");
			            return;
			        }

			        experienceEntity.setPosition(HTMLSanitizer.sanitizeInput(experienceObject.optString("position")));
			        if (LengthFilter.isTooLong(experienceEntity.getPosition(), 100)) {
			            LOGGER.warn("Position in experience is too long!");
			            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			            response.getWriter().write("Position in experience exceeds maximum length!");
			            return;
			        }

			        experienceEntity.setDescription(HTMLSanitizer.sanitizeInput(experienceObject.optString("description")));
			        if (LengthFilter.isTooLong(experienceEntity.getDescription(), 500)) {
			            LOGGER.warn("Experience description is too long!");
			            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			            response.getWriter().write("Experience description exceeds maximum length!");
			            return;
			        }
			        experienceList.add(experienceEntity);
					/*
					 * experienceEntity.setCompany(HTMLSanitizer.sanitizeInput(experienceObject.
					 * optString("company")));
					 * experienceEntity.setPosition(HTMLSanitizer.sanitizeInput(experienceObject.
					 * optString("position")));
					 * experienceEntity.setDescription(HTMLSanitizer.sanitizeInput(experienceObject.
					 * optString("description")));
					 * 
					 * experienceList.add(experienceEntity);
					 */
			}

			List<ChungChi> certificateList = new ArrayList<>();
			for (int i = 0; i < certificateArray.length(); i++) {
				JSONObject certificateObject = certificateArray.getJSONObject(i);
				ChungChi certificateEntity = new ChungChi();
				/* certificateEntity.setName(certificateObject.optString("name")); */
				certificateEntity.setName(HTMLSanitizer.sanitizeInput(certificateObject.optString("name")));
				   if (LengthFilter.isTooLong(certificateEntity.getName(), 200)) {
			            LOGGER.warn("Certificate name is too long!");
			            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			            response.getWriter().write("Certificate name exceeds maximum length!");
			            return;
			        }
				certificateList.add(certificateEntity);
			}

			List<KyNang> skillList = new ArrayList<>();
			for (int i = 0; i < skillArray.length(); i++) {
				JSONObject skillObject = skillArray.getJSONObject(i);
				KyNang skillEntity = new KyNang();
				/*
				 * skillEntity.setName(skillObject.optString("name"));
				 * skillEntity.setLevel(skillObject.optString("level"));
				 */
				 skillEntity.setName(HTMLSanitizer.sanitizeInput(skillObject.optString("name")));
			        if (LengthFilter.isTooLong(skillEntity.getName(), 100)) {
			            LOGGER.warn("Skill name is too long!");
			            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			            response.getWriter().write("Skill name exceeds maximum length!");
			            return;
			        }

			        skillEntity.setLevel(HTMLSanitizer.sanitizeInput(skillObject.optString("level")));
			        if (LengthFilter.isTooLong(skillEntity.getLevel(), 50)) {
			            LOGGER.warn("Skill level is too long!");
			            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			            response.getWriter().write("Skill level exceeds maximum length!");
			            return;
			        }

			        skillList.add(skillEntity);
					/*
					 * skillEntity.setName(HTMLSanitizer.sanitizeInput(skillObject.optString("name")
					 * )); skillEntity.setLevel(HTMLSanitizer.sanitizeInput(skillObject.optString(
					 * "level")));
					 * 
					 * skillList.add(skillEntity);
					 */
			}

			int idUV = taiKhoan.getId();

			CV cv = new CV(idUV, position, careerGoals);
			cv.setIdCV(Integer.parseInt(IdCV));
			// Tiến hành lưu các đối tượng này vào cơ sở dữ liệu hoặc xử lý theo yêu cầu
			if (mode.equals("create")) {
				CVDAO.addCV(cv, educationList, experienceList, certificateList, skillList);
				LOGGER.info("User id = {} has created new cv with idCV = {}", idUV, IdCV);
			} else if (mode.equals("edit")) {
				CVDAO.updateCV(cv, educationList, experienceList, certificateList, skillList);
				LOGGER.info("User id = {} has edited cv with id = {}", idUV, IdCV);
			}
			// CSRFTokenManager.generateToken(request);
			// Trả về phản hồi thành công
			out.write("{\"status\":\"success\"}");
			request.getRequestDispatcher("QuanLyCVServlet").forward(request, response);
		} catch (Exception e) {
			// Nếu có lỗi trong việc phân tích cú pháp JSON
			out.write("{\"status\":\"error\",\"message\":\"Invalid JSON data\"}");
		}
	}
}

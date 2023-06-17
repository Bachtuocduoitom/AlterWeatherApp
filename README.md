Giới thiệu:
Việc theo dõi thời tiết là một nhu cầu thiết yếu của mọi người, bởi vì nó ảnh hưởng đáng kể đến cuộc sống hàng ngày của chúng ta. Nhận thức về điều này, nhóm chúng em đã quyết định xây dựng một ứng dụng dự báo thời tiết cho đề tài nghiên cứu trong môn học.
Với sự nghiêm túc, chúng em đã phát triển một ứng dụng thời tiết với giao diện thân thiện, tươi sáng và dễ sử dụng. Ứng dụng cung cấp đầy đủ các chức năng cần thiết để đáp ứng yêu cầu của một ứng dụng thời tiết tiện ích. Đồng thời, chúng em cũng đã cải tiến và bổ sung những tính năng mới để tăng cường trải nghiệm người dùng.
Chúng em hy vọng rằng ứng dụng dự báo thời tiết của nhóm sẽ đạt yêu cầu của môn học và trở thành một công cụ hữu ích trong cuộc sống hàng ngày của nhiều người.

Thành viên:
Chu Ngọc Chiến
Vương Trường Giang
Trần Minh Hiếu

Công nghệ:
Xây dựng cho hệ điều hành Android
Code bằng ngôn ngữ Java
Sử dụng database: room database
Các API thời tiết từ Openweathermap và google

Các chức năng:

Cung cấp thông tin thời tiết các thành phố lớn
Mô tả:
Ứng dụng cung cấp dữ liệu thời tiết cho các thành phố lớn.
Sử dụng thanh tìm kiếm và tìm theo các thành phố đã được gợi ý.
Background thay đổi theo điều kiện thời tiết.
Các dữ liệu bao gồm:
Nhiệt độ thực tế hiện tại, nhiệt độ cao nhất, thấp nhất, nhiệt độ theo cảm nhận.
Tên thành phố hiện tại và tên quốc gia.
Thời gian mặt trời lặn, mặt trời mọc.
Tầm nhìn xa, độ ẩm, áp suất, tốc độ gió.
Công nghệ:
Lấy dữ liệu thời tiết tại thành phố:
Openweathermap API bản 2.5 : lấy tên thành phố để trả về thông tin thời tiết tại vị trí đó dưới dạng JSON.

Cung cấp thông tin thời tiết tại vị trí hiện tại
Mô tả: 
Ứng dụng cung cấp dữ liệu thời tiết cho địa điểm hiện tại của người dùng.
Background thay đổi theo điều kiện thời tiết.
Các dữ liệu bao gồm:
Nhiệt độ thực tế hiện tại, nhiệt độ cao nhất, thấp nhất, nhiệt độ theo .cảm nhận.
Tên thành phố hiện tại và tên quốc gia.
Thời gian mặt trời lặn, mặt trời mọc.
Tầm nhìn xa, độ ẩm, áp suất, tốc độ gió.
	Công nghệ:
Xác định vị trí hiện tại:
Fused location provider client: Cung cấp vĩ độ và kinh độ hiện tại.
Lấy dữ liệu thời tiết tại vị trí hiện tại:
Openweathermap API bản 2.5: lấy kinh và vĩ độ hiện tại để trả về thông tin thời tiết tại vị trí đó dưới dạng JSON.

Dự báo thời tiết
	Mô tả:
Dự báo nhiệt độ và trạng thái thời tiết trong 5 ngày tiếp theo.
Dữ liệu được cập nhật theo mốc thời gian mỗi 3 giờ đồng hồ.
Công nghệ:
Dự báo thời tiết
Openweathermap forecast API bản 2.5: lấy tên thành phố hoặc tọa độ để trả về danh sách nhiệt độ các ngày tiếp theo dưới dạng JSON
Cung cấp thông tin chất lượng không khí
	Mô tả:
Ứng dụng cung cấp mô tả cơ bản về chất lượng không khí và thông tin về chỉ số SO2, CO và bụi mịn tại thời điểm hiện tại để đưa ra lời khuyên phù hợp cho người dùng.
	Công nghệ:
Openweathermap air pollution API bản 2.5 : lấy kinh và vĩ độ hiện tại để trả về thông tin thời tiết tại vị trí đó dưới dạng JSON.

Lưu địa điểm quen thuộc
Mô tả:
Ứng dụng cung cấp khả năng lưu lại các thành phố mà người dùng muốn xem thường xuyên vào một danh sách các thành phố.
Có thể vuốt màn hình để chuyển giữa màn hình thời tiết của các thành phố đã lưu.
Vuốt tên thành phố sang trái để xóa khỏi danh sách.
Thêm địa điểm bằng cách chọn “ADD” khi tìm kiếm thành phố
Công nghệ: 
Room database: là một local database dùng để lưu dữ liệu tên thành phố trên máy.

Cung cấp widget
Mô tả:
Ứng dụng cung cấp thông tin thời tiết cơ bản gồm nhiệt độ, mô tả chung và chất lượng không khí của một thành phố tùy chọn cho người dùng.
Background thay đổi theo thời tiết
Dữ liệu được cập nhật mỗi 30 phút.
	Công nghệ:
   Lấy dữ liệu thời tiết tại thành phố:
Openweathermap API bản 2.5 : lấy tên thành phố để trả về thông tin thời tiết tại vị trí đó dưới dạng JSON.







package user;

import java.sql.*;
import javax.sql.*;
import java.util.*;
import javax.naming.*;
import java.lang.*;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;

public class UserDAO {

	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	// new connection 공통인 것임. connection이라는 class로 빼기
	
	public UserDAO() throws SQLException {
		Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        DataSource ds = null;
        Context ctx = null;
		
		try {
//			String dbURL = "jdbc:mysql://localhost:3306/BBS?characterEncoding=UTF-8&serverTimezone=UTC"; 
//			String dbID = "root";
//			String dbPassword = "thdusthdus12!!";
//			Class.forName("com.mysql.cj.jdbc.Driver");
//			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			ctx = new InitialContext();
            Context envContext  = (Context)ctx.lookup("java:/comp/env");
            ds = (DataSource)envContext.lookup("jdbc/orcl");
            conn = ds.getConnection();
            stmt = conn.createStatement(); //SQL¹®ÀåÀ» »ý¼º

            String sqlstr = "SELECT * FROM EMP";
	        for ( int num = 0; num < 1100 ; num++) {
	            stmt.executeQuery(sqlstr);
	
	            rs = stmt.getResultSet();
	        }
        
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
            rs.close();                                                      
            stmt.close();
            conn.close();
        }

	}
	
	public int login(String userID, String userPassword) {
		String SQL = "SELECT userPwd FROM MEMBER WHERE userId = ?";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				if(rs.getString(1).equals(userPassword))
					return 1; // 로그인 성공
				else 
					return 0; // 비밀번호 불일치
			}
			return -1; // 아이디가 없음
		}catch (Exception e) {
			e.printStackTrace();
		}
		return -2; // 데이터베이스 오류
	}
	
	public int join(User user)
	{
		String SQL = "INSERT INTO USER VALUES (?, ?, ?, ?, ?)";
		
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getUserPassword());
			pstmt.setString(3, user.getUserName());
			pstmt.setString(4, user.getUserGender());
			pstmt.setString(5, user.getUserEmail());
			return pstmt.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
		
		}
		return -1; //데이터 베이스 오류
	}
}

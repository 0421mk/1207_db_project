package com.sbs.example.board;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.sbs.example.board.controller.ArticleController;
import com.sbs.example.board.controller.Controller;
import com.sbs.example.board.controller.MemberController;
import com.sbs.example.board.session.Session;
import com.sbs.example.board.util.DBUtil;
import com.sbs.example.board.util.SecSql;

public class App {
	public void run() {
		Scanner scanner = new Scanner(System.in);
		Session session = new Session();
		Connection conn = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); // Mysql JDBC 드라이버 로딩
			String url = "jdbc:mysql://127.0.0.1:3306/text_board?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=UTC&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";
			conn = DriverManager.getConnection(url, "root", "");
			
			while (true) {
				System.out.printf("명령어) ");
				String cmd = scanner.nextLine();
				cmd = cmd.trim();
				
				if(cmd.equals("system exit")) {
					System.out.println("프로그램을 종료합니다.");
					break;
				}
				
				String[] cmdBits = cmd.split(" ");
				
				if(cmdBits.length < 2) {
					System.out.println("존재하지 않는 명령어입니다.");
					continue;
				}
				
				String controllerName = cmdBits[0];
				
				Controller controller = null;
				
				MemberController memberController = new MemberController(conn, scanner, cmd, session);
				ArticleController articleController = new ArticleController(conn, scanner, cmd, session);
				
				if(controllerName.equals("article")) {
					controller = articleController;
				} else if(controllerName.equals("member")) {
					controller = memberController;
				} else {
					System.out.println("잘못된 명령어입니다.");
					continue;
				}
				
				controller.doAction();
				
			}
		} catch(ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch(SQLException e) {
			System.out.println("에러: " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close(); // 연결 종료
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}

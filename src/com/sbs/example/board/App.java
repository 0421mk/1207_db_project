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

import com.sbs.example.board.util.DBUtil;
import com.sbs.example.board.util.SecSql;

public class App {
	public void run() {
		Scanner scanner = new Scanner(System.in);
		
		Connection conn = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); // Mysql JDBC 드라이버 로딩
			String url = "jdbc:mysql://127.0.0.1:3306/text_board?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=UTC&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";
			conn = DriverManager.getConnection(url, "root", "");
			
			while (true) {
				System.out.printf("명령어) ");
				String cmd = scanner.nextLine();
				cmd = cmd.trim();
				
				int actionResult = doAction(conn, scanner, cmd);
				
				if(actionResult == -1) {
					break;
				}
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

	private int doAction(Connection conn, Scanner scanner, String cmd) {
		
		if (cmd.equals("article write")) {
			
			String title;
			String body;
			
			System.out.println("== 게시글 작성 ==");
			
			System.out.printf("제목: ");
			title = scanner.nextLine();
			System.out.printf("내용: ");
			body = scanner.nextLine();

			SecSql sql = new SecSql();
			
			sql.append("INSERT INTO article");
			sql.append("SET regDate = NOW()");
			sql.append(", updateDate = NOW()");
			sql.append(", title = ?", title);
			sql.append(", body = ?", body);
			
			int id = DBUtil.insert(conn, sql);
			
			System.out.printf("%d번 게시물이 생성되었습니다. \n", id);
			
		} else if (cmd.equals("article list")) {
			
			System.out.println("== 게시글 목록 ==");
			
			List<Article> articles = new ArrayList<>();
			
			SecSql sql = new SecSql();
			
			sql.append("SELECT * FROM article");
			sql.append("ORDER BY id DESC");
			
			List<Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);
			
			for (Map<String, Object> articleMap : articleListMap) {
				articles.add(new Article(articleMap));
			}
			
			if(articles.size() == 0) {
				System.out.println("게시글이 존재하지 않습니다.");
				return 0;
			}
			
			System.out.println("번호 / 제목");
			for(Article article : articles) {
				System.out.printf("%d / %s\n", article.id, article.title);
			}
			
		} else if (cmd.startsWith("article modify ")) {
			
			boolean isInt = cmd.split(" ")[2].matches("-?\\d+");
			
			if(!isInt) {
				System.out.println("게시글의 ID를 숫자로 입력해주세요.");
				return 0;
			}
			
			int id = Integer.parseInt(cmd.split(" ")[2].trim());
			
			SecSql sql = new SecSql();
			
			sql.append("SELECT COUNT(*)");
			sql.append("FROM article");
			sql.append("WHERE id = ?", id);
			
			int articlesCount = DBUtil.selectRowIntValue(conn, sql);
			
			if(articlesCount == 0) {
				System.out.printf("%d번 게시글이 존재하지 않습니다.\n", id);
				return 0;
			}
			
			String title;
			String body;
			
			System.out.println("== 게시글 수정 ==");
			System.out.printf("새 제목: ");
			title = scanner.nextLine();
			System.out.printf("새 내용: ");
			body = scanner.nextLine();
			
			sql = new SecSql();
			
			sql.append("UPDATE article");
			sql.append("SET regDate = NOW()");
			sql.append(", updateDate = NOW()");
			sql.append(", title = ?", title);
			sql.append(", body = ?", body);
			sql.append("WHERE id = ?", id);
			
			DBUtil.update(conn, sql);
			
			System.out.printf("%d번 글이 수정되었습니다.\n", id);
			
		} else if (cmd.startsWith("article detail ")) {
			
			boolean isInt = cmd.split(" ")[2].matches("-?\\d+");
			
			if(!isInt) {
				System.out.println("게시글의 ID를 숫자로 입력해주세요.");
				return 0;
			}
			
			int id = Integer.parseInt(cmd.split(" ")[2].trim());
			
			SecSql sql = new SecSql();
			
			sql.append("SELECT COUNT(*)");
			sql.append("FROM article");
			sql.append("WHERE id = ?", id);
			
			int articlesCount = DBUtil.selectRowIntValue(conn, sql);
			
			if(articlesCount == 0) {
				System.out.printf("%d번 게시글이 존재하지 않습니다.\n", id);
				return 0;
			}
			
			sql = new SecSql();
			
			sql.append("SELECT *");
			sql.append("FROM article");
			sql.append("WHERE id = ?", id);
			
			Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);
			
			Article article = new Article(articleMap);
			
			System.out.printf("번호: %d\n", article.id);
			System.out.printf("등록날짜: %s\n", article.regDate);
			System.out.printf("수정날짜: %s\n", article.updateDate);
			System.out.printf("제목: %s\n", article.title);
			System.out.printf("내용: %s\n", article.body);
			
		} else if (cmd.startsWith("article delete ")) {
			
			boolean isInt = cmd.split(" ")[2].matches("-?\\d+");
			
			if(!isInt) {
				System.out.println("게시글의 ID를 숫자로 입력해주세요.");
				return 0;
			}
			
			int id = Integer.parseInt(cmd.split(" ")[2].trim());
			
			SecSql sql = new SecSql();
			
			sql.append("SELECT COUNT(*)");
			sql.append("FROM article");
			sql.append("WHERE id = ?", id);
			
			int articlesCount = DBUtil.selectRowIntValue(conn, sql);
			
			if(articlesCount == 0) {
				System.out.printf("%d번 게시글이 존재하지 않습니다.\n", id);
				return 0;
			}
			
			System.out.println("== 게시글 삭제 ==");
			
			sql = new SecSql();
			
			sql.append("DELETE FROM article");
			sql.append("WHERE id = ?", id);
			
			DBUtil.delete(conn, sql);
			
			System.out.printf("%d번 글이 삭제되었습니다.\n", id);
			
		} else if (cmd.equals("member join")) {
			
			String loginId;
			String loginPw;
			String loginPwConfirm;
			
			System.out.println("== 회원가입 ==");
			
			System.out.printf("로그인 아이디: ");
			loginId = scanner.nextLine();
			
			while (true) {
				if(loginId.length() == 0) {
					System.out.println("아이디를 입력해주세요.");
					continue;
				}
			}
			
		} else if (cmd.equals("system exit")) {
			System.out.println("프로그램을 종료합니다.");
			return -1;
		} else {
			System.out.println("잘못된 명령어입니다.");
		}
		
		return 0;
	}
}

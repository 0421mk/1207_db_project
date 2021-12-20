package com.sbs.example.board.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.sbs.example.board.dto.Article;
import com.sbs.example.board.session.Session;
import com.sbs.example.board.util.DBUtil;
import com.sbs.example.board.util.SecSql;

public class ArticleController {
	Connection conn;
	Scanner scanner;
	String cmd;
	Session session;
	
	public ArticleController(Connection conn, Scanner scanner, String cmd, Session session) {
		this.conn = conn;
		this.scanner = scanner;
		this.cmd = cmd;
		this.session = session;
	}

	public void doWrite() {
		
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
		
	}

	public void showList() {
		
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
			return;
		}
		
		System.out.println("번호 / 제목");
		for(Article article : articles) {
			System.out.printf("%d / %s\n", article.id, article.title);
		}
		
	}

	public void doModify() {
		
		boolean isInt = cmd.split(" ")[2].matches("-?\\d+");
		
		if(!isInt) {
			System.out.println("게시글의 ID를 숫자로 입력해주세요.");
			return;
		}
		
		int id = Integer.parseInt(cmd.split(" ")[2].trim());
		
		SecSql sql = new SecSql();
		
		sql.append("SELECT COUNT(*)");
		sql.append("FROM article");
		sql.append("WHERE id = ?", id);
		
		int articlesCount = DBUtil.selectRowIntValue(conn, sql);
		
		if(articlesCount == 0) {
			System.out.printf("%d번 게시글이 존재하지 않습니다.\n", id);
			return;
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
		sql.append("SET updateDate = NOW()");
		sql.append(", title = ?", title);
		sql.append(", body = ?", body);
		sql.append("WHERE id = ?", id);
		
		DBUtil.update(conn, sql);
		
		System.out.printf("%d번 글이 수정되었습니다.\n", id);
		
	}

	public void showDetail() {
		
		boolean isInt = cmd.split(" ")[2].matches("-?\\d+");
		
		if(!isInt) {
			System.out.println("게시글의 ID를 숫자로 입력해주세요.");
			return;
		}
		
		int id = Integer.parseInt(cmd.split(" ")[2].trim());
		
		SecSql sql = new SecSql();
		
		sql.append("SELECT COUNT(*)");
		sql.append("FROM article");
		sql.append("WHERE id = ?", id);
		
		int articlesCount = DBUtil.selectRowIntValue(conn, sql);
		
		if(articlesCount == 0) {
			System.out.printf("%d번 게시글이 존재하지 않습니다.\n", id);
			return;
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
		
	}

	public void doDelete() {
		
		boolean isInt = cmd.split(" ")[2].matches("-?\\d+");
		
		if(!isInt) {
			System.out.println("게시글의 ID를 숫자로 입력해주세요.");
			return;
		}
		
		int id = Integer.parseInt(cmd.split(" ")[2].trim());
		
		SecSql sql = new SecSql();
		
		sql.append("SELECT COUNT(*)");
		sql.append("FROM article");
		sql.append("WHERE id = ?", id);
		
		int articlesCount = DBUtil.selectRowIntValue(conn, sql);
		
		if(articlesCount == 0) {
			System.out.printf("%d번 게시글이 존재하지 않습니다.\n", id);
			return;
		}
		
		System.out.println("== 게시글 삭제 ==");
		
		sql = new SecSql();
		
		sql.append("DELETE FROM article");
		sql.append("WHERE id = ?", id);
		
		DBUtil.delete(conn, sql);
		
		System.out.printf("%d번 글이 삭제되었습니다.\n", id);
		
	}
}

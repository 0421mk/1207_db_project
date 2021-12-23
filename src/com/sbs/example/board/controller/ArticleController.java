package com.sbs.example.board.controller;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import com.sbs.example.board.dto.Article;
import com.sbs.example.board.service.ArticleService;
import com.sbs.example.board.session.Session;

public class ArticleController extends Controller {
	private Scanner scanner;
	private String cmd;
	private Session session;
	
	private ArticleService articleService;
	
	public ArticleController(Connection conn, Scanner scanner, String cmd, Session session) {
		this.scanner = scanner;
		this.cmd = cmd;
		this.session = session;
		
		articleService = new ArticleService(conn);
	}
	
	@Override
	public void doAction() {
		
		if (cmd.equals("article write")) {
			doWrite();
		} else if (cmd.equals("article list")) {
			showList();
		} else if (cmd.startsWith("article modify ")) {
			doModify();
		} else if (cmd.startsWith("article detail ")) {
			showDetail();
		} else if (cmd.startsWith("article delete ")) {
			doDelete();
		} else {
			System.out.println("존재하지 않는 명령어입니다.");
		}
		
	}

	private void doWrite() {
		
		String title;
		String body;
		
		System.out.println("== 게시글 작성 ==");
		
		System.out.printf("제목: ");
		title = scanner.nextLine();
		System.out.printf("내용: ");
		body = scanner.nextLine();
		
		int id = articleService.doWrite(title, body);
		
		System.out.printf("%d번 게시물이 생성되었습니다. \n", id);
		
	}

	private void showList() {
		
		System.out.println("== 게시글 목록 ==");
		
		List<Article> articles = articleService.getArticles();
		
		if(articles.size() == 0) {
			System.out.println("게시글이 존재하지 않습니다.");
			return;
		}
		
		System.out.println("번호 / 제목");
		for(Article article : articles) {
			System.out.printf("%d / %s\n", article.getId(), article.getTitle());
		}
		
	}

	private void doModify() {
		
		boolean isInt = cmd.split(" ")[2].matches("-?\\d+");
		
		if(!isInt) {
			System.out.println("게시글의 ID를 숫자로 입력해주세요.");
			return;
		}
		
		int id = Integer.parseInt(cmd.split(" ")[2].trim());
		
		int articlesCount = articleService.getArticlesCntById(id);
		
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
		
		articleService.doModify(title, body, id);
		
		System.out.printf("%d번 글이 수정되었습니다.\n", id);
		
	}

	private void showDetail() {
		
		boolean isInt = cmd.split(" ")[2].matches("-?\\d+");
		
		if(!isInt) {
			System.out.println("게시글의 ID를 숫자로 입력해주세요.");
			return;
		}
		
		int id = Integer.parseInt(cmd.split(" ")[2].trim());
		
		int articlesCount = articleService.getArticlesCntById(id);
		
		if(articlesCount == 0) {
			System.out.printf("%d번 게시글이 존재하지 않습니다.\n", id);
			return;
		}
		
		Article article = articleService.getArticle(id);
		
		System.out.printf("번호: %d\n", article.getId());
		System.out.printf("등록날짜: %s\n", article.getRegDate());
		System.out.printf("수정날짜: %s\n", article.getUpdateDate());
		System.out.printf("제목: %s\n", article.getTitle());
		System.out.printf("내용: %s\n", article.getBody());
		
	}

	private void doDelete() {
		
		boolean isInt = cmd.split(" ")[2].matches("-?\\d+");
		
		if(!isInt) {
			System.out.println("게시글의 ID를 숫자로 입력해주세요.");
			return;
		}
		
		int id = Integer.parseInt(cmd.split(" ")[2].trim());
		
		int articlesCount = articleService.getArticlesCntById(id);
		
		if(articlesCount == 0) {
			System.out.printf("%d번 게시글이 존재하지 않습니다.\n", id);
			return;
		}
		
		System.out.println("== 게시글 삭제 ==");
		
		articleService.doDelete(id);
		
		System.out.printf("%d번 글이 삭제되었습니다.\n", id);
		
	}
}

package com.sbs.example.board.controller;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.sbs.example.board.dao.ArticleDao;
import com.sbs.example.board.dto.Article;
import com.sbs.example.board.dto.Comment;
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
		} else if (cmd.startsWith("article list")) {
			showList();
		} else if (cmd.startsWith("article modify ")) {
			doModify();
		} else if (cmd.startsWith("article detail ")) {
			showDetail();
		} else if (cmd.startsWith("article delete ")) {
			doDelete();
		} else if (cmd.startsWith("article like ")) {
			doLike();
		} else if (cmd.startsWith("article comment ")) {
			doComment();
		} else {
			System.out.println("존재하지 않는 명령어입니다.");
		}
		
	}

	private void doComment() {
		
		if(session.getLoginedMember() == null) {
			System.out.println("로그인 후 이용해주세요.");
			return;
		}
		
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
		
		System.out.printf("== %d번 게시글의 댓글 ==\n", id);
		
		while(true) {
			
			// 댓글이 존재하지 않습니다.
			// 댓글 리스팅 (페이징 적용)
			
			System.out.println(">> [댓글쓰기] 1, [수정] 2, [삭제] 3, [페이징] 4, [나가기] 0 <<");
			
			int commentType;
			
			while(true) {
				try {
					System.out.printf("[article comment] 명령어) ");
					commentType = new Scanner(System.in).nextInt();
					
					break;
				} catch(InputMismatchException e) {
					System.out.println("정상적인 숫자를 입력해주세요.");
				}
			}
			
			if(commentType == 1) {
				
				System.out.println("== 댓글 작성 ==");
				
				String title;
				String body;
				
				System.out.printf("댓글 제목: ");
				title = scanner.nextLine();
				System.out.printf("댓글 내용: ");
				body = scanner.nextLine();
				
				int commentId = articleService.writeComment(id, title, body, session.getLoginedMemberId());
				
				System.out.printf("%d번 게시글에 %d번 댓글이 생성되었습니다.\n", id, commentId);
				
			} else if(commentType == 2) {
				System.out.println("== 댓글 수정 ==");
				
				int commentId;
				
				while(true) {
					try {
						System.out.printf(">> [수정할 댓글의 id], [취소] 0 <<) ");
						commentId = new Scanner(System.in).nextInt();
												
						break;
					} catch(InputMismatchException e) {
						System.out.println("정상적인 숫자를 입력해주세요.");
					}
				}
				
				if(commentId == 0) {
					continue;
				}
				
				int commentCnt = articleService.getCommentCntById(commentId, id);
				
				if (commentCnt == 0) {
					System.out.println("수정할 댓글이 존재하지 않습니다.");
					continue;
				}
				
				Comment comment = articleService.getCommentById(commentId);
				
				if (comment.getMemberId() != session.getLoginedMemberId()) {
					System.out.println("댓글 수정 권한이 없습니다.");
					continue;
				}
								
				System.out.printf("새 댓글 제목: ");
				String title = scanner.nextLine();
				
				System.out.printf("새 댓글 내용: ");
				String body = scanner.nextLine();
				
				articleService.modifyComment(commentId, title, body);
				System.out.printf("%d번 게시글의 %d번 댓글이 수정되었습니다.\n", id, commentId);
				
			} else if(commentType == 3) {
				System.out.println("== 댓글 삭제 ==");
			} else if(commentType == 4) {
				System.out.println("== 댓글 페이징 ==");
				// 현재 페이지: 1, 전체 페이지: 13, 전체 댓글 수: 30, 나가기
				// article comment page) 
			} else if(commentType == 0) {
				System.out.println("== 댓글 종료 ==");
				return;
			} else {
				System.out.println("가이드에 표시된 숫자만 입력해주세요.");
			}
		}

	}

	private void doLike() {
		
		if(session.getLoginedMember() == null) {
			System.out.println("로그인 후 이용해주세요.");
			return;
		}
		
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
		
		System.out.println("== 게시글 추천/비추천 ==");
		System.out.println(">> [추천] 1, [비추천] 2, [해제] 3, [나가기] 0 <<");
		
		System.out.printf("[article like] 명령어) ");
		int likeType = scanner.nextInt();
		
		scanner.nextLine();
		
		if(likeType == 0) {
			System.out.println("[article like] 종료");
			return;
		}
		
		int likeCheckCnt = articleService.likeCheckCnt(id, session.getLoginedMemberId());
		// likeCheck : 추천/비추천하지 않았다면 0, 추천했다면 1, 비추천했다면 2
		
		if(likeCheckCnt == 0) {
			
			if(likeType == 1 || likeType == 2) {
				
				articleService.insertLike(id, likeType, session.getLoginedMemberId());
				
				String msg = (likeType == 1 ? "추천" : "비추천");
				System.out.println(msg + " 완료");
				
			} else if(likeType == 3) {
				System.out.println("해제할 추천/비추천이 존재하지 않습니다.");
			} else {
				System.out.println("0~3 까지의 수만 입력해주세요.");
			}
			
		} else {
			// 결과값이 1 또는 2
			
			if(likeType == 3) {
				articleService.deleteLike(id, session.getLoginedMemberId());
				String msg = (likeCheckCnt == 1 ? "추천" : "비추천");
				System.out.println(msg + "을 취소합니다.");
				return;
			}
			
			if(likeType == likeCheckCnt) {
				String msg = (likeType == 1 ? "추천" : "비추천");
				System.out.println("이미 " + msg + "하였습니다.");
				return;
			} else {
				// 있는데 같지않다면
				// 추천한 상태인데 비추천으로 변경
				// modify
				articleService.modifyLike(id, likeType, session.getLoginedMemberId());
				
				String msg = (likeType == 1 ? "추천" : "비추천");
				System.out.println(msg + "으로 변경 완료");
			}
			
		}
		
	}

	private void doWrite() {
		
		if(session.getLoginedMember() == null) {
			System.out.println("로그인 후 이용해주세요.");
			return;
		}
		
		String title;
		String body;
		
		System.out.println("== 게시글 작성 ==");
		
		System.out.printf("제목: ");
		title = scanner.nextLine();
		System.out.printf("내용: ");
		body = scanner.nextLine();
		
		int id = articleService.doWrite(title, body, session.getLoginedMemberId());
		
		System.out.printf("%d번 게시물이 생성되었습니다. \n", id);
		
	}

	private void showList() {

		String[] cmdBits = cmd.split(" ");
		String searchKeyword = "";
		List<Article> articles;
		
		System.out.println("== 게시글 목록 ==");
		
		int page = 1;
		int itemsInAPage = 5;
		
		while(true) {
			
			// 검색어가 있는 경우
			if(cmdBits.length > 2) {
				searchKeyword = cmd.substring("article list ".length());
				articles = articleService.getArticlesByKeyword(page, itemsInAPage, searchKeyword);
			} else { // 검색어가 없는 경우
				if(cmd.length() != 12) {
					System.out.println("명령어를 잘못 입력하셨습니다.");
					return;
				}
				articles = articleService.getArticles(page, itemsInAPage);
			}
			
			if(articles.size() == 0) {
				System.out.println("게시글이 존재하지 않습니다.");
				return;
			}
			
			System.out.println("번호 / 제목 / 작성자");
			for(Article article : articles) {
				System.out.printf("%d / %s / %s\n", article.getId(), article.getTitle(), article.getExtra_writer());
			}
			
			// 64/5.0=12, 12.8 => 13
			// 정수, 실수 표현 방법이 다릅니다.
			// 현재 페이지, 마지막 페이지, 전체 글 수
			// articles.size()도 가능
			int articlesCnt = articleService.getArticlesCnt(searchKeyword);
			int lastPage = (int)Math.ceil(articlesCnt/(double)itemsInAPage);
			
			System.out.printf("현재 페이지: %d, 마지막 페이지: %d, 전체 글 수: %d\n", page, lastPage, articlesCnt);
			System.out.println("== [페이지 이동] 번호, [종료] 0 이하의 수 입력 ==");
			
			System.out.printf("[article list] 명령어) ");
			page = scanner.nextInt();
			
			scanner.nextLine();
			
			if(page <= 0) {
				System.out.println("게시판 조회를 종료합니다.");
				break;
			}
		}
		
	}

	private void doModify() {
		
		if(session.getLoginedMember() == null) {
			System.out.println("로그인 후 이용해주세요.");
			return;
		}
		
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
		
		if (article.getMemberId() != session.getLoginedMemberId()) {
			System.out.println("권한이 존재하지 않습니다.");
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
		
		articleService.increaseHit(id);
		
		Article article = articleService.getArticle(id);
		
		int likeVal = articleService.getLikeVal(id, 1); // 해당 게시글의 추천 수
		int disLikeVal = articleService.getLikeVal(id, 2); // 해당 게시글의 반대 수
		
		System.out.printf("번호: %d\n", article.getId());
		System.out.printf("등록날짜: %s\n", article.getRegDate());
		System.out.printf("수정날짜: %s\n", article.getUpdateDate());
		System.out.printf("작성자: %s\n", article.getExtra_writer());
		System.out.printf("조회수: %d\n", article.getHit());
		System.out.printf("추천: %d\n", likeVal);
		System.out.printf("반대: %d\n", disLikeVal);
		System.out.printf("제목: %s\n", article.getTitle());
		System.out.printf("내용: %s\n", article.getBody());
		
	}

	private void doDelete() {
		
		if(session.getLoginedMember() == null) {
			System.out.println("로그인 후 이용해주세요.");
			return;
		}
		
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
		
		if (article.getMemberId() != session.getLoginedMemberId()) {
			System.out.println("권한이 존재하지 않습니다.");
			return;
		}
		
		System.out.println("== 게시글 삭제 ==");
		
		articleService.doDelete(id);
		
		System.out.printf("%d번 글이 삭제되었습니다.\n", id);
		
	}
}

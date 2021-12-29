package com.sbs.example.board.service;

import java.sql.Connection;
import java.util.List;

import com.sbs.example.board.dao.ArticleDao;
import com.sbs.example.board.dto.Article;
import com.sbs.example.board.dto.Comment;

public class ArticleService {
	ArticleDao articleDao;
	
	public ArticleService(Connection conn) {
		articleDao = new ArticleDao(conn);
	}

	public int doWrite(String title, String body, int loginedMemberId) {
		return articleDao.doWrite(title, body, loginedMemberId);
	}

	public List<Article> getArticles(int page, int itemsInAPage) {
		int limitFrom = (page - 1) * itemsInAPage;
		int limitTake = itemsInAPage;
		
		return articleDao.getArticles(limitFrom, limitTake);
	}

	public int getArticlesCntById(int id) {
		
		return articleDao.getArticlesCntById(id);
		
	}

	public void doModify(String title, String body, int id) {
		
		articleDao.doModify(title, body, id);
		
	}

	public Article getArticle(int id) {
		// TODO Auto-generated method stub
		return articleDao.getArticle(id);
	}

	public void doDelete(int id) {
		
		articleDao.doDelete(id);
		
	}

	public void increaseHit(int id) {
		
		articleDao.increaseHit(id);
		
	}

	public List<Article> getArticlesByKeyword(int page, int itemsInAPage, String searchKeyword) {
		int limitFrom = (page - 1) * itemsInAPage;
		int limitTake = itemsInAPage;
		
		return articleDao.getArticlesByKeyword(limitFrom, limitTake, searchKeyword);
	}

	public int getArticlesCnt(String searchKeyword) {
		return articleDao.getArticlesCnt(searchKeyword);
	}

	public void insertLike(int id, int likeType, int loginedMemberId) {
		
		articleDao.insertLike(id, likeType, loginedMemberId);
		
	}

	public int likeCheckCnt(int id, int loginedMemberId) {
		
		return articleDao.likeCheckCnt(id, loginedMemberId);
		
	}

	public void deleteLike(int id, int loginedMemberId) {
		
		articleDao.deleteLike(id, loginedMemberId);
		
	}

	public void modifyLike(int id, int likeType, int loginedMemberId) {
		
		articleDao.modifyLike(id, likeType, loginedMemberId);
		
	}

	public int getLikeVal(int id, int likeType) {
		return articleDao.getLikeVal(id, likeType);
	}

	public int writeComment(int id, String title, String body, int loginedMemberId) {
		
		return articleDao.writeComment(id, title, body, loginedMemberId);
		
	}

	public int getCommentCntById(int commentId, int id) {
		
		return articleDao.getCommentCntById(commentId, id);
	
	}

	public Comment getCommentById(int commentId) {
		
		return articleDao.getCommentById(commentId);
	}

	public void modifyComment(int commentId, String title, String body) {
		
		articleDao.modifyComment(commentId, title, body);
		
	}

	public void deleteComment(int commentId) {
		
		articleDao.deleteComment(commentId);
		
	}

	public List<Comment> getCommentsByPage(int id, int page, int itemsInAPage) {
		int limitFrom = (page - 1) * itemsInAPage;
		int limitTake = itemsInAPage;
		
		return articleDao.getCommentsByPage(id, limitFrom, limitTake);
	}

}

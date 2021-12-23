package com.sbs.example.board.service;

import java.sql.Connection;
import java.util.List;

import com.sbs.example.board.dao.ArticleDao;
import com.sbs.example.board.dto.Article;

public class ArticleService {
	ArticleDao articleDao;
	
	public ArticleService(Connection conn) {
		articleDao = new ArticleDao(conn);
	}

	public int doWrite(String title, String body, int loginedMemberId) {
		return articleDao.doWrite(title, body, loginedMemberId);
	}

	public List<Article> getArticles() {
		return articleDao.getArticles();
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

}

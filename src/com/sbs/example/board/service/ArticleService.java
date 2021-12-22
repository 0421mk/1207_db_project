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

	public int doWrite(String title, String body) {
		return articleDao.doWrite(title, body);
	}

	public List<Article> getArticles() {
		
		return articleDao.getArticles();
		
	}

	public int getArticlesCntById(int id) {
		
		return articleDao.getArticlesCntById(id);
		
	}

}

package com.sbs.example.board.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sbs.example.board.dto.Article;
import com.sbs.example.board.dto.Comment;
import com.sbs.example.board.util.DBUtil;
import com.sbs.example.board.util.SecSql;

public class ArticleDao {
	Connection conn;

	public ArticleDao(Connection conn) {
		this.conn = conn;
	}

	public int doWrite(String title, String body, int loginedMemberId) {

		SecSql sql = new SecSql();

		sql.append("INSERT INTO article");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", memberId = ?", loginedMemberId);
		sql.append(", title = ?", title);
		sql.append(", body = ?", body);
		sql.append(", hit = 0");

		int id = DBUtil.insert(conn, sql);

		return id;

	}

	public List<Article> getArticles(int limitFrom, int limitTake) {

		List<Article> articles = new ArrayList<>();

		SecSql sql = new SecSql();

		sql.append("SELECT a.*, m.name AS extra_writer");
		sql.append("FROM article AS a");
		sql.append("LEFT JOIN `member` AS m");
		sql.append("ON a.memberId = m.id");
		sql.append("ORDER BY a.id DESC");
		sql.append("LIMIT ?, ?", limitFrom, limitTake);

		List<Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);

		for (Map<String, Object> articleMap : articleListMap) {
			articles.add(new Article(articleMap));
		}

		return articles;
	}

	public int getArticlesCntById(int id) {

		SecSql sql = new SecSql();

		sql.append("SELECT COUNT(*)");
		sql.append("FROM article");
		sql.append("WHERE id = ?", id);

		return DBUtil.selectRowIntValue(conn, sql);

	}

	public void doModify(String title, String body, int id) {

		SecSql sql = new SecSql();

		sql.append("UPDATE article");
		sql.append("SET updateDate = NOW()");
		sql.append(", title = ?", title);
		sql.append(", body = ?", body);
		sql.append("WHERE id = ?", id);

		DBUtil.update(conn, sql);

	}

	public Article getArticle(int id) {

		SecSql sql = new SecSql();

		sql.append("SELECT a.*, m.name AS extra_writer");
		sql.append("FROM article AS a");
		sql.append("LEFT JOIN `member` AS m");
		sql.append("ON a.memberId = m.id");
		sql.append("WHERE a.id = ?", id);

		Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);

		Article article = new Article(articleMap);

		return article;

	}

	public void doDelete(int id) {

		SecSql sql = new SecSql();

		sql.append("DELETE FROM article");
		sql.append("WHERE id = ?", id);

		DBUtil.delete(conn, sql);

	}

	public void increaseHit(int id) {

		SecSql sql = new SecSql();

		sql.append("UPDATE article");
		sql.append("SET hit = hit + 1");
		sql.append("WHERE id = ?", id);

		DBUtil.update(conn, sql);

	}

	public List<Article> getArticlesByKeyword(int limitFrom, int limitTake, String searchKeyword) {

		SecSql sql = new SecSql();

		sql.append("SELECT a.*, m.name AS extra_writer");
		sql.append("FROM article AS a");
		sql.append("LEFT JOIN `member` AS m");
		sql.append("ON a.memberId = m.id");
		sql.append("WHERE a.title LIKE CONCAT('%', ?, '%')", searchKeyword);
		sql.append("ORDER BY a.id DESC");
		sql.append("LIMIT ?, ?", limitFrom, limitTake);

		List<Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);

		List<Article> articles = new ArrayList<>();

		for (Map<String, Object> articleMap : articleListMap) {
			articles.add(new Article(articleMap));
		}

		return articles;
	}

	public int getArticlesCnt(String searchKeyword) {
		SecSql sql = new SecSql();

		sql.append("SELECT COUNT(*)");
		sql.append("FROM article");
		if (searchKeyword != "") {
			sql.append("WHERE title LIKE CONCAT('%', ?, '%')", searchKeyword);
		}

		return DBUtil.selectRowIntValue(conn, sql);
	}

	public void insertLike(int id, int likeType, int loginedMemberId) {

		SecSql sql = new SecSql();

		sql.append("INSERT INTO `like`");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", articleId = ?", id);
		sql.append(", memberId = ?", loginedMemberId);
		sql.append(", likeType = ?", likeType);

		DBUtil.insert(conn, sql);

	}

	public int likeCheckCnt(int id, int loginedMemberId) {

		// 데이터가 있다면 likeType 반환
		// 데이터가 없다면 0 반환

		SecSql sql = new SecSql();

		sql.append("SELECT");
		sql.append("CASE WHEN COUNT(*) != 0");
		sql.append("THEN likeType ELSE 0 END");
		sql.append("FROM `like`");
		sql.append("WHERE articleId = ? AND memberId = ?", id, loginedMemberId);

		return DBUtil.selectRowIntValue(conn, sql);
	}

	public void deleteLike(int id, int loginedMemberId) {

		SecSql sql = new SecSql();

		sql.append("DELETE FROM `like`");
		sql.append("WHERE articleId = ?", id);
		sql.append("AND memberId = ?", loginedMemberId);

		DBUtil.delete(conn, sql);

	}

	public void modifyLike(int id, int likeType, int loginedMemberId) {

		SecSql sql = new SecSql();

		sql.append("UPDATE `like`");
		sql.append("SET updateDate = NOW()");
		sql.append(", likeType = ?", likeType);
		sql.append("WHERE articleId = ?", id);
		sql.append("AND memberId = ?", loginedMemberId);

		DBUtil.update(conn, sql);

	}

	public int getLikeVal(int id, int likeType) {

		SecSql sql = new SecSql();

		sql.append("SELECT COUNT(*)");
		sql.append("FROM `like`");
		sql.append("WHERE articleId = ? AND likeType = ?", id, likeType);

		return DBUtil.selectRowIntValue(conn, sql);
	}

	public int writeComment(int id, String title, String body, int loginedMemberId) {

		SecSql sql = new SecSql();

		sql.append("INSERT INTO `comment`");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", articleId = ?", id);
		sql.append(", memberId = ?", loginedMemberId);
		sql.append(", title = ?", title);
		sql.append(", body = ?", body);

		return DBUtil.insert(conn, sql);
	}

	public int getCommentCntById(int commentId, int id) {

		SecSql sql = new SecSql();

		sql.append("SELECT COUNT(*)");
		sql.append("FROM `comment`");
		sql.append("WHERE id = ? AND articleId = ?", commentId, id);

		return DBUtil.selectRowIntValue(conn, sql);
	}

	public Comment getCommentById(int commentId) {

		SecSql sql = new SecSql();

		sql.append("SELECT *");
		sql.append("FROM `comment`");
		sql.append("WHERE id = ?", commentId);

		Map<String, Object> commentMap = DBUtil.selectRow(conn, sql);

		return new Comment(commentMap);
	}

	public void modifyComment(int commentId, String title, String body) {

		SecSql sql = new SecSql();

		sql.append("UPDATE `comment`");
		sql.append("SET updateDate = NOW()");
		sql.append(", title = ?", title);
		sql.append(", body = ?", body);
		sql.append("WHERE id = ?", commentId);

		DBUtil.update(conn, sql);

	}

	public void deleteComment(int commentId) {

		SecSql sql = new SecSql();

		sql.append("DELETE FROM `comment`");
		sql.append("WHERE id = ?", commentId);

		DBUtil.delete(conn, sql);

	}

	public List<Comment> getCommentsByPage(int id, int limitFrom, int limitTake) {

		SecSql sql = new SecSql();

		sql.append("SELECT c.*, m.name AS extra_writer");
		sql.append("FROM `comment` c");
		sql.append("INNER JOIN `member` m");
		sql.append("ON c.memberId = m.id");
		sql.append("WHERE articleId = ?", id);
		sql.append("LIMIT ?, ?", limitFrom, limitTake);

		List<Map<String, Object>> commentListMap = DBUtil.selectRows(conn, sql);

		List<Comment> comments = new ArrayList<>();

		for (Map<String, Object> commentMap : commentListMap) {
			comments.add(new Comment(commentMap));
		}

		return comments;
	}

	public int getCommentsCnt(int id) {

		SecSql sql = new SecSql();

		sql.append("SELECT COUNT(*)");
		sql.append("FROM `comment`");
		sql.append("WHERE articleId = ?", id);

		return DBUtil.selectRowIntValue(conn, sql);

	}

	public List<Article> getArticles() {
		
		List<Article> articles = new ArrayList<>();

		SecSql sql = new SecSql();

		sql.append("SELECT a.*, m.name AS extra_writer");
		sql.append("FROM article AS a");
		sql.append("LEFT JOIN `member` AS m");
		sql.append("ON a.memberId = m.id");
		sql.append("ORDER BY a.id DESC");

		List<Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);

		for (Map<String, Object> articleMap : articleListMap) {
			articles.add(new Article(articleMap));
		}

		return articles;
		
	}
}

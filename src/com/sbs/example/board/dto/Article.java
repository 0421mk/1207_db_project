package com.sbs.example.board.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class Article {
	private int id;
	private LocalDateTime regDate;
	private LocalDateTime updateDate;
	private int memberId;
	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	private String title;
	private String body;

	public Article(Map<String, Object> articleMap) {
		this.id = (int)articleMap.get("id");
		this.regDate = (LocalDateTime)articleMap.get("regDate");
		this.updateDate = (LocalDateTime)articleMap.get("updateDate");
		this.memberId = (int)articleMap.get("memberId");
		this.title = (String)articleMap.get("title");
		this.body = (String)articleMap.get("body");
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getRegDate() {
		return regDate;
	}

	public void setRegDate(LocalDateTime regDate) {
		this.regDate = regDate;
	}

	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}

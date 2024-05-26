package br.com.usasistemas.usaplatform.dao.response;

public class BasePagedResponse {
	
	private String cursorString;
	private Boolean hasMore;

	public String getCursorString() {
		return cursorString;
	}

	public void setCursorString(String cursorString) {
		this.cursorString = cursorString;
	}

	public Boolean getHasMore() {
		return hasMore;
	}

	public void setHasMore(Boolean hasMore) {
		this.hasMore = hasMore;
	}

}

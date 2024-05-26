package br.com.usasistemas.usaplatform.common.exception;

import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;

@SuppressWarnings("serial")
public abstract class BaseException extends RuntimeException {
	
	private ReturnMessage returnMessage;
	
	public BaseException() {}
	
	public BaseException(ReturnMessage rm) {
		super(rm.getMessage());
		this.returnMessage = rm;
	}

	public BaseException(ReturnMessage returnMessage, Throwable t) {
		super(returnMessage.toString(), t);
		this.returnMessage = returnMessage;
	}

	public ReturnMessage getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(ReturnMessage returnMessage) {
		this.returnMessage = returnMessage;
	}
	
}

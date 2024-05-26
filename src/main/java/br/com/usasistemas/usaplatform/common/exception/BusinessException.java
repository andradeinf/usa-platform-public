package br.com.usasistemas.usaplatform.common.exception;

import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;

@SuppressWarnings("serial")
public class BusinessException extends BaseException {
	
	public BusinessException() {}
	
	public BusinessException(ReturnMessage rm) {
		super(rm);
	}
	
	public BusinessException(ReturnMessage rm, Throwable t) {
		super(rm, t);
	}
	
}

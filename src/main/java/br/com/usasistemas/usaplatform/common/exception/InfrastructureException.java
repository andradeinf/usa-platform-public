package br.com.usasistemas.usaplatform.common.exception;

import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;

@SuppressWarnings("serial")
public class InfrastructureException extends BaseException {
	
	public InfrastructureException() {
	}
	
	public InfrastructureException(ReturnMessage rm) {
		super(rm);
	}
	
	public InfrastructureException(ReturnMessage rm, Throwable t) {
		super(rm, t);
	}

}

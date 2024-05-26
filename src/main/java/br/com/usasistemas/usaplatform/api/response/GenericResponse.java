package br.com.usasistemas.usaplatform.api.response;

import br.com.usasistemas.usaplatform.api.response.data.ReturnMessage;
import br.com.usasistemas.usaplatform.model.enums.ResponseCodesEnum;

public class GenericResponse{
	
	private ReturnMessage returnMessage;
	
	public GenericResponse(){
		this.returnMessage = new ReturnMessage();
		returnMessage.setCode(ResponseCodesEnum.GENERIC_SUCCESS.code());
		returnMessage.setMessage(ResponseCodesEnum.GENERIC_SUCCESS.message());
	}
	
	public GenericResponse(ReturnMessage returnMessage){
		this.returnMessage = returnMessage;
	}

	public ReturnMessage getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(ReturnMessage returnMessage) {
		this.returnMessage = returnMessage;
	}
		
}

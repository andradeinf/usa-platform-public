package br.com.usasistemas.usaplatform.model.enums;

public enum ResponseCodesEnum {
	GENERIC_ERROR (-100000L, "Generic Error"),
	PROFILE_UPDATE_ERROR(-100001L, "Erro ao atualizar os dados do perfil"),
	INVALID_USER_PASSWORD(-100002L, "Usuário/Senha inválido!"),
	USER_DISABLED(-100003L, "Usuário desativado!"),
	INVALID_RESET_PASSWORD_REQUEST(-100004L, "Solicitação de troca de senha inválida ou expirada!"),
	VALIDATION_ERROR(-100005L, "Erro ao validar os dados enviados!"),
	USER_NOT_FOUND(-100006L, "Usuário não encontrado!"),
	NOT_AUTHORIZED(-100007L, "Acesso não autorizado!"),
	INVALID_TIME_RANGE(-100008L, "Período inválido!"),
	FAIL_TO_UPLOAD_ATTACHMENT(-100009L, "Erro ao carregar o arquivo!"),
	FAIL_TO_UPLOAD_DOCUMENT(-100010L, "Erro ao carregar o documento!"),
	TRAINING_STORAGE_LIMITED_REACHED(-100011L, "Limite de armazenamento ultrapassado!"),
	TRAINING_FILE_FORMAT_NOT_ACCEPTED(-100012L, "Formato de vídeo não aceito!"),
	
	GENERIC_SUCCESS(100000L, "Sucesso."),	
	PROFILE_UPDATE_SUCCESS(100001L, "Perfil atualizado com sucesso."),
	SELECT_PROFILE(100002L, "Selecione uma das opções abaixo")
	;
	
	private final Long code;
	private final String message;
	
	ResponseCodesEnum(Long code, String message) {
        this.code = code;
        this.message = message;
    }
	
	public Long code(){
		return this.code;
	}
	
	public String message(){
		return this.message;
	}

}

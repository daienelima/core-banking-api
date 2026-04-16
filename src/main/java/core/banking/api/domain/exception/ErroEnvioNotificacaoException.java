package core.banking.api.domain.exception;

public class ErroEnvioNotificacaoException extends RuntimeException {

    public ErroEnvioNotificacaoException() {
        super("Erro ao enviar notificação");
    }
}

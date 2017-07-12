package com.example.daniel.videostreaming.utils.http;


import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ServerProblemException extends HttpClientException {

    private static final String MESSAGE = "Ocorreu um problema ao acessar a URL: ";

    private static final String UNEXPECTED_BEHAVIOR = "Comportamento inesperado do servidor.";

    private static final String UNKNOWN_HOST = "Host não foi encontrado.";

    private static final String TIMEOUT = "Timeout da conexão. Servidor não está respondendo.";

    private final String url;

    ServerProblemException (String url, Throwable cause) {
        super(MESSAGE + url, cause);
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    private String getCauseDescription(Throwable cause) {

        if (cause instanceof UnknownHostException)
            return UNKNOWN_HOST;

        if (cause instanceof SocketTimeoutException)
            return TIMEOUT;

        return UNEXPECTED_BEHAVIOR;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " " + getCauseDescription(getCause());
    }
}

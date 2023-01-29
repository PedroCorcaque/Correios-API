package br.com.power.correios.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason="Serviço está em instalação. Favor aguarde de 3 a 5 minutos.") // 503
public class NotReadyException extends Exception {
}

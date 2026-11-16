package br.com.fiap.vagai.handler;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ErroServlet implements ErrorController {
    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> erro(HttpServletRequest request) {
        Object valor = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int status = valor instanceof Integer codigo ? codigo : 404;
        return Erros.resposta(status, status >= 500 ? "Falha interna ao atender a requisição." : "Requisição não atendida.");
    }
}

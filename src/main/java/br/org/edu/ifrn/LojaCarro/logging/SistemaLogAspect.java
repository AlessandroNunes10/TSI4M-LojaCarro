package br.org.edu.ifrn.LojaCarro.logging;

import br.org.edu.ifrn.LojaCarro.integration.UsuarioApiClient;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SistemaLogAspect {
    private static final Logger log = LoggerFactory.getLogger(SistemaLogAspect.class);
    private final UsuarioApiClient usuarioApiClient;
    private final HttpServletRequest request;

    public SistemaLogAspect(UsuarioApiClient usuarioApiClient, HttpServletRequest request) {
        this.usuarioApiClient = usuarioApiClient;
        this.request = request;
    }

    @Around("execution(* br.org.edu.ifrn.LojaCarro.controllers.CarroController.*(..))")
    public Object registrarOperacao(ProceedingJoinPoint joinPoint) throws Throwable {
        String operacao = joinPoint.getSignature().toShortString();
        Long usuarioId = obterUsuarioId();
        String usuarioNome = usuarioApiClient.buscarNome(usuarioId);
        log.info("INICIO_OPERACAO {} usuarioId={} usuarioNome={}", operacao, usuarioId, usuarioNome);
        try {
            Object resultado = joinPoint.proceed();
            log.info("FIM_OPERACAO {} status=SUCESSO usuarioId={} usuarioNome={}", operacao, usuarioId, usuarioNome);
            return resultado;
        } catch (Throwable ex) {
            log.error("FIM_OPERACAO {} status=ERRO usuarioId={} usuarioNome={} mensagem={}", operacao, usuarioId, usuarioNome, ex.getMessage(), ex);
            throw ex;
        }
    }

    private Long obterUsuarioId() {
        String valor = request.getHeader("X-Usuario-Id");
        if (valor == null || valor.isBlank()) return null;
        try { return Long.parseLong(valor); }
        catch (NumberFormatException ex) {
            log.warn("ID_USUARIO_INVALIDO valor={}", valor);
            return null;
        }
    }
}
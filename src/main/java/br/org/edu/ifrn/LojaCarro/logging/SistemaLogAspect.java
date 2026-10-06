package br.org.edu.ifrn.LojaCarro.logging;

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
    @Around("execution(* br.org.edu.ifrn.LojaCarro.controllers..*(..))")
    public Object registrarOperacao(ProceedingJoinPoint joinPoint) throws Throwable {
        String operacao = joinPoint.getSignature().toShortString();
        log.info("INICIO_OPERACAO {}", operacao);
        try {
            Object resultado = joinPoint.proceed();
            log.info("FIM_OPERACAO {} status=SUCESSO", operacao);
            return resultado;
        } catch (Throwable ex) {
            log.error("FIM_OPERACAO {} status=ERRO mensagem={}", operacao, ex.getMessage(), ex);
            throw ex;
        }
    }
}
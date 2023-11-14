package nsu.ccfit.ru.mikhalev.task3.requestparamaspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.*;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class RequestParamsAspect {

        @Around("@annotation(nsu.ccfit.ru.mikhalev.task3.requestparamaspect.ExtractedParams)")
        public Object extractParams(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("test aspect");

            Object[] args = joinPoint.getArgs();

            ServerRequest request = (ServerRequest) args[0];

            String name = request.queryParam("name")
                .orElseThrow(() -> new IllegalArgumentException("Отсутствует параметр name"));
            String lang = request.queryParam("lang")
                .orElseThrow(() -> new IllegalArgumentException("Отсутствует параметр lang"));

            String[] s = new String[] { name, lang };

            Object[] modifiedArgs = {args[0], s};

            log.info("modified Args before proceeding: {} {}", name, lang);

            return  joinPoint.proceed(modifiedArgs);
        }
}
package nsu.ccfit.ru.mikhalev.task3.requestparamaspect;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.task3.exception.QueryArgumentException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

@Slf4j
@Aspect
@Component
public class RequestParamsAspect {

        @Around("@annotation(nsu.ccfit.ru.mikhalev.task3.requestparamaspect.ExtractedNameLangParams)")
        public Object extractedNameLangParams(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("test aspect");

            Object[] args = joinPoint.getArgs();

            ServerRequest request = (ServerRequest) args[0];

            String name = request.queryParam("name")
                .orElseThrow(() -> new QueryArgumentException("name"));
            String lang = request.queryParam("lang")
                .orElseThrow(() -> new QueryArgumentException("lang"));

            Object[] modifiedArgs = {args[0], new String[] {name, lang}};
            log.info("modified args before proceeding: {} {}", name, lang);
            return  joinPoint.proceed(modifiedArgs);
        }
}
package nsu.ccfit.ru.mikhalev.task3.requestparamaspect;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.task3.exception.QueryArgumentException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import static nsu.ccfit.ru.mikhalev.task3.context.ContextParamRequest.Index.INDEX_SERVER_REQUEST;
import static nsu.ccfit.ru.mikhalev.task3.context.ContextParamRequest.Str.*;

@Slf4j
@Aspect
@Component
public class RequestParamsAspect {

        @Around("@annotation(nsu.ccfit.ru.mikhalev.task3.requestparamaspect.annotation.ExtractedNameLangParams)")
        public Object extractedNameLangParams(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("find places geocode");

            ServerRequest request = (ServerRequest) joinPoint.getArgs()[INDEX_SERVER_REQUEST];

            String name = request.queryParam(NAME)
                .orElseThrow(() -> new QueryArgumentException(NAME));
            String lang = request.queryParam(LANG)
                .orElseThrow(() -> new QueryArgumentException(LANG));

            Object[] modifiedArgs = {request, new String[] {name, lang}};
            log.info("modified args before proceeding: {} {}", name, lang);
            return  joinPoint.proceed(modifiedArgs);
        }
}
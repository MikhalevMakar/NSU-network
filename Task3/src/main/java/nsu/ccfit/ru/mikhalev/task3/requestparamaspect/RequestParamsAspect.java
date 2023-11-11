package nsu.ccfit.ru.mikhalev.task3.requestparamaspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import java.util.Optional;

@Slf4j
@Aspect
@Component
public class RequestParamsAspect {

    @Before("@annotation(nsu.ccfit.ru.mikhalev.task3.requestparamaspect.ExtractedParams)")
    public void extractParams(JoinPoint joinPoint){
        log.info("aspect");

//        for (Object arg : joinPoint.getArgs()) {
//            if (arg instanceof ServerRequest) {
//                ServerRequest request = (ServerRequest) arg;
//                for (MethodParameter parameter : request.queryParam(ExtractedParams.class)) {
//                    if (parameter.getParameterType ().equals (String.class)) {
//                        String paramName = parameter.getParameterName ();
//                        Optional<String> paramValue = request.queryParam (paramName);
//                        if (paramValue.isPresent ()) {
//                            joinPoint.getArgs ()[parameter.getParameterIndex ()] = paramValue.get ();
//                        } else {
//                            throw new IllegalArgumentException ("Отсутствует параметр: " + paramName);
//                        }
//                    }
//                }
//                break;
//            }
//        }
    }
}
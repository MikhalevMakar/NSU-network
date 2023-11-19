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

        @Around("@annotation(nsu.ccfit.ru.mikhalev.task3.requestparamaspect.annotation.ExtractedPlacesGeocode)")
        public Object extractedNameParams(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("find places geocode");

            ServerRequest request = (ServerRequest) joinPoint.getArgs()[INDEX_SERVER_REQUEST];

            String name = request.queryParam(NAME)
                .orElseThrow(() -> new QueryArgumentException(NAME));

            Object[] modifiedArgs = {request, new String[] {name}};
            log.info("modified args before proceeding: {}", name);
            return  joinPoint.proceed(modifiedArgs);
        }

    @Around("@annotation(nsu.ccfit.ru.mikhalev.task3.requestparamaspect.annotation.ExtractedInfoByCoord)")
    public Object extractedLatLonParams(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("find info by coord");

        ServerRequest request = (ServerRequest) joinPoint.getArgs()[INDEX_SERVER_REQUEST];

        double lat = Double.parseDouble(request.queryParam(LAT)
                            .orElseThrow(() -> new QueryArgumentException(LAT)));

        double lon = Double.parseDouble(request.queryParam(LON)
                            .orElseThrow(() -> new QueryArgumentException(LON)));

        Object[] modifiedArgs = {request, new Double[] {lat, lon}};
        log.info("modified args before proceeding: {} {}", lat, lon);
        return  joinPoint.proceed(modifiedArgs);
    }
}
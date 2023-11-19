package nsu.ccfit.ru.mikhalev.task3.requestparamaspect.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtractedInfoByCoord {}
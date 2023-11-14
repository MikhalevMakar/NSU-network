package nsu.ccfit.ru.mikhalev.task3.requestparamaspect;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtractedParams {}
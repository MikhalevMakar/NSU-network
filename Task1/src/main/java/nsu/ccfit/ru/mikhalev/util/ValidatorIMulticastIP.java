package nsu.ccfit.ru.mikhalev.util;

//import jakarta.validation.ConstraintValidator;
//import jakarta.validation.ConstraintValidatorContext;
//import lombok.extern.slf4j.Slf4j;
//import nsu.ccfit.ru.mikhalev.custom_annotation.MulticastIP;
//
//import java.util.regex.Pattern;
//
//@Slf4j
//public class ValidatorIMulticastIP implements ConstraintValidator<MulticastIP, String> {
//    private static final String IP_REGEX_V4 = "(22[4-9]|23[0-9]|239)\\" +
//                                              ".(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\" +
//                                              ".(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\" +
//                                              ".(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
//
//    private static final String IP_REGEX_V6 = "^ff[0-7][0-f]{1,3}" +
//                                              ":[0-f]{1,4}:[0-f]{1,4}" +
//                                              ":[0-f]{1,4}:[0-f]{1,4}" +
//                                              ":[0-f]{1,4}:[0-f]{1,4}" +
//                                              ":[0-f]{1,4}$\n";
//
//    @Override
//    public boolean isValid(String ip, ConstraintValidatorContext context) {
//        log.info("check id {} on multicast group", ip);
//        return ip != null && (Pattern.matches(IP_REGEX_V4, ip) || Pattern.matches(IP_REGEX_V6, ip));
//    }
//}

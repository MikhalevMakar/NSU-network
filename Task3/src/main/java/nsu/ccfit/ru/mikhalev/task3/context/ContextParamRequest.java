package nsu.ccfit.ru.mikhalev.task3.context;

public final class ContextParamRequest {

    private ContextParamRequest() {
        throw new IllegalStateException("utility class");
    }

    public static class Index {

        private Index() {
            throw new IllegalStateException("utility class");
        }

        public static final int INDEX_NAME = 0;

        public static final int INDEX_LAT = 0;

        public static final int INDEX_LON = 1;

        public static final int INDEX_SERVER_REQUEST = 0;
    }

    public static class Str {

        private Str() {
            throw new IllegalStateException("utility class");
        }

        public static final String LAT = "lat";

        public static final String LON = "lon";

        public static final String NAME = "name";

        public static final String API_KEY = "apikey";
    }
}

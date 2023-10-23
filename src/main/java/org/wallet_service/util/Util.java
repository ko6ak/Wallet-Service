package org.wallet_service.util;

import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Общий класс для различных методов.
 */
public final class Util {
    private Util() {
    }

    /**
     * Метод получает JSON из запроса в сервлете.
     * @param req запрос от клиента.
     * @return стоку содержащую JSON.
     * @throws IOException в случае проблем с чтением из потоков.
     */
    public static String getJSONFromRequest(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        try(BufferedReader reader = req.getReader()) {
            String input;
            while ((input = reader.readLine()) != null) {
                sb.append(input).append('\n');
            }
        }
        return sb.toString();
    }
}

package org.wallet_service.util;

import java.math.BigDecimal;
import java.util.*;

/**
 * Класс отвечает за валидацию входных данных.
 */
public class Validator {
    Map<String, List<String>> result = new HashMap<>();

    public Map<String, List<String>> getResult() {
        return result;
    }

    /**
     * Метод проверки входного значения на не пустоту.
     * @param input Пара: имя параметра, значение параметра.
     */
    public void checkNotBlank(Map<String, String> input){
        for (Map.Entry<String, String> me : input.entrySet()){
            if (me.getValue().isBlank()) {
                result.computeIfAbsent(me.getKey(), k -> new ArrayList<>());
                result.get(me.getKey()).add("не должно быть пустым");
            }
        }
    }

    /**
     * Метод проверки входного значения на количество символов. Значение должно быть от 2 до 50 символов.
     * @param input Пара: имя параметра, значение параметра.
     */
    public void checkSize(Map<String, String> input){
        for (Map.Entry<String, String> me : input.entrySet()){
            if (me.getValue().length() < 2 || me.getValue().length() > 50) {
                result.computeIfAbsent(me.getKey(), k -> new ArrayList<>());
                result.get(me.getKey()).add("длина должна быть от 2 до 50 символов");
            }
        }
    }

    /**
     * Метод проверки входного значения на количество символов. Используется для пароля. Значение должно быть от 5 до 32 символов.
     * @param input Пара: имя параметра, значение параметра.
     */
    public void checkSizePassword(Map<String, String> input){
        for (Map.Entry<String, String> me : input.entrySet()){
            if (me.getValue().length() < 5 || me.getValue().length() > 32) {
                result.computeIfAbsent(me.getKey(), k -> new ArrayList<>());
                result.get(me.getKey()).add("длина должна быть от 5 до 32 символов");
            }
        }
    }

    /**
     * Метод проверки входного значения на валидный email.
     * @param input Пара: имя параметра, значение параметра.
     */
    public void checkEmail(Map<String, String> input){
        for (Map.Entry<String, String> me : input.entrySet()){
            if (!me.getValue().matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                    + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {
                result.computeIfAbsent(me.getKey(), k -> new ArrayList<>());
                result.get(me.getKey()).add("неправильный email");
            }
        }
    }

    /**
     * Метод проверки входного значения на денежный формат.
     * @param input Пара: имя параметра, значение параметра.
     */
    public void checkFormat(Map<String, String> input){
        for (Map.Entry<String, String> me : input.entrySet()){
            if (!me.getValue().matches("\\d+\\.\\d{2}")) {
                result.computeIfAbsent(me.getKey(), k -> new ArrayList<>());
                result.get(me.getKey()).add("должно соответствовать формату 0.00");
            }
        }
    }

    /**
     * Метод проверки входного значения на то, что оно должно быть больше 0.01.
     * @param input Пара: имя параметра, значение параметра.
     */
    public void checkMinDecimal(Map<String, String> input){
        for (Map.Entry<String, String> me : input.entrySet()){
            if (new BigDecimal(me.getValue()).compareTo(new BigDecimal("0.00")) <= 0) {
                result.computeIfAbsent(me.getKey(), k -> new ArrayList<>());
                result.get(me.getKey()).add("должно быть больше или равно 0.01");
            }
        }
    }
}

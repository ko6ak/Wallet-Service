package org.wallet_service.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

/**
 * Класс получает параметры, указанные в файле liquibase.properties.
 */
public final class ConfigParser {
    public static String changeLogFile;
    public static String driver;
    public static String url;
    public static String username;
    public static String password;

    private ConfigParser() {
    }

    /**
     * Метод получает значения параметров changeLogFile, driver, url, username и password из файла src/main/resources/liquibase.properties.
     */
    public static void parse(){
        String file = Objects.requireNonNull(ConfigParser.class.getClassLoader().getResource("liquibase.properties")).getPath();

        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            while (br.ready()){
                String[] input = br.readLine().split("=");
                switch (input[0]){
                    case ("changeLogFile") -> changeLogFile = input[1];
                    case ("driver") -> driver = input[1];
                    case ("url") -> url = input[1];
                    case ("username") -> username = input[1];
                    case ("password") -> password = input[1];
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean configIsNull(){
        return changeLogFile == null && driver == null && url == null && username == null && password == null;
    }
}

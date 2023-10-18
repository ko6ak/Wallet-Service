package org.wallet_service.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Класс получает параметры, указанные в файле liquibase.properties.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConfigParser {
    public static String changeLogFile;
    public static String driver;
    public static String url;
    public static String username;
    public static String password;

    /**
     * Метод получает значения параметров changeLogFile, driver, url, username и password из файла src/main/resources/liquibase.properties.
     */
    public static void parse(){
        try(BufferedReader br = new BufferedReader(new FileReader("src/main/resources/liquibase.properties"))){
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
}

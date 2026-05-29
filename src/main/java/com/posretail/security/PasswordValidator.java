package com.posretail.security;

import java.util.regex.Pattern;

/**
 * Reglas del PDF:
 *  - Más de 7 y menos de 12 caracteres (es decir, longitud entre 8 y 11).
 *  - Debe contener mayúsculas, minúsculas, números y caracteres especiales.
 */
public class PasswordValidator {

    private static final Pattern UPPER   = Pattern.compile("[A-Z]");
    private static final Pattern LOWER   = Pattern.compile("[a-z]");
    private static final Pattern DIGIT   = Pattern.compile("\\d");
    private static final Pattern SPECIAL = Pattern.compile("[^A-Za-z0-9]");

    public static String validate(String pwd) {
        if (pwd == null) return "La contraseña es obligatoria";
        int len = pwd.length();
        if (len <= 7 || len >= 12)
            return "La contraseña debe tener entre 8 y 11 caracteres";
        if (!UPPER.matcher(pwd).find())   return "Debe contener al menos una letra mayúscula";
        if (!LOWER.matcher(pwd).find())   return "Debe contener al menos una letra minúscula";
        if (!DIGIT.matcher(pwd).find())   return "Debe contener al menos un número";
        if (!SPECIAL.matcher(pwd).find()) return "Debe contener al menos un carácter especial";
        return null; // válida
    }

    public static boolean isValid(String pwd) {
        return validate(pwd) == null;
    }
}

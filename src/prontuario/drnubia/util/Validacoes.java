package prontuario.drnubia.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class Validacoes {

    public static boolean validarCPF(String cpf) {
        if (cpf == null) return false;

        cpf = cpf.replaceAll("\\D", "");


        if (cpf.length() != 11) return false;


        if (cpf.matches("(\\d)\\1{10}")) return false;

        try {
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += (cpf.charAt(i) - '0') * (10 - i);
            }
            int resto = soma % 11;
            int digito1 = (resto < 2) ? 0 : 11 - resto;

            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += (cpf.charAt(i) - '0') * (11 - i);
            }
            resto = soma % 11;
            int digito2 = (resto < 2) ? 0 : 11 - resto;

            return digito1 == (cpf.charAt(9) - '0') && digito2 == (cpf.charAt(10) - '0');
        } catch (Exception e) {
            return false;
        }
    }


    public static String formatarCPF(String cpf) {
        if (cpf == null) return null;

        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11) return cpf; 

        return cpf.substring(0, 3) + "." +
               cpf.substring(3, 6) + "." +
               cpf.substring(6, 9) + "-" +
               cpf.substring(9, 11);
    }

    public static boolean validarData(String data) {
        if (data == null) return false;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate.parse(data, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static String formatarData(String dataISO) {
        if (dataISO == null) return null;

        try {
            LocalDate date = LocalDate.parse(dataISO); 
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return date.format(formatter);
        } catch (DateTimeParseException e) {
            return dataISO; 
        }
    }

   
    public static boolean validarNome(String nome) {
        if (nome == null) return false;

        String regex = "^[A-Za-zÀ-ú ]{3,100}$";

        return Pattern.matches(regex, nome);
    }

}

package com.example.kaeuc.smartufpa.utils;

import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.widget.EditText;

import com.example.kaeuc.smartufpa.R;

import java.util.Arrays;

/**
 * Created by kaeuc on 16/03/2017.
 */

public class InputParser {

    private static final String TAG = InputParser.class.getSimpleName();

    /**
     * Método para avaliar uma view do tipo input que deve conter apenas caracteres alfanuméricos.
     *
     * @param inputEditText View do tipo TextInputEditText.
     * @param maxLength Tamanho máximo da string permitida no input.
     * @param minLength Tamanho mínimo da string permitida no input
     * @return true se passou por todas as avaliações.
     */  
    public static boolean validateNoSpecialChar(TextInputEditText inputEditText, int maxLength, int minLength)
            throws EmptyInputException, ExtenseInputException, ShortInputException, InvalidCharacterException {
        if(inputEditText.getText().length() == 0)   throw new EmptyInputException();

        else if(inputEditText.getText().length() > maxLength)   throw new ExtenseInputException();

        else if(inputEditText.getText().length() < minLength)   throw new ShortInputException();

        else if (!inputEditText.getText().toString().matches("[a-zA-Z0-9\\s]+")) throw new InvalidCharacterException();

        return true;
    }

    /**
     * Método para avaliar uma view do tipo input sem restrição de tipos de caracteres.
     *
     * @param inputEditText View do tipo TextInputEditText.
     * @param maxLength Tamanho máximo da string permitida no input.
     * @param minLength Tamanho mínimo da string permitida no input
     * @return true Se passou por todas as avaliações.
     */
    public static boolean validateRegularText(TextInputEditText inputEditText, int maxLength, int minLength)
            throws EmptyInputException, ExtenseInputException, ShortInputException{
        if(inputEditText.getText().length() == 0)   throw new EmptyInputException();

        else if(inputEditText.getText().length() > maxLength)   throw new ExtenseInputException();

        else if(inputEditText.getText().length() < minLength)   throw new ShortInputException();

        return true;
    }



    public static String parseInputString(String inputString){
        String noDoubleSpaces = inputString.replaceAll("\\s+", " ");
        String[] strings = noDoubleSpaces.split("\\s");
        String finalString = "";
        //Capitaliza primeira letra de palavras com mais de 2 letras
        for (int i = 0; i < strings.length; i++) {
            if(strings[i].length() > 2){
                strings[i] = strings[i].substring(0, 1).toUpperCase() + strings[i].substring(1);
            }
            finalString += strings[i] + " ";
        }
        return finalString.substring(0,finalString.length()-1);
    }

    public static class EmptyInputException extends Exception{
        private EmptyInputException(){
            super("Your input is empty.");
        }
    }

    public static class ExtenseInputException extends Exception{
        private ExtenseInputException() {
            super("Your input has more characters than the maximum specified.");
        }
    }

    public static class ShortInputException extends Exception{
        private ShortInputException() {
            super("Your input has less characters than the minimum specified.");
        }
    }

    public static class InvalidCharacterException extends Exception {
        private InvalidCharacterException(){
            super("Your input has invalid characters. Only letters and numbers are allowed.");
        }
    }
}

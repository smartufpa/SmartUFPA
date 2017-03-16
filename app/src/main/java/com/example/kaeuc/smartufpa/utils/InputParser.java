package com.example.kaeuc.smartufpa.utils;

import android.support.design.widget.TextInputEditText;
import android.widget.EditText;

import com.example.kaeuc.smartufpa.R;

/**
 * Created by kaeuc on 16/03/2017.
 */

public class InputParser {
    /**
     * Método para avaliar uma view do tipo input
     *
     * @param editText View do tipo TextInputEditText.
     * @param maxLength Tamanho máximo da string permitida no input.
     * @param minLength Tamanho mínimo da string permitida no input
     * @return true Se passou por todas as avaliações
     */
    public static boolean validateInput(EditText editText, int maxLength, int minLength)
            throws EmptyInputException,ExtenseInputException,ShortInputException{
        if(editText.getText().length() == 0)   throw new EmptyInputException();

        else if(editText.getText().length() > maxLength)   throw new ExtenseInputException();

        else if(editText.getText().length() < minLength)   throw new ShortInputException();

        return true;
    }

    /**
     * Método para avaliar uma view do tipo input
     *
     * @param inputEditText View do tipo TextInputEditText.
     * @param maxLength Tamanho máximo da string permitida no input.
     * @param minLength Tamanho mínimo da string permitida no input
     * @return true Se passou por todas as avaliações
     */  
    public static boolean validateInput(TextInputEditText inputEditText, int maxLength, int minLength)
            throws EmptyInputException,ExtenseInputException,ShortInputException{
        if(inputEditText.getText().length() == 0)   throw new EmptyInputException();

        else if(inputEditText.getText().length() > maxLength)   throw new ExtenseInputException();

        else if(inputEditText.getText().length() < minLength)   throw new ShortInputException();

        return true;
    }



    public static String parseInputString(String inputString){
        //TODO: parse mais completo
        return inputString.replaceAll("\\s+", " ");
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
    
}

package controller;

public class Validador {

    public static class ValidacaoException extends Exception {
        public ValidacaoException(String mensagem) {
            super(mensagem);
        }
    }

    // Para campos inteiros 
    public static int parseInteiroNaoNegativo(String texto, String nomeCampo) throws ValidacaoException {
        int valor;
        try {
            valor = Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new ValidacaoException("Caractere inválido, digite apenas números no campo " + nomeCampo + "!");
        }
        if (valor < 0) {
            throw new ValidacaoException("Número inválido, " + nomeCampo + " precisa ser maior ou igual a 0!");
        }
        return valor;
    }

    // Para campos decimais
    public static double parseDecimalNaoNegativo(String texto, String nomeCampo) throws ValidacaoException {
        double valor;
        try {
            valor = Double.parseDouble(texto.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new ValidacaoException("Caractere inválido, digite apenas números no campo " + nomeCampo + "!");
        }
        if (valor < 0) {
            throw new ValidacaoException("Número inválido, " + nomeCampo + " precisa ser maior ou igual a 0!");
        }
        return valor;
    }
}
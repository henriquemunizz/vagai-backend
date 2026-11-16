package br.com.fiap.vagai.exception;

public class SemPermissao extends RuntimeException {
    public SemPermissao() {
        super("Operação restrita ao proprietário.");
    }
}

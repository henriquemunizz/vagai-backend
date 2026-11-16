package br.com.fiap.vagai.exception;

public class NaoEncontrado extends RuntimeException {
    public NaoEncontrado() {
        super("Registro não encontrado.");
    }
}

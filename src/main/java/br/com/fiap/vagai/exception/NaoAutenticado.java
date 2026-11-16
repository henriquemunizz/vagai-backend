package br.com.fiap.vagai.exception;

public class NaoAutenticado extends RuntimeException {
    public NaoAutenticado() {
        super("Autenticação necessária ou credenciais inválidas.");
    }
}

package io.github.Eduardo_Port.userhubapi.exceptions;

public class EmailAlreadyUsed extends RuntimeException {
    public EmailAlreadyUsed() {
        super("O email deve ser único, esse email já está em registrado em outra conta.");
    }
}

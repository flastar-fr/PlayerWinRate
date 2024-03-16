package fr.flastar.programme;

public class PlayerAttributeException extends IllegalArgumentException {

    public PlayerAttributeException() {
        this("Invalid health : health must be a positive number");
    }

    public PlayerAttributeException(String message) {
        super(message);
    }
}

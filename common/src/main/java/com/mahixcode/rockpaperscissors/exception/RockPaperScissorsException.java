package com.mahixcode.rockpaperscissors.exception;

public abstract class RockPaperScissorsException extends RuntimeException {
    protected RockPaperScissorsException(String message) {
        super(message);
    }

    protected RockPaperScissorsException(String message, Throwable cause) {
        super(message, cause);
    }

    protected RockPaperScissorsException(Throwable cause) {
        super(cause);
    }
}

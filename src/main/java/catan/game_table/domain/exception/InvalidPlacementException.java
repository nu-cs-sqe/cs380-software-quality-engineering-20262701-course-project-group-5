package catan.game_table.domain.exception;

public class InvalidPlacementException extends RuntimeException {
  public InvalidPlacementException(String message) {
    super(message);
  }
}

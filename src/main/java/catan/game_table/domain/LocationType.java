package catan.game_table.domain;

public enum LocationType {
  VERTEX("vertex"),
  EDGE("edge"),
  HEX("hex");

  private final String label;

  LocationType(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return label;
  }
}

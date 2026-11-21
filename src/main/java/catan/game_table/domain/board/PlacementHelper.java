package catan.game_table.domain.board;

import catan.game_table.domain.Color;

import java.util.ArrayList;
import java.util.List;

public class PlacementHelper {

  public List<Vertex> getValidBuildingSpots(Color color) {
    return new ArrayList<>();
  }

  public List<Edge> getValidRoadSpots(Color color) {
    return new ArrayList<>();
  }

  public List<Hex> getValidRobberSpots() {
    return new ArrayList<>();
  }
}

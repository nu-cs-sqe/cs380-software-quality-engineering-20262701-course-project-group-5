package catan.game_table.domain.board;

import catan.game_table.domain.Color;
import catan.game_table.domain.GamePhase;
import catan.game_table.domain.GamePhaseHolder;

import java.util.Set;

public class PlacementHelper {

    private final GameBoard gameBoard;
    private final GamePhaseHolder gamePhaseHolder;

    public PlacementHelper(GameBoard gameBoard, GamePhaseHolder gamePhaseHolder) {
        this.gameBoard = gameBoard;
        this.gamePhaseHolder = gamePhaseHolder;
    }

    public Set<Vertex> getValidBuildingSpots(Color color) {
        GamePhase gamePhase = gamePhaseHolder.getGamePhase();
        return gameBoard.getLandVertices();
    }

}

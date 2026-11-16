package catan.game_table.domain.board;

import catan.game_table.domain.Color;
import catan.game_table.domain.GamePhase;
import catan.game_table.domain.GamePhaseHolder;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlacementHelperTests {

    private final Set<Vertex> landVertices = makeLandVerticesSet();

    @Test
    public void GetValidBuildingSpots_GivenRed_AndSetupPhase_AndEmptyBoard_ExpectAllLandVertices() {

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);
        EasyMock.expect(gameBoard.getLandVertices()).andReturn(landVertices);
        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(GamePhase.SETUP);
        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(Color.RED);
        Set<Vertex> expectedVertices = makeLandVerticesSet();
        Set<Point> actualPoints = actualVertices.stream().map(Vertex::getPoint).collect(Collectors.toSet());
        Set<Point> expectedPoints = expectedVertices.stream().map(Vertex::getPoint).collect(Collectors.toSet());

        assertTrue(allPointsEqual(expectedPoints, actualPoints));
        EasyMock.verify(gameBoard, gamePhaseHolder);

    }

    @Test
    public void GetValidBuildingSpots_GivenRed_AndRegularPhase_AndEmptyBoard_ExpectNoVertices() {

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);
        EasyMock.expect(gameBoard.getLandVertices()).andReturn(landVertices);
        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(GamePhase.REGULAR);
        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(Color.RED);
        Set<Vertex> expectedVertices = new HashSet<>();
        Set<Point> actualPoints = actualVertices.stream().map(Vertex::getPoint).collect(Collectors.toSet());
        Set<Point> expectedPoints = expectedVertices.stream().map(Vertex::getPoint).collect(Collectors.toSet());

        assertTrue(allPointsEqual(expectedPoints, actualPoints));
        EasyMock.verify(gameBoard, gamePhaseHolder);

    }

    private Set<Vertex> makeLandVerticesSet() {

        Set<Vertex> landVertices = new HashSet<>();

        for (int j = 0; j < 3; j++) {
            for (int i = j + 2; i < 13 - j; i++) {
                Vertex vertex = EasyMock.createMock(Vertex.class);
                EasyMock.expect(vertex.getPoint()).andStubReturn(new Point(i, -j));
                EasyMock.replay(vertex);
                landVertices.add(vertex);
            }
            for (int i = j + 2; i < 13 - j; i++) {
                Vertex vertex = EasyMock.createMock(Vertex.class);
                EasyMock.expect(vertex.getPoint()).andStubReturn(new Point(i, j + 1));
                EasyMock.replay(vertex);
                landVertices.add(vertex);
            }
        }

        return landVertices;

    }

    private boolean pointsEqual(Point p1, Point p2) {
        return p1.x == p2.x && p1.y == p2.y;
    }

    private boolean allPointsEqual(Set<Point> set1, Set<Point> set2) {

        if (set1.size() != set2.size()) {
            return false;
        }

        Set<Point> set1Copy = new HashSet<>(set1);
        Set<Point> set2Copy = new HashSet<>(set2);
        for (Point p1 : set1Copy) {
            if (!set2Copy.removeIf(p2 -> pointsEqual(p1, p2))) {
                return false;
            }
        }

        return set2Copy.isEmpty();

    }

}

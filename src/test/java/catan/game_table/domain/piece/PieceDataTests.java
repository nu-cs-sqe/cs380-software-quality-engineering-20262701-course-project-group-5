package catan.game_table.domain.piece;

import catan.game_table.domain.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PieceDataTests {
    private static final String CITY = "City";
    private static final String SETTLEMENT = "Settlement";
    private static final String HARBOR = "Harbor";
    private static final String ROAD = "Road";
    @Test
    public void Equals_TwoCity_BothBlue_True() {
        PieceData firstCityData = new PieceData(Color.BLUE, CITY);
        PieceData secondCityData = new PieceData(Color.BLUE, CITY);

        assertTrue(firstCityData.equals(secondCityData));
    }

    @Test
    public void Equals_TwoSettlement_BothOrange_True() {
        PieceData firstSettlementData = new PieceData(Color.ORANGE, SETTLEMENT);
        PieceData secondSettlementData = new PieceData(Color.ORANGE, SETTLEMENT);

        assertTrue(firstSettlementData.equals(secondSettlementData));
    }

    @Test
    public void Equals_TwoHarbor_BothRed_True() {
        PieceData firstHarborData = new PieceData(Color.RED, HARBOR);
        PieceData secondHarborData = new PieceData(Color.RED, HARBOR);

        assertTrue(firstHarborData.equals(secondHarborData));
    }

    @Test
    public void Equals_TwoRoad_BothWhite_True() {
        PieceData firstRoadData = new PieceData(Color.WHITE, ROAD);
        PieceData secondRoadData = new PieceData(Color.WHITE, ROAD);

        assertTrue(firstRoadData.equals(secondRoadData));
    }

    @Test
    public void Equals_OrangeHarbor_BlueHarbor_False() {
        PieceData firstHarborData = new PieceData(Color.ORANGE, HARBOR);
        PieceData secondHarborData = new PieceData(Color.BLUE, HARBOR);

        assertFalse(firstHarborData.equals(secondHarborData));
    }
}

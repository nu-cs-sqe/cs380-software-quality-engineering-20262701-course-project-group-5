package catan.game_table.domain.piece;

import catan.game_table.domain.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PieceDataTests {
    private static final String CITY = "City";
    private static final String SETTLEMENT = "Settlement";
    private static final String HARBOR = "Harbor";
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

}

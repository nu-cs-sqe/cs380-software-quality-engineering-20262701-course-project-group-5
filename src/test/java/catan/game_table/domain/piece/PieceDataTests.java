package catan.game_table.domain.piece;

import catan.game_table.domain.Color;
import catan.game_table.domain.PieceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PieceDataTests {

    // equals()
    @Test
    public void Equals_TwoCity_BothBlue_True() {
        PieceData firstCityData = new PieceData(Color.BLUE, PieceType.CITY);
        PieceData secondCityData = new PieceData(Color.BLUE, PieceType.CITY);

        assertTrue(firstCityData.equals(secondCityData));
    }

    @Test
    public void Equals_TwoSettlement_BothOrange_True() {
        PieceData firstSettlementData = new PieceData(Color.ORANGE, PieceType.SETTLEMENT);
        PieceData secondSettlementData = new PieceData(Color.ORANGE, PieceType.SETTLEMENT);

        assertTrue(firstSettlementData.equals(secondSettlementData));
    }

    @Test
    public void Equals_TwoHarbor_BothRed_True() {
        PieceData firstHarborData = new PieceData(Color.RED, PieceType.HARBOR);
        PieceData secondHarborData = new PieceData(Color.RED, PieceType.HARBOR);

        assertTrue(firstHarborData.equals(secondHarborData));
    }
}

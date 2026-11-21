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

    @Test
    public void Equals_TwoRoad_BothWhite_True() {
        PieceData firstRoadData = new PieceData(Color.WHITE, PieceType.ROAD);
        PieceData secondRoadData = new PieceData(Color.WHITE, PieceType.ROAD);

        assertTrue(firstRoadData.equals(secondRoadData));
    }

    @Test
    public void Equals_NoColorRobber_WhiteRoad_False() {
        PieceData robberData = new PieceData(Color.NONE, PieceType.ROBBER);
        PieceData roadData = new PieceData(Color.WHITE, PieceType.ROAD);

        assertFalse(robberData.equals(roadData));
    }

    @Test
    public void Equals_WhiteCity_RedSettlement_False() {
        PieceData cityData = new PieceData(Color.WHITE, PieceType.CITY);
        PieceData settlementData = new PieceData(Color.RED, PieceType.SETTLEMENT);

        assertFalse(cityData.equals(settlementData));
    }

    @Test
    public void Equals_OrangeRoad_BlueHarbor_False() {
        PieceData roadData = new PieceData(Color.ORANGE, PieceType.ROAD);
        PieceData harborData = new PieceData(Color.BLUE, PieceType.HARBOR);

        assertFalse(roadData.equals(harborData));
    }

    @Test
    public void Equals_RedSettlement_RedRoad_False() {
        PieceData settlementData = new PieceData(Color.RED, PieceType.SETTLEMENT);
        PieceData roadData = new PieceData(Color.RED, PieceType.ROAD);

        assertFalse(settlementData.equals(roadData));
    }

    @Test
    public void Equals_OrangeHarbor_BlueHarbor_False() {
        PieceData firstHarborData = new PieceData(Color.ORANGE, PieceType.HARBOR);
        PieceData secondHarborData = new PieceData(Color.BLUE, PieceType.HARBOR);

        assertFalse(firstHarborData.equals(secondHarborData));
    }

    // hashCode()
    @Test
    public void HashCode_BlueCity_BlueCity_Equals(){
        PieceData firstCityData = new PieceData(Color.BLUE, PieceType.CITY);
        PieceData secondCityData = new PieceData(Color.BLUE, PieceType.CITY);

        int firstCityHash = firstCityData.hashCode();
        int secondCityHash = secondCityData.hashCode();

        assertEquals(firstCityHash, secondCityHash);
    }
}

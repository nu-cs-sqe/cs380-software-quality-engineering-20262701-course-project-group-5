package catan.game_table.domain.piece;

import catan.game_table.domain.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PieceDataTests {
    private static final String CITY = "City";
    @Test
    public void Equals_TwoCity_BothBlue_True() {
        PieceData firstCityData = new PieceData(Color.BLUE, CITY);
        PieceData secondCityData = new PieceData(Color.BLUE, CITY);

        assertTrue(firstCityData.equals(secondCityData));
    }
}

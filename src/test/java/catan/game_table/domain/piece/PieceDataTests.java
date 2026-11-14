package catan.game_table.domain.piece;

public class PieceDataTests {
    @Test
    public void Equals_TwoCity_SameColor_True() {
        PieceData firstCityData = new PieceData(Color.BLUE, CITY);
        PieceData secondCityData = new PieceData(Color.BLUE, CITY);

        assertTrue(firstCityData.equals(secondCityData));
    }
}

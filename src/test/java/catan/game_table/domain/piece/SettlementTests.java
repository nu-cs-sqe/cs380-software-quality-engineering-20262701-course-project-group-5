package catan.game_table.domain.piece;

import org.junit.jupiter.api.Test;
import org.easymock.EasyMock;

public class SettlementTests {
    //produce(products : ResourceProducts)
    @Test
    public void Produce_BankHasNoInventory_NoChangeToPlayerInventory(){
        PieceData pieceData = EasyMock.createMock(PieceData.class);
        BankResourceDistributor bankResourceDistributor = EasyMock.createMock(BankResourceDistributor.class);
        ScoreTracker ownerVictoryPointsTracker = EasyMock.createMock(ScoreTracker.class);

        Settlement settlement = new Settlement(pieceData, bankResourceDistributor, ownerVictoryPointsTracker);

        bankResourceDistributor.produce(products);
        bankResourceDistributor.resourceInventory
        EasyMock.expectLastCall();
    }
}

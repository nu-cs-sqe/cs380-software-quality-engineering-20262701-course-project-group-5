package catan.game_table.domain.piece;

import catan.game_table.domain.resource.WoolResourceStack;
import org.junit.jupiter.api.Test;
import org.easymock.EasyMock;

import java.util.List;

public class SettlementTests {
    //produce(products : ResourceProducts)
    @Test
    public void ProduceResources_ValidProducts_VerifyCorrectFunctionCall(){
        PieceData pieceData = EasyMock.createMock(PieceData.class);
        BankResourceDistributor bankResourceDistributor = EasyMock.createMock(BankResourceDistributor.class);
        ScoreTracker ownerVictoryPointsTracker = EasyMock.createMock(ScoreTracker.class);
        List<WoolResourceStack> woolResourceStackList = EasyMock.createMock(WoolResourceStack.class);

        EasyMock.expect(pieceData.color).andStubReturn(Color.BLUE);
        WoolResourceStack singleWoolResourceStack = new WoolResourceStack(1);
        EasyMock.expect(woolResourceStackList.getFirst()).andStubReturn(singleWoolResourceStack);
        EasyMock.expect(singleWoolResourceStack.getSize()).andStubReturn(1);
        EasyMock.expect(singleWoolResourceStack.add(singleWoolResourceStack));

        EasyMock.replay(pieceData, bankResourceDistributor);

        Settlement settlement = new Settlement(pieceData, bankResourceDistributor, ownerVictoryPointsTracker);

        settlement.produceResource(woolResourceStackList);

        EasyMock.verify(bankResourceDistributor.produceResources(Color.BLUE, woolResourceStackList));
    }
}

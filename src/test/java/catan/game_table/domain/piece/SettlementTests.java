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
        ResourceStack resourceStack = EasyMock.createMock(ResourceStack.class);

        List<ResourceStack> resourceStackList = List.of(resourceStack);

        EasyMock.expect(pieceData.color).andReturn(Color.BLUE);
        WoolResourceStack singleWoolResourceStack = new WoolResourceStack(1);
        EasyMock.expect(resourceStack.getSize()).andStubReturn(1);
        EasyMock.expect(resourceStack.add(singleWoolResourceStack));

        EasyMock.replay(pieceData, bankResourceDistributor);

        Settlement settlement = new Settlement(pieceData, bankResourceDistributor, ownerVictoryPointsTracker);

        settlement.produceResources(resourceStackList);

        EasyMock.verify(bankResourceDistributor);
    }
}

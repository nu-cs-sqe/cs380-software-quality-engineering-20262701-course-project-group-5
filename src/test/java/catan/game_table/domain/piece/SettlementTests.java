package catan.game_table.domain.piece;

import catan.game_table.domain.Color;
import catan.game_table.domain.PieceType;
import catan.game_table.domain.ScoreTracker;
import catan.game_table.domain.bank.BankResourceDistributor;
import catan.game_table.domain.resource.ResourceStack;
import catan.game_table.domain.resource.WoolResourceStack;
import org.junit.jupiter.api.Test;
import org.easymock.EasyMock;

import java.util.Arrays;
import java.util.List;

public class SettlementTests {
    //produce(products : ResourceProducts)
    @Test
    public void ProduceResources_ValidProducts_VerifyCorrectFunctionCall(){
        PieceData pieceData = new PieceData(Color.BLUE, PieceType.SETTLEMENT);
        BankResourceDistributor bankResourceDistributor = EasyMock.createMock(BankResourceDistributor.class);
        ScoreTracker ownerVictoryPointsTracker = EasyMock.createMock(ScoreTracker.class);
        WoolResourceStack resourceStack = EasyMock.createMock(WoolResourceStack.class);

        List<ResourceStack> resourceStackList = Arrays.asList(resourceStack);
        bankResourceDistributor.produceResources(pieceData.getColor(), resourceStackList);
        EasyMock.expectLastCall();

        EasyMock.replay(bankResourceDistributor);

        Settlement settlement = new Settlement(pieceData, bankResourceDistributor, ownerVictoryPointsTracker);

        settlement.produceResources(resourceStackList);

        EasyMock.verify(bankResourceDistributor);
    }
}

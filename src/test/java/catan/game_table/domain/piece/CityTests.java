package catan.game_table.domain.piece;

import catan.game_table.domain.Color;
import catan.game_table.domain.PieceType;
import catan.game_table.domain.ScoreTracker;
import catan.game_table.domain.bank.BankResourceDistributor;
import catan.game_table.domain.resource.ResourceStack;
import catan.game_table.domain.resource.WoolResourceStack;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class CityTests {
    //produce(products : ResourceProducts)
    @Test
    public void ProduceResources_ValidProducts_VerifyCorrectFunctionCall(){
        PieceData pieceData = new PieceData(Color.RED, PieceType.CITY);
        BankResourceDistributor bankResourceDistributor = EasyMock.createMock(BankResourceDistributor.class);
        ScoreTracker ownerVictoryPointsTracker = EasyMock.createMock(ScoreTracker.class);
        WoolResourceStack resourceStack = EasyMock.createMock(WoolResourceStack.class);

        List<ResourceStack> resourceStackList = Arrays.asList(resourceStack);
        WoolResourceStack doubleStack = new WoolResourceStack(2);
        EasyMock.expect(resourceStack.add(resourceStack)).andStubReturn(doubleStack);
        bankResourceDistributor.produceResources(pieceData.getColor(), resourceStackList);
        EasyMock.expectLastCall();

        EasyMock.replay(bankResourceDistributor, resourceStack);

        City city = new City(pieceData, bankResourceDistributor, ownerVictoryPointsTracker);

        city.produceResources(resourceStackList);

        EasyMock.verify(bankResourceDistributor, resourceStack);
    }
}

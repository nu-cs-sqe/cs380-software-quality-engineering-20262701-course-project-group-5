package catan.game_table.domain.piece;

import catan.game_table.domain.bank.BankResourceDistributor;
import catan.game_table.domain.resource.ResourceStack;

import java.util.List;

public class City extends Building {
    public City(PieceData pieceData, BankResourceDistributor bankResourceDistributor){
        super(pieceData,bankResourceDistributor);
    }
    @Override
    public void produceResources(List<ResourceStack> products) {
    }
}

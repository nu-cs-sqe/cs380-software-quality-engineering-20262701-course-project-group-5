package catan.game_table.ui.controllers;

import catan.game_table.domain.Color;
import catan.game_table.domain.match_model.GameSetup;
import catan.game_table.domain.models.LobbyModel;
import catan.game_table.domain.player.PlayerData;
import catan.game_table.ui.GameScreenFactory;
import catan.game_table.ui.ViewRouter;
import catan.game_table.ui.views.LobbyView;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

public class LobbyControllerTests {
    @Test
    public void OnStart_PlayerListSizeTwo_ShowErrorInView() {
        LobbyView lobbyView = EasyMock.createMock(LobbyView.class);
        LobbyModel lobbyModel = EasyMock.createMock(LobbyModel.class);
        ViewRouter viewRouter = EasyMock.createMock(ViewRouter.class);
        GameSetup gameSetup = EasyMock.createMock(GameSetup.class);
        GameScreenFactory gameScreenFactory = EasyMock.createMock(GameScreenFactory.class);

        PlayerData playerOne = new PlayerData(Color.BLUE, "Player1");
        PlayerData playerTwo = new PlayerData(Color.RED, "Player2");
        List<PlayerData> playerList = Arrays.asList(playerOne, playerTwo);
        IllegalArgumentException error = new IllegalArgumentException("Not enough players.");
        EasyMock.expect(gameSetup.createGame(playerList)).andThrow(error);

        lobbyView.showError(error.getMessage());
        EasyMock.expectLastCall();

        EasyMock.replay(lobbyView, gameSetup);

        LobbyController lobbyController = new LobbyController(lobbyView, lobbyModel, viewRouter, gameSetup, gameScreenFactory);

        lobbyController.onStart(playerList);

        EasyMock.verify(lobbyView, gameSetup);
    }
}

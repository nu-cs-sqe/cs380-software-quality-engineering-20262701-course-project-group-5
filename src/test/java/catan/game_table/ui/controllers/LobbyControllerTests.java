package catan.game_table.ui.controllers;

import catan.game_table.domain.Color;
import catan.game_table.domain.match_model.GameSession;
import catan.game_table.domain.match_model.GameSetup;
import catan.game_table.domain.models.LobbyModel;
import catan.game_table.domain.player.PlayerData;
import catan.game_table.ui.GameScreenFactory;
import catan.game_table.ui.ViewRouter;
import catan.game_table.ui.views.GameTableView;
import catan.game_table.ui.views.LobbyView;
import javafx.scene.Scene;
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

    @Test
    public void OnStart_PlayerListSizeFour_MakeSession_MakeScene_ShowSceneViaRouter() {
        LobbyView lobbyView = EasyMock.createMock(LobbyView.class);
        LobbyModel lobbyModel = EasyMock.createMock(LobbyModel.class);
        ViewRouter viewRouter = EasyMock.createMock(ViewRouter.class);
        GameSetup gameSetup = EasyMock.createMock(GameSetup.class);
        GameScreenFactory gameScreenFactory = EasyMock.createMock(GameScreenFactory.class);

        PlayerData playerOne = new PlayerData(Color.BLUE, "Player1");
        PlayerData playerTwo = new PlayerData(Color.RED, "Player2");
        PlayerData playerThree = new PlayerData(Color.WHITE, "Player3");
        PlayerData playerFour = new PlayerData(Color.ORANGE, "Player4");
        List<PlayerData> playerList = Arrays.asList(playerOne, playerTwo, playerThree, playerFour);

        GameSession session = EasyMock.createMock(GameSession.class);
        EasyMock.expect(gameSetup.createGame(playerList)).andStubReturn(session);

        Scene gameScene = EasyMock.createMock(Scene.class);
        EasyMock.expect(gameScreenFactory.create(session)).andStubReturn(gameScene);

        viewRouter.show(gameScene);
        EasyMock.expectLastCall();

        EasyMock.replay(gameSetup, gameScreenFactory, viewRouter);

        LobbyController lobbyController = new LobbyController(lobbyView, lobbyModel, viewRouter, gameSetup, gameScreenFactory);

        lobbyController.onStart(playerList);

        EasyMock.verify(gameSetup, gameScreenFactory, viewRouter);
    }
}

package catan.game_table.domain.player;

import catan.game_table.domain.development.Knight;
import catan.game_table.domain.Playable;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class GamePlayerTests {

  @Test
  public void play_item_knight() {
    GamePlayer player = new GamePlayer();

    Playable knight = EasyMock.createMock(Knight.class);

    // this is how you expect a void method
    knight.onPlayEffect();
    EasyMock.expectLastCall();
    EasyMock.replay(knight);

    player.play(knight);

    EasyMock.verify(knight);


  }


}

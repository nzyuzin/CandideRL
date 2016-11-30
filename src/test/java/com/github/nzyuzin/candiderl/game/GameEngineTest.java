/*
 * This file is part of CandideRL.
 *
 * CandideRL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CandideRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CandideRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.nzyuzin.candiderl.game;

import com.github.nzyuzin.candiderl.game.map.Map;
import com.github.nzyuzin.candiderl.game.map.MapFactory;
import com.github.nzyuzin.candiderl.game.ui.GameUi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameEngineTest {

    @Mock
    private MapFactory mapFactory;

    @Mock
    private GameUi gameUi;

    private Map testMap = MapFactory.buildMapFrom(new char[][]{
            {'#', '#', '#', '#'},
            {'#', ' ', ' ', '#'},
            {'#', ' ', ' ', '#'},
            {'#', '#', '#', '#'}
    });

    @Test
    public void gameStartsAndExitsWithoutException() {
        when(mapFactory.getMap()).thenReturn(testMap);
        when(gameUi.getInputChar()).thenReturn('q');
        when(gameUi.getMapWidth()).thenReturn(testMap.getWidth());
        when(gameUi.getMapHeight()).thenReturn(testMap.getHeight());

        GameEngine engine = GameEngine.getGameEngine(mapFactory, gameUi);
        engine.startGame();

        verify(mapFactory, atLeastOnce()).getMap();
        verify(gameUi, times(1)).getInputChar();
        verify(gameUi, atLeastOnce()).drawUi(any());
        try {
            verify(gameUi).close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

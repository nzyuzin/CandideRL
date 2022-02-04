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

package com.github.nzyuzin.candiderl.game.characters.actions;

import com.github.nzyuzin.candiderl.game.characters.GameCharacter;
import com.github.nzyuzin.candiderl.game.map.Map;
import com.github.nzyuzin.candiderl.game.map.cells.MapCell;
import com.github.nzyuzin.candiderl.game.utility.Position;
import com.github.nzyuzin.candiderl.game.utility.PositionOnMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MoveToNextCellActionTest {

    @Mock
    private GameCharacter character;

    @Mock
    private Map map;

    @Mock
    private MapCell targetCell;

    @Test(expected = ActionAlreadyExecutedException.class)
    public void testExecute() throws Exception {
        when(character.getMap()).thenReturn(map);
        when(character.isDead()).thenReturn(false);

        Position characterPosition = Position.getInstance(0, 1);
        Position targetPosition = Position.getInstance(0, 0);
        PositionOnMap targetOnMap = new PositionOnMap(targetPosition, map);
        when(character.getPosition()).thenReturn(characterPosition);
        when(map.getCell(targetPosition)).thenReturn(targetCell);
        when(targetCell.getGameCharacter()).thenReturn(Optional.empty());
        when(targetCell.isPassable()).thenReturn(true);

        MoveToNextCellAction action = new MoveToNextCellAction(character, targetOnMap, 0, 100);

        assertTrue(!action.failureReason().isPresent());
        action.execute();

        verify(map).moveGameCharacter(character, targetOnMap);

        action.execute();
    }
}

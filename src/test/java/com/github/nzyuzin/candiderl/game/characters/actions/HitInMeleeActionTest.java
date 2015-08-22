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
import com.github.nzyuzin.candiderl.game.utility.Direction;
import com.github.nzyuzin.candiderl.game.utility.Position;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HitInMeleeActionTest {

    @Mock
    private GameCharacter performer;
    @Mock
    private GameCharacter target;

    @Before
    public void setUp() throws Exception {
        Map map = mock(Map.class);
        Position performerPosition = Position.getInstance(20, 20);
        Position targetPosition = performerPosition.apply(Direction.NORTH);
        when(performer.getPosition()).thenReturn(performerPosition);
        when(target.getPosition()).thenReturn(targetPosition);
        when(performer.isDead()).thenReturn(false);
        when(target.isDead()).thenReturn(false);
        when(target.getMap()).thenReturn(map);
        when(performer.getMap()).thenReturn(map);
    }

    @Test
    public void testExecute() throws Exception {
        HitInMeleeAction action = new HitInMeleeAction(performer, target);

        assertThat(action.canBeExecuted(), is(true));

        action.execute();

        verify(performer).rollDamageDice();
        verify(target).takeDamage(anyInt());
    }
}

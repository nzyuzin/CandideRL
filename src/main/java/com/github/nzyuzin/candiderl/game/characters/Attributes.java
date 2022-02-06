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

package com.github.nzyuzin.candiderl.game.characters;

import java.io.Serializable;

public class Attributes implements Serializable {

    protected int maxHp;
    protected int strength;
    protected int dexterity;
    protected int intelligence;
    protected int armor;

    public Attributes(int maxHp, int strength, int dexterity, int intelligence, int armor) {
        this.maxHp = maxHp;
        this.strength = strength;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
        this.armor = armor;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getStrength() {
        return strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getArmor() {
        return armor;
    }

}

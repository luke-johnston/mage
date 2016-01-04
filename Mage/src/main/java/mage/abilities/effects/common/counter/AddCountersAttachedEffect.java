/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.abilities.effects.common.counter;

import mage.abilities.Ability;
import mage.abilities.dynamicvalue.DynamicValue;
import mage.abilities.dynamicvalue.common.StaticValue;
import mage.abilities.effects.OneShotEffect;
import mage.constants.Outcome;
import mage.counters.Counter;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.util.CardUtil;

/**
 *
 * @author LevelX2
 */

public class AddCountersAttachedEffect extends OneShotEffect {

    private Counter counter;
    private DynamicValue amount;
    private String textEnchanted;

    public AddCountersAttachedEffect(Counter counter, String textEnchanted) {
        this(counter, new StaticValue(0), textEnchanted);
    }

    /**
     *
     * @param counter
     * @param amount this amount will be added to the counter instances
     * @param textEnchanted text used for the enchanted permanent in rule text
     */
    public AddCountersAttachedEffect(Counter counter, DynamicValue amount, String textEnchanted) {
        super(Outcome.Benefit);
        this.counter = counter.copy();
        this.amount = amount;
        this.textEnchanted = textEnchanted;
        setText();
    }

    public AddCountersAttachedEffect(final AddCountersAttachedEffect effect) {
        super(effect);
        if (effect.counter != null) {
            this.counter = effect.counter.copy();
        }
        this.amount = effect.amount;
        this.textEnchanted = effect.textEnchanted;
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Permanent permanent = game.getPermanent(source.getSourceId());
        if (permanent != null && permanent.getAttachedTo() != null) {
            Permanent attachedTo = game.getPermanent(permanent.getAttachedTo());
            if (attachedTo != null && counter != null) {
                Counter newCounter = counter.copy();
                newCounter.add(amount.calculate(game, source, this));
                attachedTo.addCounters(newCounter, game);
            }
            return true;
        }
        return false;
    }

    private void setText() {
        StringBuilder sb = new StringBuilder();
        // put a +1/+1 counter on it
        sb.append("put ");
        if (counter.getCount() > 1) {
            sb.append(CardUtil.numberToText(counter.getCount())).append(" ");
        } else {
            sb.append("a ");
        }
        sb.append(counter.getName().toLowerCase()).append(" counter on ");
        sb.append(textEnchanted);
        if (amount.getMessage().length() > 0) {
            sb.append(" for each ").append(amount.getMessage());
        }
        staticText = sb.toString();
    }

    @Override
    public AddCountersAttachedEffect copy() {
        return new AddCountersAttachedEffect(this);
    }

}
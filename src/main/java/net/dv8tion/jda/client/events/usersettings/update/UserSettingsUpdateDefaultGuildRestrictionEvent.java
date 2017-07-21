/*
 *     Copyright 2015-2017 Austin Keener & Michael Ritter & Florian Spieß
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.dv8tion.jda.client.events.usersettings.update;

import net.dv8tion.jda.core.JDA;

/**
 * <b><u>UserSettingsUpdateEmojiConversionEvent</u></b><br>
 * Fired if a {@link net.dv8tion.jda.client.entities.UserSettings UserSettings}'s emoji conversion changes.<br>
 * <br>
 * Use: Detect when a UserSettings emoji conversion changes and get it's previous value.
 */
public class UserSettingsUpdateDefaultGuildRestrictionEvent extends GenericUserSettingsUpdateEvent
{
    private final boolean oldValue;

    public UserSettingsUpdateDefaultGuildRestrictionEvent(final JDA api, final long responseNumber, final boolean oldValue)
    {
        super(api, responseNumber);
        this.oldValue = oldValue;
    }

    public boolean getOldValue()
    {
        return this.oldValue;
    }
}

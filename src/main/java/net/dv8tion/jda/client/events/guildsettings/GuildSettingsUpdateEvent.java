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

package net.dv8tion.jda.client.events.guildsettings;

import net.dv8tion.jda.client.entities.GuildSettings;
import net.dv8tion.jda.client.events.usersettings.GenericUserSettingsEvent;
import net.dv8tion.jda.core.JDA;

/**
 * <b><u>GuildSettingsUpdateEvent</u></b><br>
 * Fired whenever a {@link net.dv8tion.jda.client.entities.GuildSettings GuildSettings} is updated.<br>
 * <br>
 * Use: Detect any GuildSettingsUpdateEvent.
 */
public class GuildSettingsUpdateEvent extends GenericUserSettingsEvent
{
    private final GuildSettings oldValue;

    public GuildSettingsUpdateEvent(final JDA api, final long responseNumber, GuildSettings oldValue)
    {
        super(api, responseNumber);
        this.oldValue = oldValue;
    }

    public GuildSettings getOldValue()
    {
        return this.oldValue;
    }
}

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

package net.dv8tion.jda.client.events.guildsettings.update;

import net.dv8tion.jda.client.entities.GuildSettings;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;

/**
 * <b><u>UserSettingsUpdateAfkTimeoutEvent</u></b><br>
 * Fired if a {@link net.dv8tion.jda.client.entities.UserSettings UserSettings}'s afk timeout changes.<br>
 * <br>
 * Use: Detect when a UserSettings afk timeout changes and get it's previous value.
 */
public class GuildSettingsUpdateNotificationLevelEvent extends GenericGuildSettingsUpdateEvent
{
    private final GuildSettings.NotificationLevel oldValue;

    public GuildSettingsUpdateNotificationLevelEvent(final JDA api, final long responseNumber, final Guild guild, final GuildSettings.NotificationLevel oldValue)
    {
        super(api, responseNumber, guild);
        this.oldValue = oldValue;
    }

    public GuildSettings.NotificationLevel getOldValue()
    {
        return this.oldValue;
    }
}

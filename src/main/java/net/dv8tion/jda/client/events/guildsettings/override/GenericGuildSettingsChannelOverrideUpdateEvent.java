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

package net.dv8tion.jda.client.events.guildsettings.override;

import net.dv8tion.jda.client.entities.GuildSettings.ChannelOverride;
import net.dv8tion.jda.client.events.guildsettings.update.GenericGuildSettingsUpdateEvent;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

/**
 * <b><u>GuildSettingsUpdateEvent</u></b><br>
 * Fired whenever a {@link net.dv8tion.jda.client.entities.GuildSettings GuildSettings} is updated.<br>
 * <br>
 * Use: Detect any GuildSettingsUpdateEvent.
 */
public class GenericGuildSettingsChannelOverrideUpdateEvent extends GenericGuildSettingsUpdateEvent
{
    private final TextChannel channel;

    public GenericGuildSettingsChannelOverrideUpdateEvent(final JDA api, final long responseNumber, final Guild guild, final TextChannel channel)
    {
        super(api, responseNumber, guild);
        this.channel = channel;
    }

    public ChannelOverride getChannelOverride()
    {
        return this.getGuildSettings().getChannelOverride(this.channel);
    }

}

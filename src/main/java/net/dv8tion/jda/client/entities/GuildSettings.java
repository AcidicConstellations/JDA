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

package net.dv8tion.jda.client.entities;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

public interface GuildSettings
{
    ChannelOverride getChannelOverride(long id);

    ChannelOverride getChannelOverride(String id);

    ChannelOverride getChannelOverride(TextChannel channel);

    List<ChannelOverride> getChannelOverrides();

    Guild getGuild();

    NotificationLevel getNotificationLevel();

    boolean isMobilePush();

    boolean isMuted();

    boolean isSuppressEveryone();

    public interface ChannelOverride
    {
        TextChannel getChannel();

        NotificationLevel getNotificationLevel();

        boolean isMuted();
    }

    public enum NotificationLevel
    {
        ALL_MESSAGES(0),
        DEFAULT(3),
        MENTIONS_ONLY(1),
        NOTHING(2),

        UNKNOWN(-1);

        private static final NotificationLevel[] KEY_MAP;

        static
        {
            KEY_MAP = new NotificationLevel[NotificationLevel.values().length - 1];

            for (final NotificationLevel type : NotificationLevel.values())
                if (type.key >= 0)
                    NotificationLevel.KEY_MAP[type.key] = type;
        }

        public static NotificationLevel fromKey(final int key)
        {
            return key >= 0 && key < NotificationLevel.KEY_MAP.length ? NotificationLevel.KEY_MAP[key] : UNKNOWN;
        }

        private final int key;

        NotificationLevel(final int key)
        {
            this.key = key;
        }

        public int getKey()
        {
            return this.key;
        }
    }
}

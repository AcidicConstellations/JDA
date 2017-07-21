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

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.dv8tion.jda.core.entities.Guild;

public interface Profile
{
    List<Badge> getBadges();

    List<ConnectedAccount> getConnectedAccounts();

    Badge getDisplayedBadge();

    List<Guild> getMutualGuilds();

    OffsetDateTime getNitroSince();

    enum Badge
    {
        DISCORD_PARTNER(3, 1),
        DISCORD_STAFF(4, 0),
        HYPESQUAD(2, 2),
        NITRO(1, -1),

        UNKNOWN(0, -1);

        public static List<Badge> getBadges(final int flags, final boolean nitro)
        {
            final ArrayList<Badge> badges = new ArrayList<>(1);

            if (nitro)
                badges.add(NITRO);

            for (final Badge badge : Badge.values())
                if ((flags >> badge.offset & 1) == 1)
                    badges.add(badge);

            return badges;
        }

        public static int getFlags(final Collection<Badge> badges)
        {
            int flags = 0;
            for (final Badge badge : badges)
                if (badge.offset != -1)
                    flags |= badge.value;
            return flags;
        }

        private final int offset;
        private final int priority;
        private final int value;

        private Badge(final int priority, final int offset)
        {
            this.priority = priority;
            this.offset = offset;
            this.value = 1 << offset;
        }

        public int getPriority()
        {
            return this.priority;
        }
    }
}

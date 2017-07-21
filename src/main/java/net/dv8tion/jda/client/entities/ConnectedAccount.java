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

import java.util.HashMap;

public interface ConnectedAccount
{

    String getId();

    String getName();

    Type getType();

    int getVisibility();

    boolean isFriendSync();

    boolean isRevoked();

    boolean isVerified();

    enum Type
    {
        LEAGUE_OF_LEGENDS("leagueoflegends"),
        REDDIT("reddit"),
        SKYPE("skype"),
        STEAM("steam"),
        TWITCH("twitch"),
        TWITTER("twitter"),
        YOUTUBE("youtube"),
        UNKNOWN("");

        private static final HashMap<String, Type> KEY_MAP;

        static
        {
            KEY_MAP = new HashMap<>(Type.values().length - 1);
            for (final Type type : Type.values())
                if (!type.name.isEmpty())
                    Type.KEY_MAP.put(type.name, type);
        }

        public static Type fromName(final String name)
        {
            final Type type = Type.KEY_MAP.get(name);

            return type == null ? UNKNOWN : type;
        }

        private final String name;

        private Type(final String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return this.name;
        }
    }
}

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

public enum RelationshipType
{
    NO_RELATIONSHIP(0, "NONE"),
    FRIEND(1, "Friend"),
    BLOCKED(2, "BlockedUser"),
    INCOMING_FRIEND_REQUEST(3, "IFR"),
    OUTGOING_FRIEND_REQUEST(4, "OFR"),
    UNKNOWN(-1, "");

    private static final RelationshipType[] KEY_MAP;

    static
    {
        KEY_MAP = new RelationshipType[RelationshipType.values().length - 1];

        for (RelationshipType type : RelationshipType.values())
        {
            if (type.key >= 0)
                RelationshipType.KEY_MAP[type.key] = type;
        }
    }

    private final int key;
    private final String name;

    RelationshipType(int key, String name)
    {
        this.key = key;
        this.name=name;
    }

    public int getKey()
    {
        return key;
    }

    public String getName()
    {
        return this.name;
    }

    public static RelationshipType fromKey(int key)
    {
        return key >= 0 && key < KEY_MAP.length ? KEY_MAP[key] : UNKNOWN;
    }
}

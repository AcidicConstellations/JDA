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

package net.dv8tion.jda.client.entities.impl;

import net.dv8tion.jda.client.entities.ConnectedAccount;

public class ConnectedAccountImpl implements ConnectedAccount
{
    public final boolean friendSync;
    public final String id;
    public final String name;
    public final boolean revoked;
    public final Type type;
    public final boolean verified;
    public final int visibility;

    public ConnectedAccountImpl(final boolean friendSync, final int visibility, final boolean verified, final String name, final String id, final Type type, final boolean revoked)
    {
        this.friendSync = friendSync;
        this.visibility = visibility;
        this.verified = verified;
        this.name = name;
        this.id = id;
        this.type = type;
        this.revoked = revoked;
    }

    @Override
    public String getId()
    {
        return this.id;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public Type getType()
    {
        return this.type;
    }

    @Override
    public int getVisibility()
    {
        return this.visibility;
    }

    @Override
    public boolean isFriendSync()
    {
        return this.friendSync;
    }

    @Override
    public boolean isRevoked()
    {
        return this.revoked;
    }

    @Override
    public boolean isVerified()
    {
        return this.verified;
    }
}

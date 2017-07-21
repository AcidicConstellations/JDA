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

import net.dv8tion.jda.client.entities.Relationship;
import net.dv8tion.jda.client.entities.RelationshipType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.requests.Request;
import net.dv8tion.jda.core.requests.Response;
import net.dv8tion.jda.core.requests.RestAction;
import net.dv8tion.jda.core.requests.Route;

public abstract class AbstractRelationshipImpl implements Relationship
{
    protected final RelationshipType type;
    protected final User user;

    public AbstractRelationshipImpl(final User user, final RelationshipType type)
    {
        this.user = user;
        this.type = type;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (!(obj instanceof Relationship))
            return false;

        final Relationship relationship = (Relationship) obj;
        return this.type == relationship.getType() && this.user.equals(relationship.getUser());
    }

    @Override
    public JDA getJDA()
    {
        return this.user.getJDA();
    }

    @Override
    public RelationshipType getType()
    {
        return this.type;
    }

    @Override
    public User getUser()
    {
        return this.user;
    }

    @Override
    public int hashCode()
    {
        return (this.type.getName() + ' ' + this.user.getId()).hashCode();
    }

    @Override
    public String toString()
    {
        return this.type.getName() + '(' + this.user.toString() + ')';
    }

    protected RestAction<Void> addRelationship()
    {
        final Route.CompiledRoute route = Route.Relationships.ADD_RELATIONSHIP.compile(this.user.getId());
        return new RestAction<Void>(this.getJDA(), route)
        {
            @Override
            protected void handleResponse(final Response response, final Request<Void> request)
            {
                if (response.isOk())
                    request.onSuccess(null);
                else
                    request.onFailure(response);
            }
        };
    }

    protected RestAction<Void> deleteRelationship()
    {
        final Route.CompiledRoute route = Route.Relationships.DELETE_RELATIONSHIP.compile(this.user.getId());
        return new RestAction<Void>(this.getJDA(), route)
        {
            @Override
            protected void handleResponse(final Response response, final Request<Void> request)
            {
                if (response.isOk())
                    request.onSuccess(null);
                else
                    request.onFailure(response);
            }
        };
    }
}

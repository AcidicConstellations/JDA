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

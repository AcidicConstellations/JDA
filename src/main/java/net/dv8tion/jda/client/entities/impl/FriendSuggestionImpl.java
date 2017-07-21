package net.dv8tion.jda.client.entities.impl;

import java.util.List;
import net.dv8tion.jda.client.entities.FriendSuggestion;
import net.dv8tion.jda.core.entities.User;

public class FriendSuggestionImpl implements FriendSuggestion
{
    protected final List<Reason> reasons;
    protected final User user;

    public FriendSuggestionImpl(final List<Reason> reasons, final User user)
    {
        super();
        this.reasons = reasons;
        this.user = user;
    }

    @Override
    public List<Reason> getReasons()
    {
        return this.reasons;
    }

    @Override
    public User getUser()
    {
        return this.user;
    }

    public static class ReasonImpl implements Reason
    {
        protected final String name;
        protected final String platformType;
        protected final int type;

        public ReasonImpl(final String name, final String platformType, final int type)
        {
            super();
            this.name = name;
            this.platformType = platformType;
            this.type = type;
        }

        @Override
        public String getName()
        {
            return this.name;
        }

        @Override
        public String getPlatformType()
        {
            return this.platformType;
        }

        @Override
        public int getType()
        {
            return this.type;
        }
    }
}

package net.dv8tion.jda.client.entities;

import java.util.List;
import net.dv8tion.jda.core.entities.User;

public interface FriendSuggestion
{
    List<Reason> getReasons();

    User getUser();

    interface Reason
    {
        String getName();

        String getPlatformType();

        int getType(); // TODO: find out what this is used for
    }
}

package net.dv8tion.jda.client.entities.impl;

import java.util.List;

import net.dv8tion.jda.client.entities.GuildSettings;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

public class GuildSettingsImpl implements GuildSettings
{
    private final List<ChannelOverride> channelOverrides;
    private final Guild guild;
    private final NotificationLevel messageNotifications;
    private final boolean mobilePush;
    private final boolean muted;
    private final boolean suppressEveryone;

    public GuildSettingsImpl(final List<ChannelOverride> channelOverrides, final Guild guild, final NotificationLevel messageNotifications, final boolean mobilePush, final boolean muted, final boolean suppressEveryone)
    {
        this.channelOverrides = channelOverrides;
        this.guild = guild;
        this.messageNotifications = messageNotifications;
        this.mobilePush = mobilePush;
        this.muted = muted;
        this.suppressEveryone = suppressEveryone;
    }

    @Override
    public List<ChannelOverride> getChannelOverrides()
    {
        return this.channelOverrides;
    }

    @Override
    public Guild getGuild()
    {
        return this.guild;
    }

    @Override
    public NotificationLevel getMessageNotifications()
    {
        return this.messageNotifications;
    }

    @Override
    public boolean isMobilePush()
    {
        return this.mobilePush;
    }

    @Override
    public boolean isMuted()
    {
        return this.muted;
    }

    @Override
    public boolean isSuppressEveryone()
    {
        return this.suppressEveryone;
    }

    public static class ChannelOverrideImpl implements ChannelOverride
    {
        private final TextChannel channel;
        private final NotificationLevel messageNotifications;
        private final boolean muted;

        public ChannelOverrideImpl(final TextChannel channel, final NotificationLevel messageNotifications, final boolean muted)
        {
            this.channel = channel;
            this.messageNotifications = messageNotifications;
            this.muted = muted;
        }

        @Override
        public TextChannel getChannel()
        {
            return this.channel;
        }

        @Override
        public NotificationLevel getMessageNotifications()
        {
            return this.messageNotifications;
        }

        @Override
        public boolean isMuted()
        {
            return this.muted;
        }
    }
}
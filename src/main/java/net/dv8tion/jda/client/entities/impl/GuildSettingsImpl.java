package net.dv8tion.jda.client.entities.impl;

import gnu.trove.map.TLongObjectMap;
import net.dv8tion.jda.client.entities.GuildSettings;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.utils.MiscUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuildSettingsImpl implements GuildSettings
{
    private final TLongObjectMap<ChannelOverrideImpl> channelOverrides; // TODO: create on channel create/delete on channel delete
    private final Guild guild;
    private boolean mobilePush;
    private boolean muted;
    private NotificationLevel notificationLevel;
    private boolean suppressEveryone;

    public GuildSettingsImpl(final TLongObjectMap<ChannelOverrideImpl> channelOverrides, final Guild guild, final NotificationLevel notificationLevel, final boolean mobilePush, final boolean muted, final boolean suppressEveryone)
    {
        this.channelOverrides = channelOverrides;
        this.guild = guild;
        this.notificationLevel = notificationLevel;
        this.mobilePush = mobilePush;
        this.muted = muted;
        this.suppressEveryone = suppressEveryone;
    }

    @Override
    public ChannelOverrideImpl getChannelOverride(final long id)
    {
        return this.channelOverrides.get(id);
    }

    @Override
    public ChannelOverrideImpl getChannelOverride(final String id)
    {
        return this.getChannelOverride(MiscUtil.parseSnowflake(id));
    }

    @Override
    public ChannelOverrideImpl getChannelOverride(final TextChannel channel)
    {
        return this.getChannelOverride(channel.getIdLong());
    }

    @Override
    public List<ChannelOverride> getChannelOverrides()
    {
        return Collections.unmodifiableList(new ArrayList<>(this.channelOverrides.valueCollection()));
    }

    @Override
    public Guild getGuild()
    {
        return this.guild;
    }

    @Override
    public NotificationLevel getNotificationLevel()
    {
        return this.notificationLevel;
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

    public void setMobilePush(final boolean mobilePush)
    {
        this.mobilePush = mobilePush;
    }

    public void setMuted(final boolean muted)
    {
        this.muted = muted;
    }

    public void setNotificationLevel(final NotificationLevel notificationLevel) // TODO: DEFAULT only allowed for channels overrides
    {
        this.notificationLevel = notificationLevel;
    }

    public void setSuppressEveryone(final boolean suppressEveryone)
    {
        this.suppressEveryone = suppressEveryone;
    }

    public static class ChannelOverrideImpl implements ChannelOverride
    {
        private final TextChannel channel;
        private boolean muted;
        private NotificationLevel notificationLevel;

        public ChannelOverrideImpl(final TextChannel channel, final NotificationLevel notificationLevel, final boolean muted)
        {
            this.channel = channel;
            this.notificationLevel = notificationLevel;
            this.muted = muted;
        }

        @Override
        public TextChannel getChannel()
        {
            return this.channel;
        }

        @Override
        public NotificationLevel getNotificationLevel()
        {
            return this.notificationLevel;
        }

        @Override
        public boolean isMuted()
        {
            return this.muted;
        }

        public void setMuted(final boolean muted)
        {
            this.muted = muted;
        }

        public void setNotificationLevel(final NotificationLevel notificationLevel)
        {
            this.notificationLevel = notificationLevel;
        }
    }
}
package net.dv8tion.jda.client.handle;

import net.dv8tion.jda.client.entities.GuildSettings;
import net.dv8tion.jda.client.entities.impl.GuildSettingsImpl;
import net.dv8tion.jda.client.events.guildsettings.override.update.GuildSettingsChannelOverrideUpdateMuteEvent;
import net.dv8tion.jda.client.events.guildsettings.override.update.GuildSettingsChannelOverrideUpdateNotificationLevelEvent;
import net.dv8tion.jda.client.events.guildsettings.update.GuildSettingsUpdateMobilePushEvent;
import net.dv8tion.jda.client.events.guildsettings.update.GuildSettingsUpdateMuteEvent;
import net.dv8tion.jda.client.events.guildsettings.update.GuildSettingsUpdateNotificationLevelEvent;
import net.dv8tion.jda.client.events.guildsettings.update.GuildSettingsUpdateSuppressEveryoneEvent;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.impl.JDAImpl;
import net.dv8tion.jda.core.handle.SocketHandler;
import org.json.JSONArray;
import org.json.JSONObject;

public class GuildSettingsUpdateHandler extends SocketHandler
{
    public GuildSettingsUpdateHandler(final JDAImpl api)
    {
        super(api);
    }

    @Override
    protected Long handleInternally(final JSONObject content)
    {
        final Guild guild = this.api.getGuildById(content.getLong("guild_id"));

        final GuildSettingsImpl settings = (GuildSettingsImpl) this.api.asClient().getGuildSettings(guild);

        this.api.getEntityBuilder().createGuildSettings(content);

        if (content.has("channel_overrides"))
        {
            final JSONArray channelOverrides = content.getJSONArray("channel_overrides");
            for (int i = 0; i < channelOverrides.length(); i++)
            {

                final JSONObject channelOverrideObject = channelOverrides.getJSONObject(i);

                final TextChannel channel = guild.getTextChannelById(channelOverrideObject.getLong("channel_id"));

                final GuildSettingsImpl.ChannelOverrideImpl override = settings.getChannelOverride(channel);

                if (channelOverrideObject.has("message_notifications"))
                {
                    final GuildSettings.NotificationLevel oldNotificationLevel = override.getNotificationLevel();
                    final GuildSettings.NotificationLevel notificationLevel = GuildSettings.NotificationLevel.fromKey(channelOverrideObject.getInt("message_notifications"));
                    if (oldNotificationLevel != notificationLevel)
                    {
                        override.setNotificationLevel(notificationLevel);
                        this.api.getEventManager().handle(new GuildSettingsChannelOverrideUpdateNotificationLevelEvent(this.api, this.responseNumber, guild, channel, oldNotificationLevel));
                    }
                }

                if (channelOverrideObject.has("muted"))
                {
                    final boolean oldMuted = override.isMuted();
                    final boolean muted = channelOverrideObject.getBoolean("muted");
                    if (oldMuted != muted)
                    {
                        override.setMuted(muted);
                        this.api.getEventManager().handle(new GuildSettingsChannelOverrideUpdateMuteEvent(this.api, this.responseNumber, guild, channel, oldMuted));
                    }
                }
            }

        }

        if (content.has("message_notifications"))
        {
            final GuildSettings.NotificationLevel oldNotificationLevel = settings.getNotificationLevel();
            final GuildSettings.NotificationLevel notificationLevel = GuildSettings.NotificationLevel.fromKey(content.getInt("message_notifications"));
            if (oldNotificationLevel != notificationLevel)
            {
                settings.setNotificationLevel(notificationLevel);
                this.api.getEventManager().handle(new GuildSettingsUpdateNotificationLevelEvent(this.api, this.responseNumber, guild, oldNotificationLevel));
            }
        }

        if (content.has("muted"))
        {
            final boolean oldMuted = settings.isMuted();
            final boolean muted = content.getBoolean("muted");
            if (oldMuted != muted)
            {
                settings.setMuted(muted);
                this.api.getEventManager().handle(new GuildSettingsUpdateMuteEvent(this.api, this.responseNumber, guild, oldMuted));
            }
        }

        if (content.has("mobile_push"))
        {
            final boolean oldMobilePush = settings.isMobilePush();
            final boolean mobilePush = content.getBoolean("mobile_push");
            if (oldMobilePush != mobilePush)
            {
                settings.setMobilePush(mobilePush);
                this.api.getEventManager().handle(new GuildSettingsUpdateMobilePushEvent(this.api, this.responseNumber, guild, oldMobilePush));
            }
        }

        if (content.has("suppress_everyone"))
        {
            final boolean oldSuppressEveryone = settings.isSuppressEveryone();
            final boolean suppressEveryone = content.getBoolean("suppress_everyone");
            if (oldSuppressEveryone != suppressEveryone)
            {
                settings.setSuppressEveryone(suppressEveryone);
                this.api.getEventManager().handle(new GuildSettingsUpdateSuppressEveryoneEvent(this.api, this.responseNumber, guild, oldSuppressEveryone));
            }
        }

        return null;
    }
}

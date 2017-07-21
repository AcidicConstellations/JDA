package net.dv8tion.jda.client.handle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import net.dv8tion.jda.client.entities.UserSettings;
import net.dv8tion.jda.client.entities.UserSettings.ContentFilterLevel;
import net.dv8tion.jda.client.entities.impl.UserSettingsImpl;
import net.dv8tion.jda.client.events.usersettings.update.*;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.impl.JDAImpl;
import net.dv8tion.jda.core.handle.SocketHandler;
import org.json.JSONObject;

public class UserSettingsUpdateHandler extends SocketHandler
{
    public UserSettingsUpdateHandler(final JDAImpl api)
    {
        super(api);
    }

    @Override
    protected Long handleInternally(final JSONObject content)
    {
        final UserSettingsImpl settings = (UserSettingsImpl) this.api.asClient().getUserSettings();

        if (content.has("afk_timeout"))
        {
            final int afkTimeout = content.getInt("afk_timeout");
            if (settings.getAfkTimeout() != afkTimeout)
            {
                settings.setAfkTimeout(afkTimeout);
                this.api.getEventManager().handle(new UserSettingsUpdateAfkTimeoutEvent(this.api, this.responseNumber, afkTimeout));
            }
        }

        if (content.has("convert_emoticons"))
        {
            final boolean convertEmoticons = content.getBoolean("convert_emoticons");
            if (settings.getConvertEmoticons() != convertEmoticons)
            {
                settings.setConvertEmoticons(convertEmoticons);
                this.api.getEventManager().handle(new UserSettingsUpdateEmojiConversionEvent(this.api, this.responseNumber, convertEmoticons));
            }
        }

        if (content.has("default_guilds_restricted"))
        {
            final boolean defaultGuildsRestricted = content.getBoolean("default_guilds_restricted");
            if (settings.getDefaultGuildsRestricted() != defaultGuildsRestricted)
            {
                settings.setDefaultGuildsRestricted(defaultGuildsRestricted);
                this.api.getEventManager().handle(new UserSettingsUpdateDefaultGuildRestrictionEvent(this.api, this.responseNumber, defaultGuildsRestricted));
            }
        }

        if (content.has("detect_platform_accounts"))
        {
            final boolean detectPlatformAccounts = content.getBoolean("detect_platform_accounts");
            if (settings.getDetectPlatformAccounts() != detectPlatformAccounts)
            {
                settings.setDetectPlatformAccounts(detectPlatformAccounts);
                this.api.getEventManager().handle(new UserSettingsUpdateDetectPlatformAccountsEvent(this.api, this.responseNumber, detectPlatformAccounts));
            }
        }

        if (content.has("developer_mode"))
        {
            final boolean developerMode = content.getBoolean("developer_mode");
            if (settings.getDeveloperMode() != developerMode)
            {
                settings.setDeveloperMode(developerMode);
                this.api.getEventManager().handle(new UserSettingsUpdateDeveloperModeEvent(this.api, this.responseNumber, developerMode));
            }
        }

        if (content.has("enable_tts_command"))
        {
            final boolean enableTtsCommand = content.getBoolean("enable_tts_command");
            if (settings.getEnableTtsCommand() != enableTtsCommand)
            {
                settings.setEnableTtsCommand(enableTtsCommand);
                this.api.getEventManager().handle(new UserSettingsUpdateEnableTtsCommandEvent(this.api, this.responseNumber, enableTtsCommand));
            }
        }

        if (content.has("explicit_content_filter"))
        {
            final ContentFilterLevel explicitContentFilter = UserSettings.ContentFilterLevel.fromKey(content.getInt("explicit_content_filter"));
            if (settings.getExplicitContentFilter() != explicitContentFilter)
            {
                settings.setExplicitContentFilter(explicitContentFilter);
                this.api.getEventManager().handle(new UserSettingsUpdateExplicitContentFilterEvent(this.api, this.responseNumber, explicitContentFilter));
            }
        }

        if (content.has("friend_source_flags"))
        {
            final JSONObject friendSourceFlagsObject = content.getJSONObject("friend_source_flags");
            final Map<String, Boolean> friendSourceFlags = new HashMap<>(friendSourceFlagsObject.length());
            for (Entry<String, Object> flag : friendSourceFlagsObject.toMap().entrySet())
                friendSourceFlags.put(flag.getKey(), (boolean) flag.getValue());
            if (settings.getFriendSourceFlags() != friendSourceFlags)
            {
                settings.setFriendSourceFlags(friendSourceFlags);
                this.api.getEventManager().handle(new UserSettingsUpdateFriendSourceFlagsEvent(this.api, this.responseNumber, friendSourceFlags));
            }
        }

        if (content.has("guild_positions"))
        {
            final List<Guild> guildPositions = StreamSupport.stream(content.getJSONArray("guild_positions").spliterator(), false).map(o -> api.getGuildById((String) o)).collect(Collectors.toList());
            if (settings.getGuildPositions() != guildPositions)
            {
                settings.setGuildPositions(guildPositions);
                this.api.getEventManager().handle(new UserSettingsUpdateGuildPositionsEvent(this.api, this.responseNumber, guildPositions));
            }
        }

        if (content.has("inline_attachment_media"))
        {
            final boolean inlineAttachmentMedia = content.getBoolean("inline_attachment_media");
            if (settings.getInlineAttachmentMedia() != inlineAttachmentMedia)
            {
                settings.setEnableTtsCommand(inlineAttachmentMedia);
                this.api.getEventManager().handle(new UserSettingsUpdateInlineAttachmentMediaEvent(this.api, this.responseNumber, inlineAttachmentMedia));
            }
        }

        if (content.has("inline_embed_media"))
        {
            final boolean inlineEmbedMedia = content.getBoolean("inline_embed_media");
            if (settings.getInlineEmbedMedia() != inlineEmbedMedia)
            {
                settings.setEnableTtsCommand(inlineEmbedMedia);
                this.api.getEventManager().handle(new UserSettingsUpdateInlineEmbedMediaEvent(this.api, this.responseNumber, inlineEmbedMedia));
            }
        }

        if (content.has("locale"))
        {
            final UserSettings.Locale locale = UserSettings.Locale.fromKey(content.getString("locale"));
            if (settings.getLocale() != locale)
            {
                settings.setLocale(locale);
                this.api.getEventManager().handle(new UserSettingsUpdateLocaleEvent(this.api, this.responseNumber, locale));
            }
        }

        if (content.has("message_display_compact"))
        {
            final boolean messageDisplayCompact = content.getBoolean("message_display_compact");
            if (settings.getMessageDisplayCompact() != messageDisplayCompact)
            {
                settings.setMessageDisplayCompact(messageDisplayCompact);
                this.api.getEventManager().handle(new UserSettingsUpdateMessageDisplayCompactEvent(this.api, this.responseNumber, messageDisplayCompact));
            }
        }

        if (content.has("render_embeds"))
        {
            final boolean renderEmbeds = content.getBoolean("render_embeds");
            if (settings.getRenderEmbeds() != renderEmbeds)
            {
                settings.setRenderEmbeds(renderEmbeds);
                this.api.getEventManager().handle(new UserSettingsUpdateRenderEmbedsEvent(this.api, this.responseNumber, renderEmbeds));
            }
        }

        if (content.has("render_reactions"))
        {
            final boolean renderReactions = content.getBoolean("render_reactions");
            if (settings.getRenderReactions() != renderReactions)
            {
                settings.setRenderReactions(renderReactions);
                this.api.getEventManager().handle(new UserSettingsUpdateRenderReactionsEvent(this.api, this.responseNumber, renderReactions));
            }
        }

        if (content.has("restricted_guilds"))
        {
            final List<Guild> restrictedGuilds = StreamSupport.stream(content.getJSONArray("restricted_guilds").spliterator(), false).map(o -> api.getGuildById((String) o)).collect(Collectors.toList());
            if (settings.getRestrictedGuilds() != restrictedGuilds)
            {
                settings.setRestrictedGuilds(restrictedGuilds);
                this.api.getEventManager().handle(new UserSettingsUpdateRestrictedGuildsEvent(this.api, this.responseNumber, restrictedGuilds));
            }
        }

        if (content.has("show_current_game"))
        {
            final boolean showCurrentGame = content.getBoolean("show_current_game");
            if (settings.getShowCurrentGame() != showCurrentGame)
            {
                settings.setShowCurrentGame(showCurrentGame);
                this.api.getEventManager().handle(new UserSettingsUpdateShowCurrentGameEvent(this.api, this.responseNumber, showCurrentGame));
            }
        }

        if (content.has("status"))
        {
            final OnlineStatus status = OnlineStatus.fromKey(content.getString("status"));
            if (settings.getStatus() != status)
            {
                settings.setStatus(status);
                this.api.getEventManager().handle(new UserSettingsUpdateStatusEvent(this.api, this.responseNumber, status));
            }
        }

        if (content.has("theme"))
        {
            final UserSettings.Theme theme = UserSettings.Theme.fromKey(content.getString("theme"));
            if (settings.getTheme() != theme)
            {
                settings.setTheme(theme);
                this.api.getEventManager().handle(new UserSettingsUpdateThemeEvent(this.api, this.responseNumber, theme));
            }
        }

        if (content.has("timezone_offset"))
        {
            final int timezoneOffset = content.getInt("timezone_offset");
            if (settings.getTimezoneOffset() != timezoneOffset)
            {
                settings.setTimezoneOffset(timezoneOffset);
                this.api.getEventManager().handle(new UserSettingsUpdateTimezoneOffsetEvent(this.api, this.responseNumber, timezoneOffset));
            }
        }

        return null;
    }
}

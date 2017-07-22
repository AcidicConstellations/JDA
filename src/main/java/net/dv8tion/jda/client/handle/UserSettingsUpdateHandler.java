package net.dv8tion.jda.client.handle;

import net.dv8tion.jda.client.entities.UserSettings;
import net.dv8tion.jda.client.entities.UserSettings.ContentFilterLevel;
import net.dv8tion.jda.client.entities.impl.UserSettingsImpl;
import net.dv8tion.jda.client.events.usersettings.update.*;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.impl.JDAImpl;
import net.dv8tion.jda.core.handle.SocketHandler;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
            final int oldAfkTimeout = settings.getAfkTimeout();
            final int afkTimeout = content.getInt("afk_timeout");
            if (oldAfkTimeout != afkTimeout)
            {
                settings.setAfkTimeout(afkTimeout);
                this.api.getEventManager().handle(new UserSettingsUpdateAfkTimeoutEvent(this.api, this.responseNumber, oldAfkTimeout));
            }
        }

        if (content.has("convert_emoticons"))
        {
            final boolean oldConvertEmoticons = settings.getConvertEmoticons();
            final boolean convertEmoticons = content.getBoolean("convert_emoticons");
            if (oldConvertEmoticons != convertEmoticons)
            {
                settings.setConvertEmoticons(convertEmoticons);
                this.api.getEventManager().handle(new UserSettingsUpdateEmojiConversionEvent(this.api, this.responseNumber, oldConvertEmoticons));
            }
        }

        if (content.has("default_guilds_restricted"))
        {
            final boolean oldDefaultGuildsRestricted = settings.getDefaultGuildsRestricted();
            final boolean defaultGuildsRestricted = content.getBoolean("default_guilds_restricted");
            if (oldDefaultGuildsRestricted != defaultGuildsRestricted)
            {
                settings.setDefaultGuildsRestricted(defaultGuildsRestricted);
                this.api.getEventManager().handle(new UserSettingsUpdateDefaultGuildRestrictionEvent(this.api, this.responseNumber, oldDefaultGuildsRestricted));
            }
        }

        if (content.has("detect_platform_accounts"))
        {
            final boolean oldDetectPlatformAccounts = settings.getDetectPlatformAccounts();
            final boolean detectPlatformAccounts = content.getBoolean("detect_platform_accounts");
            if (oldDetectPlatformAccounts != detectPlatformAccounts)
            {
                settings.setDetectPlatformAccounts(detectPlatformAccounts);
                this.api.getEventManager().handle(new UserSettingsUpdateDetectPlatformAccountsEvent(this.api, this.responseNumber, oldDetectPlatformAccounts));
            }
        }

        if (content.has("developer_mode"))
        {
            final boolean oldDeveloperMode = settings.getDeveloperMode();
            final boolean developerMode = content.getBoolean("developer_mode");
            if (oldDeveloperMode != developerMode)
            {
                settings.setDeveloperMode(developerMode);
                this.api.getEventManager().handle(new UserSettingsUpdateDeveloperModeEvent(this.api, this.responseNumber, oldDeveloperMode));
            }
        }

        if (content.has("enable_tts_command"))
        {
            final boolean oldEnableTtsCommand = settings.getEnableTtsCommand();
            final boolean enableTtsCommand = content.getBoolean("enable_tts_command");
            if (oldEnableTtsCommand != enableTtsCommand)
            {
                settings.setEnableTtsCommand(enableTtsCommand);
                this.api.getEventManager().handle(new UserSettingsUpdateEnableTtsCommandEvent(this.api, this.responseNumber, oldEnableTtsCommand));
            }
        }

        if (content.has("explicit_content_filter"))
        {
            final ContentFilterLevel oldExplicitContentFilter = settings.getExplicitContentFilter();
            final ContentFilterLevel explicitContentFilter = UserSettings.ContentFilterLevel.fromKey(content.getInt("explicit_content_filter"));
            if (oldExplicitContentFilter != explicitContentFilter)
            {
                settings.setExplicitContentFilter(explicitContentFilter);
                this.api.getEventManager().handle(new UserSettingsUpdateExplicitContentFilterEvent(this.api, this.responseNumber, oldExplicitContentFilter));
            }
        }

        if (content.has("friend_source_flags"))
        {
            final Map<String, Boolean> oldFriendSourceFlagsObject = settings.getFriendSourceFlags();
            final JSONObject friendSourceFlagsObject = content.getJSONObject("friend_source_flags");
            final Map<String, Boolean> friendSourceFlags = new HashMap<>(friendSourceFlagsObject.length());
            for (final Entry<String, Object> flag : friendSourceFlagsObject.toMap().entrySet())
                friendSourceFlags.put(flag.getKey(), (boolean) flag.getValue());
            if (!oldFriendSourceFlagsObject.equals(friendSourceFlags))
            {
                settings.setFriendSourceFlags(friendSourceFlags);
                this.api.getEventManager().handle(new UserSettingsUpdateFriendSourceFlagsEvent(this.api, this.responseNumber, oldFriendSourceFlagsObject));
            }
        }

        if (content.has("guild_positions"))
        {
            final List<Guild> oldGuildPositions = settings.getGuildPositions();
            final List<Guild> guildPositions = StreamSupport.stream(content.getJSONArray("guild_positions").spliterator(), false).map(o -> this.api.getGuildById((String) o)).collect(Collectors.toList());
            if (!oldGuildPositions.equals(guildPositions))
            {
                settings.setGuildPositions(guildPositions);
                this.api.getEventManager().handle(new UserSettingsUpdateGuildPositionsEvent(this.api, this.responseNumber, oldGuildPositions));
            }
        }

        if (content.has("inline_attachment_media"))
        {
            final boolean oldInlineAttachmentMedia = settings.getInlineAttachmentMedia();
            final boolean inlineAttachmentMedia = content.getBoolean("inline_attachment_media");
            if (oldInlineAttachmentMedia != inlineAttachmentMedia)
            {
                settings.setEnableTtsCommand(inlineAttachmentMedia);
                this.api.getEventManager().handle(new UserSettingsUpdateInlineAttachmentMediaEvent(this.api, this.responseNumber, oldInlineAttachmentMedia));
            }
        }

        if (content.has("inline_embed_media"))
        {
            final boolean oldInlineEmbedMedia = settings.getInlineEmbedMedia();
            final boolean inlineEmbedMedia = content.getBoolean("inline_embed_media");
            if (oldInlineEmbedMedia != inlineEmbedMedia)
            {
                settings.setEnableTtsCommand(inlineEmbedMedia);
                this.api.getEventManager().handle(new UserSettingsUpdateInlineEmbedMediaEvent(this.api, this.responseNumber, oldInlineEmbedMedia));
            }
        }

        if (content.has("locale"))
        {
            final UserSettings.Locale oldLocale = settings.getLocale();
            final UserSettings.Locale locale = UserSettings.Locale.fromKey(content.getString("locale"));
            if (oldLocale != locale)
            {
                settings.setLocale(locale);
                this.api.getEventManager().handle(new UserSettingsUpdateLocaleEvent(this.api, this.responseNumber, oldLocale));
            }
        }

        if (content.has("message_display_compact"))
        {
            final boolean oldMessageDisplayCompact = settings.getMessageDisplayCompact();
            final boolean messageDisplayCompact = content.getBoolean("message_display_compact");
            if (oldMessageDisplayCompact != messageDisplayCompact)
            {
                settings.setMessageDisplayCompact(messageDisplayCompact);
                this.api.getEventManager().handle(new UserSettingsUpdateMessageDisplayCompactEvent(this.api, this.responseNumber, oldMessageDisplayCompact));
            }
        }

        if (content.has("render_embeds"))
        {
            final boolean oldRenderEmbeds = settings.getRenderEmbeds();
            final boolean renderEmbeds = content.getBoolean("render_embeds");
            if (oldRenderEmbeds != renderEmbeds)
            {
                settings.setRenderEmbeds(renderEmbeds);
                this.api.getEventManager().handle(new UserSettingsUpdateRenderEmbedsEvent(this.api, this.responseNumber, oldRenderEmbeds));
            }
        }

        if (content.has("render_reactions"))
        {
            final boolean oldRenderReactions = settings.getRenderReactions();
            final boolean renderReactions = content.getBoolean("render_reactions");
            if (oldRenderReactions != renderReactions)
            {
                settings.setRenderReactions(renderReactions);
                this.api.getEventManager().handle(new UserSettingsUpdateRenderReactionsEvent(this.api, this.responseNumber, oldRenderReactions));
            }
        }

        if (content.has("restricted_guilds"))
        {
            final List<Guild> oldRestrictedGuilds = settings.getRestrictedGuilds();
            final List<Guild> restrictedGuilds = StreamSupport.stream(content.getJSONArray("restricted_guilds").spliterator(), false).map(o -> this.api.getGuildById((String) o)).collect(Collectors.toList());
            if (!oldRestrictedGuilds.equals(restrictedGuilds))
            {
                settings.setRestrictedGuilds(restrictedGuilds);
                this.api.getEventManager().handle(new UserSettingsUpdateRestrictedGuildsEvent(this.api, this.responseNumber, oldRestrictedGuilds));
            }
        }

        if (content.has("show_current_game"))
        {
            final boolean oldShowCurrentGame = settings.getRenderReactions();
            final boolean showCurrentGame = content.getBoolean("show_current_game");
            if (oldShowCurrentGame != showCurrentGame)
            {
                settings.setShowCurrentGame(showCurrentGame);
                this.api.getEventManager().handle(new UserSettingsUpdateShowCurrentGameEvent(this.api, this.responseNumber, oldShowCurrentGame));
            }
        }

        if (content.has("status"))
        {
            final OnlineStatus oldStatus = settings.getStatus();
            final OnlineStatus status = OnlineStatus.fromKey(content.getString("status"));
            if (oldStatus != status)
            {
                settings.setStatus(status);
                this.api.getEventManager().handle(new UserSettingsUpdateStatusEvent(this.api, this.responseNumber, oldStatus));
            }
        }

        if (content.has("theme"))
        {
            final UserSettings.Theme oldTheme = settings.getTheme();
            final UserSettings.Theme theme = UserSettings.Theme.fromKey(content.getString("theme"));
            if (oldTheme != theme)
            {
                settings.setTheme(theme);
                this.api.getEventManager().handle(new UserSettingsUpdateThemeEvent(this.api, this.responseNumber, oldTheme));
            }
        }

        if (content.has("timezone_offset"))
        {
            final int oldTimezoneOffset = settings.getTimezoneOffset();
            final int timezoneOffset = content.getInt("timezone_offset");
            if (oldTimezoneOffset != timezoneOffset)
            {
                settings.setTimezoneOffset(timezoneOffset);
                this.api.getEventManager().handle(new UserSettingsUpdateTimezoneOffsetEvent(this.api, this.responseNumber, oldTimezoneOffset));
            }
        }

        return null;
    }
}

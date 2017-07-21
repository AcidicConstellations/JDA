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

import java.util.List;
import java.util.Map;
import net.dv8tion.jda.client.entities.UserSettings;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;

public class UserSettingsImpl implements UserSettings
{
    protected final JDA api;

    protected int afkTimeout;
    protected boolean convertEmoticons;
    protected boolean defaultGuildsRestricted;
    protected boolean detectPlatformAccounts;
    protected boolean developerMode;
    protected boolean enableTtsCommand;
    protected ContentFilterLevel explicitContentFilter;
    protected Map<String, Boolean> friendSourceFlags;
    protected List<Guild> guildPositions = null;
    protected boolean inlineAttachmentMedia;
    protected boolean inlineEmbedMedia;
    protected Locale locale;
    protected boolean messageDisplayCompact;
    protected boolean renderEmbeds;
    protected boolean renderReactions;
    protected List<Guild> restrictedGuilds = null;
    protected boolean showCurrentGame;
    protected OnlineStatus status;
    protected Theme theme;
    protected int timezoneOffset;

    public UserSettingsImpl(final JDA api)
    {
        this.api = api;
    }

    @Override
    public boolean equals(final Object obj)
    {
        return obj instanceof UserSettings && this.api.getSelfUser().equals(((UserSettingsImpl) obj).getJDA().getSelfUser());
    }

    @Override
    public int getAfkTimeout()
    {
        return this.afkTimeout;
    }

    @Override
    public boolean getConvertEmoticons()
    {
        return this.convertEmoticons;
    }

    @Override
    public boolean getDefaultGuildsRestricted()
    {
        return this.defaultGuildsRestricted;
    }

    @Override
    public boolean getDetectPlatformAccounts()
    {
        return this.detectPlatformAccounts;
    }

    @Override
    public boolean getDeveloperMode()
    {
        return this.developerMode;
    }

    @Override
    public boolean getEnableTtsCommand()
    {
        return this.enableTtsCommand;
    }

    @Override
    public ContentFilterLevel getExplicitContentFilter()
    {
        return this.explicitContentFilter;
    }

    @Override
    public Map<String, Boolean> getFriendSourceFlags()
    {
        return this.friendSourceFlags;
    }

    @Override
    public List<Guild> getGuildPositions()
    {
        return this.guildPositions;
    }

    @Override
    public boolean getInlineAttachmentMedia()
    {
        return this.inlineAttachmentMedia;
    }

    @Override
    public boolean getInlineEmbedMedia()
    {
        return this.inlineEmbedMedia;
    }

    @Override
    public JDA getJDA()
    {
        return this.api;
    }

    @Override
    public Locale getLocale()
    {
        return this.locale;
    }

    @Override
    public boolean getMessageDisplayCompact()
    {
        return this.messageDisplayCompact;
    }

    @Override
    public boolean getRenderEmbeds()
    {
        return this.renderEmbeds;
    }

    @Override
    public boolean getRenderReactions()
    {
        return this.renderReactions;
    }

    @Override
    public List<Guild> getRestrictedGuilds()
    {
        return this.restrictedGuilds;
    }

    @Override
    public boolean getShowCurrentGame()
    {
        return this.showCurrentGame;
    }

    @Override
    public OnlineStatus getStatus()
    {
        return this.status;
    }

    @Override
    public Theme getTheme()
    {
        return this.theme;
    }

    @Override
    public int getTimezoneOffset()
    {
        return this.timezoneOffset;
    }

    @Override
    public int hashCode()
    {
        return Long.hashCode(this.getJDA().getSelfUser().getIdLong());
    }

    public void setAfkTimeout(final int afkTimeout)
    {
        this.afkTimeout = afkTimeout;
    }

    public void setConvertEmoticons(final boolean convertEmoticons)
    {
        this.convertEmoticons = convertEmoticons;
    }

    public void setDefaultGuildsRestricted(final boolean defaultGuildsRestricted)
    {
        this.defaultGuildsRestricted = defaultGuildsRestricted;
    }

    public void setDetectPlatformAccounts(final boolean detectPlatformAccounts)
    {
        this.detectPlatformAccounts = detectPlatformAccounts;
    }

    public void setDeveloperMode(final boolean developerMode)
    {
        this.developerMode = developerMode;
    }

    public void setEnableTtsCommand(final boolean enableTtsCommand)
    {
        this.enableTtsCommand = enableTtsCommand;
    }

    public void setExplicitContentFilter(final ContentFilterLevel explicitContentFilter)
    {
        this.explicitContentFilter = explicitContentFilter;
    }

    public void setFriendSourceFlags(final Map<String, Boolean> friendSourceFlags)
    {
        this.friendSourceFlags = friendSourceFlags;
    }

    public void setGuildPositions(final List<Guild> guildPositions)
    {
        this.guildPositions = guildPositions;
    }

    public void setInlineAttachmentMedia(final boolean inlineAttachmentMedia)
    {
        this.inlineAttachmentMedia = inlineAttachmentMedia;
    }

    public void setInlineEmbedMedia(final boolean inlineEmbedMedia)
    {
        this.inlineEmbedMedia = inlineEmbedMedia;
    }

    public void setLocale(final Locale locale)
    {
        this.locale = locale;
    }

    public void setMessageDisplayCompact(final boolean messageDisplayCompact)
    {
        this.messageDisplayCompact = messageDisplayCompact;
    }

    public void setRenderEmbeds(final boolean renderEmbeds)
    {
        this.renderEmbeds = renderEmbeds;
    }

    public void setRenderReactions(final boolean renderReactions)
    {
        this.renderReactions = renderReactions;
    }

    public void setRestrictedGuilds(final List<Guild> restrictedGuilds)
    {
        this.restrictedGuilds = restrictedGuilds;
    }

    public void setShowCurrentGame(final boolean showCurrentGame)
    {
        this.showCurrentGame = showCurrentGame;
    }

    public void setStatus(final OnlineStatus status)
    {
        this.status = status;
    }

    public void setTheme(final Theme theme)
    {
        this.theme = theme;
    }

    public void setTimezoneOffset(final int timezoneOffset)
    {
        this.timezoneOffset = timezoneOffset;
    }

    @Override
    public String toString()
    {
        return "UserSettings(" + this.getJDA().getSelfUser() + ")";
    }
}

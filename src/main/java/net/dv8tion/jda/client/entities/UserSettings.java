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

package net.dv8tion.jda.client.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;

public interface UserSettings
{

    int getAfkTimeout();

    boolean getConvertEmoticons();

    boolean getDefaultGuildsRestricted();

    boolean getDetectPlatformAccounts();

    boolean getDeveloperMode();

    boolean getEnableTtsCommand();

    ContentFilterLevel getExplicitContentFilter();

    Map<String, Boolean> getFriendSourceFlags(); // TODO: what are these?

    List<Guild> getGuildPositions();

    boolean getInlineAttachmentMedia();

    boolean getInlineEmbedMedia();

    JDA getJDA();

    Locale getLocale();

    boolean getMessageDisplayCompact();

    boolean getRenderEmbeds();

    boolean getRenderReactions();

    List<Guild> getRestrictedGuilds();

    boolean getShowCurrentGame();

    OnlineStatus getStatus();

    Theme getTheme();

    int getTimezoneOffset();

    public enum ContentFilterLevel
    {
        KEEP_ME_SAFE(2),
        MY_FRIENDS_ARE_NICE(1),
        I_LIVE_ON_THE_EDGE(0),

        UNKNOWN(-1);

        private static final ContentFilterLevel[] KEY_MAP;

        static
        {
            KEY_MAP = new ContentFilterLevel[ContentFilterLevel.values().length - 1];

            for (final ContentFilterLevel level : ContentFilterLevel.values())
                if (level.key >= 0)
                    ContentFilterLevel.KEY_MAP[level.key] = level;
        }

        public static ContentFilterLevel fromKey(final int key)
        {
            return key >= 0 && key < ContentFilterLevel.KEY_MAP.length ? ContentFilterLevel.KEY_MAP[key] : UNKNOWN;
        }

        private final int key;

        private ContentFilterLevel(final int key)
        {
            this.key = key;
        }

        public int getKey()
        {
            return this.key;
        }
    }

    static enum Locale
    {
        BULGARIAN("bg"),
        CZECH("cs"),
        DANISH("da"),
        DUTCH("nl"),
        ENGLISH_UK("en-GB"),
        ENGLISH_US("en-US"),
        FINNISH("fi"),
        FRENCH("fr"),
        GERMAN("de"),
        ITALIAN("it"),
        JAPANESE("ja"),
        KOREAN("ko"),
        NORWEGIAN("no"),
        POLISH("pl"),
        PORTUGUESE_BRAZILIAN("pt-BR"),
        RUSSIAN("ru"),
        SPANISH("es-ES"),
        SWEDISH("sv-SE"),
        TRADITIONAL_CHINESE("zh-TW"),
        TURKISH("tr"),
        UKRAINIAN("uk"),

        UNKNOWN("");

        private static final HashMap<String, Locale> KEY_MAP;

        static
        {
            KEY_MAP = new HashMap<>(Locale.values().length - 1);
            for (final Locale locale : Locale.values())
                if (!locale.key.isEmpty())
                    Locale.KEY_MAP.put(locale.key, locale);
        }

        public static Locale fromKey(final String key)
        {
            final Locale locale = Locale.KEY_MAP.get(key);

            return locale == null ? UNKNOWN : locale;
        }

        private final String key;

        private Locale(final String key)
        {
            this.key = key;
        }

        public String getKey()
        {
            return this.key;
        }
    }

    static enum Theme
    {
        DARK("dark"),
        LIGHT("light"),

        UNKNOWN("");

        private static final HashMap<String, Theme> KEY_MAP;

        static
        {
            KEY_MAP = new HashMap<>(Theme.values().length - 1);
            for (final Theme theme : Theme.values())
                if (!theme.key.isEmpty())
                    Theme.KEY_MAP.put(theme.key, theme);
        }

        public static Theme fromKey(final String key)
        {
            final Theme theme = Theme.KEY_MAP.get(key);

            return theme == null ? UNKNOWN : theme;
        }

        private final String key;

        private Theme(final String key)
        {
            this.key = key;
        }

        public String getKey()
        {
            return this.key;
        }
    }
}

package net.dv8tion.jda.client.handle;

import net.dv8tion.jda.client.entities.GuildSettings;
import net.dv8tion.jda.client.events.guildsettings.GuildSettingsUpdateEvent;
import net.dv8tion.jda.core.entities.impl.JDAImpl;
import net.dv8tion.jda.core.handle.SocketHandler;
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
        GuildSettings oldSettings = api.asClient().getGuildSettings(content.getLong("guild_id"));

        api.getEntityBuilder().createGuildSettings(content);

        this.api.getEventManager().handle(new GuildSettingsUpdateEvent(this.api, this.responseNumber, oldSettings));

        return null;
    }
}

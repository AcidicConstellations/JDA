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

package net.dv8tion.jda.client.handle;

import net.dv8tion.jda.client.events.notes.update.NoteUpdateEvent;
import net.dv8tion.jda.core.entities.impl.JDAImpl;
import net.dv8tion.jda.core.entities.impl.UserImpl;
import net.dv8tion.jda.core.handle.SocketHandler;
import org.json.JSONObject;

import java.util.Objects;

public class NoteUpdateHandler extends SocketHandler
{
    public NoteUpdateHandler(final JDAImpl api)
    {
        super(api);
    }

    @Override
    protected Long handleInternally(final JSONObject content)
    {
        final UserImpl user = (UserImpl) this.api.getUserById(content.getLong("id"));

        final String oldNote = this.api.asClient().getNote(user);
        final String note = content.getString("note");

        if (!Objects.equals(oldNote, note))
        {
            user.setNote(note);
            this.api.getEventManager().handle(new NoteUpdateEvent(this.api, this.responseNumber, oldNote));
        }

        return null;
    }
}

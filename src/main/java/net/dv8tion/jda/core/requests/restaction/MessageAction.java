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

package net.dv8tion.jda.core.requests.restaction;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.impl.JDAImpl;
import net.dv8tion.jda.core.entities.impl.MessageEmbedImpl;
import net.dv8tion.jda.core.requests.*;
import net.dv8tion.jda.core.utils.Checks;
import net.dv8tion.jda.core.utils.MiscUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageAction extends RestAction<Message> implements Appendable
{
    protected final Map<String, InputStream> files = new HashMap<>();
    protected final StringBuilder content;
    protected MessageEmbed embed = null;
    protected String nonce = null;
    protected boolean tts = false, override = false;

    public MessageAction(JDA api, Route.CompiledRoute route)
    {
        super(api, route);
        content = new StringBuilder();
    }

    public MessageAction(JDA api, Route.CompiledRoute route, StringBuilder contentBuilder)
    {
        super(api, route);
        if (contentBuilder.length() > Message.MAX_CONTENT_LENGTH)
            throw new IllegalArgumentException("Cannot build a Message with more than 2000 characters. Please limit your input.");
        this.content = contentBuilder;
    }

    public MessageAction apply(final Message message)
    {
        if (message == null)
            return this;
        final List<MessageEmbed> embeds = message.getEmbeds();
        if (embeds != null && !embeds.isEmpty())
            this.embed = embeds.get(0);

        for (Message.Attachment a : message.getAttachments())
        {
            try
            {
                addFile(a.getInputStream(), a.getFileName());
            }
            catch (IOException ex)
            {
                JDAImpl.LOG.log(ex);
            }
        }
        return content(message.getContentRaw()).tts(message.isTTS());
    }

    public boolean isEmpty()
    {
        return (content.length() == 0)
            && (embed == null || embed.getLength() == 0);
    }

    public boolean isEdit()
    {
        return finalizeRoute().getMethod() == Method.PATCH;
    }

    public MessageAction tts(final boolean isTTS)
    {
        this.tts = isTTS;
        return this;
    }

    public MessageAction reset()
    {
        return content(null).nonce(null).embed(null).tts(false).override(false).clearFiles();
    }

    public MessageAction nonce(final String nonce)
    {
        this.nonce = nonce;
        return this;
    }

    public MessageAction content(final String content)
    {
        if (content == null || content.isEmpty())
            this.content.setLength(0);
        else if (content.length() <= Message.MAX_CONTENT_LENGTH)
            this.content.replace(0, this.content.length(), content);
        else
            throw new IllegalArgumentException("A message may not exceed 2000 characters. Please limit your input!");
        return this;
    }

    public MessageAction embed(final MessageEmbed embed)
    {
        if (embed != null)
        {
            if (!(embed instanceof MessageEmbedImpl))
                throw new IllegalArgumentException("Cannot use provided embed implementation!");
            final AccountType type = getJDA().getAccountType();
            Checks.check(embed.isSendable(type),
                "Provided Message contains an embed with a length greater than %d characters, which is the max for %s accounts!",
                type == AccountType.BOT ? MessageEmbed.EMBED_MAX_LENGTH_BOT : MessageEmbed.EMBED_MAX_LENGTH_CLIENT, type);
        }
        this.embed = embed;
        return this;
    }

    @Override
    public MessageAction append(final CharSequence csq)
    {
        return append(csq, 0, csq.length());
    }

    @Override
    public MessageAction append(final CharSequence csq, final int start, final int end)
    {
        if (content.length() + end - start > Message.MAX_CONTENT_LENGTH)
            throw new IllegalArgumentException("A message may not exceed 2000 characters. Please limit your input!");
        content.append(csq, start, end);
        return this;
    }

    @Override
    public MessageAction append(final char c)
    {
        if (content.length() == Message.MAX_CONTENT_LENGTH)
            throw new IllegalArgumentException("A message may not exceed 2000 characters. Please limit your input!");
        content.append(c);
        return this;
    }

    public MessageAction appendFormat(final String format, final Object... args)
    {
        this.content.append(String.format(format, args));
        return this;
    }

    public MessageAction addFile(final InputStream data, final String name)
    {
        checkEdit();
        Checks.notNull(data, "Data");
        Checks.notBlank(name, "Name");
        checkFileAmount();
        files.put(name, data);
        return this;
    }

    public MessageAction addFile(final byte[] data, final String name)
    {
        checkEdit();
        Checks.notNull(data, "Data");
        Checks.notBlank(name, "Name");
        Checks.check(data.length <= Message.MAX_FILE_SIZE, "File may not exceed the maximum file length of 8MB!");
        checkFileAmount();
        files.put(name, new ByteArrayInputStream(data));
        return this;
    }

    public MessageAction addFile(final File file)
    {
        Checks.notNull(file, "File");
        return addFile(file, file.getName());
    }

    public MessageAction addFile(final File file, final String name)
    {
        checkEdit();
        Checks.notNull(file, "File");
        Checks.notBlank(name, "File Name");
        Checks.check(file.exists() && file.canRead(),
            "Provided file either does not exist or cannot be read from!");
        Checks.check(file.length() <= Message.MAX_FILE_SIZE, "File may not exceed the maximum file length of 8MB!");
        checkFileAmount();
        try
        {
            files.put(name, new FileInputStream(file));
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        return this;
    }

    public MessageAction clearFiles()
    {
        files.clear();
        return this;
    }

    public MessageAction override(final boolean bool)
    {
        this.override = bool;
        return this;
    }

    protected RequestBody asMultipart()
    {
        final MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        final MediaType type = MediaType.parse("application/octet-stream");
        int index = 0;
        for (Map.Entry<String, InputStream> entry : files.entrySet())
        {
            final RequestBody body = MiscUtil.createRequestBody(type, entry.getValue());
            builder.addFormDataPart("file" + index++, entry.getKey(), body);
        }
        if (!isEmpty())
            builder.addFormDataPart("payload_json", getJSON().toString());
        return builder.build();
    }

    protected RequestBody asJSON()
    {
        return RequestBody.create(MediaType.parse("application/json"), getJSON().toString());
    }

    protected JSONObject getJSON()
    {
        final JSONObject obj = new JSONObject();
        if (override)
        {
            if (embed == null)
                obj.put("embed", JSONObject.NULL);
            else
                obj.put("embed", getJSONEmbed(embed));
            if (content.length() == 0)
                obj.put("content", JSONObject.NULL);
            else
                obj.put("content", content.toString());
            if (nonce == null)
                obj.put("nonce", JSONObject.NULL);
            else
                obj.put("nonce", nonce);
            obj.put("tts", tts);
        }
        else
        {
            if (embed != null)
                obj.put("embed", getJSONEmbed(embed));
            if (content.length() > 0)
                obj.put("content", content.toString());
            if (nonce != null)
                obj.put("nonce", nonce);
            obj.put("tts", tts);
        }
        return obj;
    }

    protected static JSONObject getJSONEmbed(final MessageEmbed embed)
    {
        return ((MessageEmbedImpl) embed).toJSONObject();
    }

    protected void checkFileAmount()
    {
        if (files.size() >= Message.MAX_FILE_AMOUNT)
            throw new IllegalStateException("Cannot add more than " + Message.MAX_FILE_AMOUNT + " files!");
    }

    protected void checkEdit()
    {
        if (isEdit())
            throw new IllegalStateException("Cannot add files to an existing message! Edit-Message does not support this operation!");
    }

    @Override
    protected RequestBody finalizeData()
    {
        if (!files.isEmpty())
            return asMultipart();
        else if (!isEmpty())
            return asJSON();
        throw new IllegalStateException("Cannot build a message without content!");
    }

    @Override
    protected void handleResponse(Response response, Request<Message> request)
    {
        if (response.isOk())
            request.onSuccess(api.getEntityBuilder().createMessage(response.getObject()));
        else
            request.onFailure(response);
    }
}

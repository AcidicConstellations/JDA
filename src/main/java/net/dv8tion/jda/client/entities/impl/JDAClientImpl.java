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

import gnu.trove.map.TLongObjectMap;
import java.util.*;
import java.util.stream.Collectors;
import net.dv8tion.jda.client.JDAClient;
import net.dv8tion.jda.client.entities.*;
import net.dv8tion.jda.client.requests.restaction.ApplicationAction;
import net.dv8tion.jda.client.requests.restaction.SearchPaginationAction;
import net.dv8tion.jda.client.requests.restaction.pagination.MentionPaginationAction;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.entities.impl.GuildImpl;
import net.dv8tion.jda.core.entities.impl.JDAImpl;
import net.dv8tion.jda.core.entities.impl.UserImpl;
import net.dv8tion.jda.core.exceptions.GuildUnavailableException;
import net.dv8tion.jda.core.requests.Request;
import net.dv8tion.jda.core.requests.Response;
import net.dv8tion.jda.core.requests.RestAction;
import net.dv8tion.jda.core.requests.Route;
import net.dv8tion.jda.core.utils.MiscUtil;
import net.dv8tion.jda.core.utils.Checks;
import org.json.JSONArray;
import org.json.JSONObject;

public class JDAClientImpl implements JDAClient
{
    protected final JDAImpl api;
    protected final TLongObjectMap<Group> groups = MiscUtil.newLongMap();
    protected final TLongObjectMap<Relationship> relationships = MiscUtil.newLongMap();
    protected final TLongObjectMap<CallUser> callUsers = MiscUtil.newLongMap();
    protected final UserSettingsImpl userSettings;

    public JDAClientImpl(JDAImpl api)
    {
        this.api = api;

        this.userSettings = new UserSettingsImpl(api);
    }

    @Override
    public JDA getJDA()
    {
        return api;
    }

    @Override
    public RestAction<Group> createGroup(Collection<Friend> friends)
    {
        Checks.notNull(friends, "friends");
        return createGroup(friends.stream()
                .map(f -> f.getUser().getId())
                .toArray(String[]::new));
    }

    @Override
    public RestAction<Group> createGroup(Friend... friends)
    {
        Checks.notNull(friends, "friends");
        return createGroup(Arrays.stream(friends)
                .map(f -> f.getUser().getId())
                .toArray(String[]::new));
    }

    @Override
    public RestAction<Group> createGroup(long... friendIds)
    {
        Checks.notNull(friendIds, "friendIds");
        return createGroup(Arrays.stream(friendIds)
                .mapToObj(Long::toUnsignedString)
                .toArray(String[]::new));
    }

    @Override
    public RestAction<Group> createGroup(String... friendIds)
    {
        Checks.notNull(friendIds, "friendIds");
        Checks.check(friendIds.length > 1, "can only create a group with at least 2 members");
        for (String id : friendIds)
            Checks.check(isFriend(id), "All recipients have to be friends");

        JSONObject body = new JSONObject().put("recipients", friendIds);

        final Route.CompiledRoute route = Route.Self.CREATE_PRIVATE_CHANNEL.compile();
        return new RestAction<Group>(this.getJDA(), route, body)
        {
            @Override
            protected void handleResponse(final Response response, final Request<Group> request)
            {
                if (!response.isOk())
                    request.onFailure(response);
                else
                    request.onSuccess(api.getEntityBuilder().createGroup(response.getObject()));
            }
        };
    }

    @Override
    public List<Group> getGroups()
    {
        return Collections.unmodifiableList(new ArrayList<>(groups.valueCollection()));
    }

    @Override
    public List<Group> getGroupsByName(String name, boolean ignoreCase)
    {
        return Collections.unmodifiableList(groups.valueCollection().stream()
                .filter(g -> g.getName() != null && (ignoreCase ? g.getName().equalsIgnoreCase(name) : g.getName().equals(name)))
                .collect(Collectors.toList()));
    }

    @Override
    public Group getGroupById(String id)
    {
        return groups.get(MiscUtil.parseSnowflake(id));
    }

    @Override
    public Group getGroupById(long id)
    {
        return groups.get(id);
    }

    @Override
    public List<Relationship> getRelationships()
    {
        return Collections.unmodifiableList(new ArrayList<>(relationships.valueCollection()));
    }

    @Override
    public List<Relationship> getRelationships(RelationshipType type)
    {
        return getRelationships(type, Relationship.class);
    }

    @Override
    public List<Relationship> getRelationships(RelationshipType type, String name, boolean ignoreCase)
    {
        return getRelationships(type, Relationship.class, name, ignoreCase);
    }

    @Override
    public List<Relationship> getRelationshipsByName(String name, boolean ignoreCase)
    {
        return Collections.unmodifiableList(relationships.valueCollection().stream()
                .filter(r -> (ignoreCase ? r.getUser().getName().equalsIgnoreCase(name) : r.getUser().getName().equals(name)))
                .collect(Collectors.toList()));
    }

    @Override
    public Relationship getRelationship(User user)
    {
        return getRelationshipById(user.getIdLong());
    }

    @Override
    public Relationship getRelationshipById(String id)
    {
        return relationships.get(MiscUtil.parseSnowflake(id));
    }

    @Override
    public Relationship getRelationshipById(long id)
    {
        return relationships.get(id);
    }

    @Override
    public boolean hasRelationship(User user)
    {
        return hasRelationship(user.getIdLong());
    }

    @Override
    public boolean hasRelationship(String userId)
    {
        return hasRelationship(MiscUtil.parseSnowflake(userId));
    }

    @Override
    public boolean hasRelationship(long userId)
    {
        return relationships.containsKey(userId);
    }

    @Override
    public Relationship getRelationship(User user, RelationshipType type)
    {
        return getRelationshipById(user.getIdLong(), type);
    }

    @Override
    public Relationship getRelationshipById(String id, RelationshipType type)
    {
        return getRelationshipById(MiscUtil.parseSnowflake(id), type);
    }

    @Override
    public Relationship getRelationshipById(long id, RelationshipType type)
    {
        Relationship relationship = getRelationshipById(id);
        return relationship != null && relationship.getType() == type ? relationship : null;
    }

    @Override
    public boolean hasRelationship(long userId, RelationshipType type)
    {
        return relationships.containsKey(userId);
    }

    @Override
    public boolean hasRelationship(String userId, RelationshipType type)
    {
        return hasRelationship(MiscUtil.parseSnowflake(userId));
    }

    @Override
    public boolean hasRelationship(User user, RelationshipType type)
    {
        return hasRelationship(user.getIdLong());
    }

    @Override
    public List<Friend> getFriends()
    {
        return getRelationships(RelationshipType.FRIEND, Friend.class);
    }

    @Override
    public List<Friend> getFriendsByName(String name, boolean ignoreCase)
    {
        return getRelationships(RelationshipType.FRIEND, Friend.class, name, ignoreCase);
    }

    @Override
    public Friend getFriend(User user)
    {
        return getFriendById(user.getIdLong());
    }

    @Override
    public Friend getFriendById(String id)
    {
        return getFriendById(MiscUtil.parseSnowflake(id));
    }

    @Override
    public Friend getFriendById(long id)
    {
        return getRelationshipById(id, RelationshipType.FRIEND, Friend.class);
    }

    @Override
    public boolean isFriend(User user)
    {
        return isFriend(user.getId());
    }

    @Override
    public boolean isFriend(String userId)
    {
        return isFriend(MiscUtil.parseSnowflake(userId));
    }

    @Override
    public boolean isFriend(long userId)
    {
        return hasRelationship(userId, RelationshipType.FRIEND);
    }

    @Override
    public List<BlockedUser> getBlockedUsers()
    {
        return getRelationships(RelationshipType.BLOCKED, BlockedUser.class);
    }

    @Override
    public List<BlockedUser> getBlockedUsersByName(String name, boolean ignoreCase)
    {
        return getRelationships(RelationshipType.BLOCKED, BlockedUser.class, name, ignoreCase);
    }

    @Override
    public BlockedUser getBlockedUser(User user)
    {
        return getBlockedUserById(user.getIdLong());
    }

    @Override
    public BlockedUser getBlockedUser(Member member)
    {
        return getBlockedUser(member.getUser());
    }

    @Override
    public BlockedUser getBlockedUserById(String id)
    {
        return getBlockedUserById(MiscUtil.parseSnowflake(id));
    }

    @Override
    public BlockedUser getBlockedUserById(long id)
    {
        return getRelationshipById(id, RelationshipType.BLOCKED, BlockedUser.class);
    }

    @Override
    public boolean isBlocked(long userId)
    {
        return hasRelationship(userId, RelationshipType.BLOCKED);
    }

    @Override
    public boolean isBlocked(String userId)
    {
        return isBlocked(MiscUtil.parseSnowflake(userId));
    }

    @Override
    public boolean isBlocked(User user)
    {
        return hasRelationship(user.getIdLong());
    }

    @Override
    public List<IncomingFriendRequest> getIncomingFriendRequests()
    {
        return getRelationships(RelationshipType.INCOMING_FRIEND_REQUEST, IncomingFriendRequest.class);
    }

    @Override
    public List<IncomingFriendRequest> getIncomingFriendRequestsByName(String name, boolean ignoreCase)
    {
        return getRelationships(RelationshipType.INCOMING_FRIEND_REQUEST, IncomingFriendRequest.class, name, ignoreCase);
    }

    @Override
    public IncomingFriendRequest getIncomingFriendRequest(User user)
    {
        return getIncomingFriendRequestById(user.getIdLong());
    }

    @Override
    public IncomingFriendRequest getIncomingFriendRequestById(String id)
    {
        return getIncomingFriendRequestById(MiscUtil.parseSnowflake(id));
    }

    @Override
    public IncomingFriendRequest getIncomingFriendRequestById(long id)
    {
        return getRelationshipById(id, RelationshipType.INCOMING_FRIEND_REQUEST, IncomingFriendRequest.class);
    }

    @Override
    public boolean isFriendRquestIncoming(long userId)
    {
        return hasRelationship(userId, RelationshipType.INCOMING_FRIEND_REQUEST);
    }

    @Override
    public boolean isFriendRquestIncoming(String userId)
    {
        return isFriendRquestIncoming(MiscUtil.parseSnowflake(userId));
    }

    @Override
    public boolean isFriendRquestIncoming(User user)
    {
        return hasRelationship(user.getIdLong());
    }

    @Override
    public List<OutgoingFriendRequest> getOutgoingFriendRequests()
    {
        return getRelationships(RelationshipType.OUTGOING_FRIEND_REQUEST, OutgoingFriendRequest.class);
    }

    @Override
    public List<OutgoingFriendRequest> getOutgoingFriendRequestsByName(String name, boolean ignoreCase)
    {
        return getRelationships(RelationshipType.OUTGOING_FRIEND_REQUEST, OutgoingFriendRequest.class, name, ignoreCase);
    }

    @Override
    public OutgoingFriendRequest getOutgoingFriendRequest(User user)
    {
        return getOutgoingFriendRequestById(user.getIdLong());
    }

    @Override
    public OutgoingFriendRequest getOutgoingFriendRequestById(String id)
    {
        return getOutgoingFriendRequestById(MiscUtil.parseSnowflake(id));
    }

    @Override
    public OutgoingFriendRequest getOutgoingFriendRequestById(long id)
    {
        return getRelationshipById(id, RelationshipType.OUTGOING_FRIEND_REQUEST, OutgoingFriendRequest.class);
    }

    @Override
    public boolean isFriendRquestOutgoing(long userId)
    {
        return hasRelationship(userId, RelationshipType.FRIEND);
    }

    @Override
    public boolean isFriendRquestOutgoing(String userId)
    {
        return isFriendRquestOutgoing(MiscUtil.parseSnowflake(userId));
    }

    @Override
    public boolean isFriendRquestOutgoing(User user)
    {
        return hasRelationship(user.getIdLong());
    }

    private <T extends Relationship> List<T> getRelationships(RelationshipType type, Class<T> clazz)
    {
        return Collections.unmodifiableList(relationships.valueCollection().stream()
                .filter(r -> r.getType().equals(type))
                .map(clazz::cast)
                .collect(Collectors.toList()));
    }

    private <T extends Relationship> T getRelationshipById(long id, RelationshipType type, Class<T> clazz)
    {
        Relationship relationship = getRelationshipById(id);
        if (relationship != null && relationship.getType() == type)
            return clazz.cast(relationship);
        return null;
    }

    private <T extends Relationship> List<T> getRelationships(RelationshipType type, Class<T> clazz, String name, boolean ignoreCase)
    {
        return Collections.unmodifiableList(relationships.valueCollection().stream()
                .filter(r -> r.getType().equals(type))
                .filter(r -> (ignoreCase ? r.getUser().getName().equalsIgnoreCase(name) : r.getUser().getName().equals(name)))
                .map(clazz::cast)
                .collect(Collectors.toList()));
    }

    @Override
    public RestAction<List<FriendSuggestion>> getFriendSuggestions()
    {
        Route.CompiledRoute route = Route.Self.FRIEND_SUGGESTIONS.compile();

        return new RestAction<List<FriendSuggestion>>(api, route)
        {
            @Override
            protected void handleResponse(Response response, Request<List<FriendSuggestion>> request)
            {
                if (response.isOk())
                {
                    EntityBuilder entityBuilder = this.api.getEntityBuilder();
                    JSONArray array = response.getArray();
                    List<FriendSuggestion> suggestions = new ArrayList<>(array.length());
                    for (int i = 0; i < array.length(); i++)
                        suggestions.add(entityBuilder.createFriendSuggestion(array.getJSONObject(i)));
                    request.onSuccess(suggestions);
                }
                else
                {
                    request.onFailure(response);
                }
            }
        };
    }

    @Override
    public RestAction<Void> sendFriedRequest(String username, int discriminator) // Error 80004
    {
        Route.CompiledRoute route = Route.Relationships.ADD_FRIEND_NAME_DISCRIM.compile();
        JSONObject data = new JSONObject()
                .put("username", username)
                .put("discriminator", discriminator);

        return new RestAction<Void>(api, route, data)
        {
            @Override
            protected void handleResponse(Response response, Request<Void> request)
            {
                if (response.isOk())
                    request.onSuccess(null);
                else
                    request.onFailure(response);
            }
        };
    }

    @Override
    public RestAction<Void> sendFriedRequest(User user)
    {
        Checks.notNull(user, "user");
        return sendFriedRequest(user.getId());
    }

    @Override
    public RestAction<Void> sendFriedRequest(String user)
    {
        Checks.notNull(user, "user");
        return addRelationship(user, RelationshipType.FRIEND.getKey());
    }

    @Override
    public RestAction<Void> sendFriedRequest(long user)
    {
        return sendFriedRequest(Long.toUnsignedString(user));
    }

    @Override
    public RestAction<Void> block(User user)
    {
        Checks.notNull(user, "user");
        return block(user.getId());
    }

    @Override
    public RestAction<Void> block(String user)
    {
        Checks.notNull(user, "user");
        return addRelationship(user, RelationshipType.BLOCKED.getKey());
    }

    @Override
    public RestAction<Void> block(long user)
    {
        return block(Long.toUnsignedString(user));
    }

    private RestAction<Void> addRelationship(String user, int type)
    {
        Route.CompiledRoute route = Route.Relationships.ADD_RELATIONSHIP.compile(user);
        JSONObject data = new JSONObject().put("type", type);
        return new RestAction<Void>(api, route, data)
        {
            @Override
            protected void handleResponse(Response response, Request<Void> request)
            {
                if (response.isOk())
                    request.onSuccess(null);
                else
                    request.onFailure(response);
            }
        };
    }

    @Override
    public String getNote(User user)
    {
        Checks.notNull(user, "user");
        return ((UserImpl)user).getNote();
    }

    @Override
    public String getNote(String user)
    {
        Checks.notNull(user, "user");
        return getNote(MiscUtil.parseSnowflake(user));
    }

    @Override
    public String getNote(long user)
    {
        return getNote(api.getUserMap().get(user));
    }

    @Override
    public RestAction<Void> setNote(User user, String text)
    {
        return setNote(user.getId(), text);
    }

    @Override
    public RestAction<Void> setNote(String user, String text)
    {
        Checks.notNull(user, "user");
        Checks.check(text.length() <= 256, "text too long");

        Route.CompiledRoute route = Route.Users.SET_NOTE.compile(user);
        JSONObject data = new JSONObject().put("note", text);
        return new RestAction<Void>(api, route, data)
        {
            @Override
            protected void handleResponse(Response response, Request<Void> request)
            {
                if (response.isOk())
                    request.onSuccess(null);
                else
                    request.onFailure(response);
            }
        };
    }

    @Override
    public RestAction<Void> setNote(long user, String text)
    {
        return setNote(Long.toUnsignedString(user), text);
    }

    @Override
    public UserSettings getUserSettings()
    {
        return userSettings;
    }

    @Override
    public GuildSettings getGuildSettings(Guild guild)
    {
        Checks.notNull(guild, "guild");
        return ((GuildImpl)guild).getGuildSettings();
    }

    @Override
    public GuildSettings getGuildSettings(String guild)
    {
        Checks.notNull(guild, "guild");
        return getGuildSettings(MiscUtil.parseSnowflake(guild));
    }

    @Override
    public GuildSettings getGuildSettings(long guild)
    {
        return getGuildSettings(api.getGuildMap().get(guild));
    }

    @Override
    public SearchPaginationAction search(Guild guild)
    {
        return new SearchPaginationAction(guild);
    }

    @Override
    public SearchPaginationAction search(PrivateChannel channel)
    {
        return new SearchPaginationAction(channel);
    }

    @Override
    public SearchPaginationAction search(Group group)
    {
        return new SearchPaginationAction(group);
    }

    @Override
    public MentionPaginationAction getRecentMentions()
    {
        return new MentionPaginationAction(getJDA());
    }

    @Override
    public MentionPaginationAction getRecentMentions(Guild guild)
    {
        Checks.notNull(guild, "Guild");
        if (!guild.isAvailable())
            throw new GuildUnavailableException("Cannot retrieve recent mentions for this Guild due to it being temporarily unavailable!");
        return new MentionPaginationAction(guild);
    }

    public TLongObjectMap<Group> getGroupMap()
    {
        return groups;
    }

    public TLongObjectMap<Relationship> getRelationshipMap()
    {
        return relationships;
    }

    public TLongObjectMap<CallUser> getCallUserMap()
    {
        return callUsers;
    }

    @Override
    public ApplicationAction createApplication(String name)
    {
        return new ApplicationAction(api, name);
    }

    @Override
    public RestAction<List<Application>> getApplications()
    {
        Route.CompiledRoute route = Route.Applications.GET_APPLICATIONS.compile();
        return new RestAction<List<Application>>(api, route)
        {
            @Override
            protected void handleResponse(Response response, Request<List<Application>> request)
            {
                if (response.isOk())
                {
                    JSONArray array = response.getArray();
                    List<Application> applications = new ArrayList<>(array.length());
                    EntityBuilder entityBuilder = api.getEntityBuilder();

                    for (int i = 0; i < array.length(); i++)
                        applications.add(entityBuilder.createApplication(array.getJSONObject(i)));

                    request.onSuccess(Collections.unmodifiableList(applications));
                }
                else
                {
                    request.onFailure(response);
                }
            }
        };
    }

    @Override
    public RestAction<Application> getApplicationById(String id)
    {
        Checks.notEmpty(id, "id");

        Route.CompiledRoute route = Route.Applications.GET_APPLICATION.compile(id);
        return new RestAction<Application>(api, route)
        {
            @Override
            protected void handleResponse(Response response, Request<Application> request)
            {
                if (response.isOk())
                    request.onSuccess(api.getEntityBuilder().createApplication(response.getObject()));
                else
                    request.onFailure(response);
            }
        };
    }

    @Override
    public RestAction<List<AuthorizedApplication>> getAuthorizedApplications()
    {
        Route.CompiledRoute route = Route.Applications.GET_AUTHORIZED_APPLICATIONS.compile();
        return new RestAction<List<AuthorizedApplication>>(api, route)
        {
            @Override
            protected void handleResponse(Response response, Request<List<AuthorizedApplication>> request)
            {
                if (response.isOk())
                {
                    JSONArray array = response.getArray();
                    List<AuthorizedApplication> applications = new ArrayList<>(array.length());
                    EntityBuilder entityBuilder = api.getEntityBuilder();

                    for (int i = 0; i < array.length(); i++)
                        applications.add(entityBuilder.createAuthorizedApplication(array.getJSONObject(i)));

                    request.onSuccess(Collections.unmodifiableList(applications));
                }
                else
                {
                    request.onFailure(response);
                }
            }
        };
    }

    @Override
    public RestAction<AuthorizedApplication> getAuthorizedApplicationById(String id)
    {
        Checks.notEmpty(id, "id");

        Route.CompiledRoute route = Route.Applications.GET_AUTHORIZED_APPLICATION.compile(id);
        return new RestAction<AuthorizedApplication>(api, route)
        {
            @Override
            protected void handleResponse(Response response, Request<AuthorizedApplication> request)
            {
                if (response.isOk())
                    request.onSuccess(api.getEntityBuilder().createAuthorizedApplication(response.getObject()));
                else
                    request.onFailure(response);
            }
        };
    }
}

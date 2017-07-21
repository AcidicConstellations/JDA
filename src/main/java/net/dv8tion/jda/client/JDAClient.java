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

package net.dv8tion.jda.client;

import net.dv8tion.jda.client.entities.*;
import net.dv8tion.jda.client.requests.restaction.ApplicationAction;
import net.dv8tion.jda.client.requests.restaction.SearchPaginationAction;
import net.dv8tion.jda.client.requests.restaction.pagination.MentionPaginationAction;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.exceptions.ErrorResponseException;
import net.dv8tion.jda.core.requests.ErrorResponse;
import net.dv8tion.jda.core.requests.RestAction;
import java.util.Collection;
import java.util.List;

import javax.annotation.CheckReturnValue;

public interface JDAClient
{
    JDA getJDA();

    RestAction<Group> createGroup(Friend... friends);
    RestAction<Group> createGroup(Collection<Friend> friends);
    RestAction<Group> createGroup(long... friendIds);
    RestAction<Group> createGroup(String... friendIds);

    List<Group> getGroups();
    List<Group> getGroupsByName(String name, boolean ignoreCase);
    Group getGroupById(String id);
    Group getGroupById(long id);

    // relationship stuff

    List<Relationship> getRelationships();
    List<Relationship> getRelationshipsByName(String name, boolean ignoreCase);
    Relationship getRelationship(User user);
    Relationship getRelationshipById(String id);
    Relationship getRelationshipById(long id);
    boolean hasRelationship(User user);
    boolean hasRelationship(String userId);
    boolean hasRelationship(long userId);

    List<Relationship> getRelationships(RelationshipType type);
    List<Relationship> getRelationships(RelationshipType type, String name, boolean ignoreCase);
    Relationship getRelationship(User user, RelationshipType type);
    Relationship getRelationshipById(String id, RelationshipType type);
    Relationship getRelationshipById(long id, RelationshipType type);
    boolean hasRelationship(User user, RelationshipType type);
    boolean hasRelationship(String userId, RelationshipType type);
    boolean hasRelationship(long userId, RelationshipType type);

    List<Friend> getFriends();
    List<Friend> getFriendsByName(String name, boolean ignoreCase);
    Friend getFriend(User user);
    Friend getFriendById(String id);
    Friend getFriendById(long id);
    boolean isFriend(User user);
    boolean isFriend(String userId);
    boolean isFriend(long userId);

    List<BlockedUser> getBlockedUsers();
    List<BlockedUser> getBlockedUsersByName(String name, boolean ignoreCase);
    BlockedUser getBlockedUser(User user);
    BlockedUser getBlockedUser(Member member);
    BlockedUser getBlockedUserById(String id);
    BlockedUser getBlockedUserById(long id);
    boolean isBlocked(User user);
    boolean isBlocked(String userId);
    boolean isBlocked(long userId);

    List<IncomingFriendRequest> getIncomingFriendRequests();
    List<IncomingFriendRequest> getIncomingFriendRequestsByName(String name, boolean ignoreCase);
    IncomingFriendRequest getIncomingFriendRequest(User user);
    IncomingFriendRequest getIncomingFriendRequestById(String id);
    IncomingFriendRequest getIncomingFriendRequestById(long id);
    boolean isFriendRquestIncoming(User user);
    boolean isFriendRquestIncoming(String userId);
    boolean isFriendRquestIncoming(long userId);

    List<OutgoingFriendRequest> getOutgoingFriendRequests();
    List<OutgoingFriendRequest> getOutgoingFriendRequestsByName(String name, boolean ignoreCase);
    OutgoingFriendRequest getOutgoingFriendRequest(User user);
    OutgoingFriendRequest getOutgoingFriendRequestById(String id);
    OutgoingFriendRequest getOutgoingFriendRequestById(long id);
    boolean isFriendRquestOutgoing(User user);
    boolean isFriendRquestOutgoing(String userId);
    boolean isFriendRquestOutgoing(long userId);

    RestAction<List<FriendSuggestion>> getFriendSuggestions();

    /**
     * @throws ErrorResponseException with {@link ErrorResponse#UNKNOWN_TAG}
     */
    RestAction<Void> sendFriedRequest(String name, int discriminator);

    RestAction<Void> sendFriedRequest(User user);
    RestAction<Void> sendFriedRequest(String user);
    RestAction<Void> sendFriedRequest(long user);

    RestAction<Void> block(User user);
    RestAction<Void> block(String user);
    RestAction<Void> block(long user);

    String getNote(User user);
    String getNote(String user);
    String getNote(long user);

    RestAction<Void> setNote(User user, String text);
    RestAction<Void> setNote(String user, String text);
    RestAction<Void> setNote(long user, String text);

    UserSettings getUserSettings();

    GuildSettings getGuildSettings(Guild guild);
    GuildSettings getGuildSettings(String guild);
    GuildSettings getGuildSettings(long guild);

    SearchPaginationAction search(Guild guild);
    SearchPaginationAction search(PrivateChannel channel);
    SearchPaginationAction search(Group group);
    
    /**
     * Retrieves the recent mentions for the currently logged in
     * client account.
     *
     * <p>The returned {@link net.dv8tion.jda.client.requests.restaction.pagination.MentionPaginationAction MentionPaginationAction}
     * allows to filter by whether the messages mention everyone or a role.
     *
     * @return {@link net.dv8tion.jda.client.requests.restaction.pagination.MentionPaginationAction MentionPaginationAction}
     */
    @CheckReturnValue
    MentionPaginationAction getRecentMentions();

    /**
     * Retrieves the recent mentions for the currently logged in
     * client account.
     *
     * <p>The returned {@link net.dv8tion.jda.client.requests.restaction.pagination.MentionPaginationAction MentionPaginationAction}
     * allows to filter by whether the messages mention everyone or a role.
     *
     * <p><b>To target recent mentions from all over Discord use {@link #getRecentMentions()} instead!</b>
     *
     * @param  guild
     *         The {@link net.dv8tion.jda.core.entities.Guild Guild} to narrow recent mentions to
     *
     * @throws java.lang.IllegalArgumentException
     *         If the specified Guild is {@code null}
     * @throws net.dv8tion.jda.core.exceptions.GuildUnavailableException
     *         If the specified Guild is not currently {@link net.dv8tion.jda.core.entities.Guild#isAvailable() available}
     *
     * @return {@link net.dv8tion.jda.client.requests.restaction.pagination.MentionPaginationAction MentionPaginationAction}
     */
    @CheckReturnValue
    MentionPaginationAction getRecentMentions(Guild guild);

    /**
     * Creates a new {@link net.dv8tion.jda.client.entities.Application Application} for this user account
     * with the given name.
     *
     * <p>A name <b>must not</b> be {@code null} nor less than 2 characters or more than 32 characters long!
     *
     * <p>Possible {@link net.dv8tion.jda.core.requests.ErrorResponse ErrorResponses} caused by
     * the returned {@link net.dv8tion.jda.core.requests.RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link net.dv8tion.jda.core.requests.ErrorResponse#MAX_OAUTH_APPS MAX_OAUTH_APPS}
     *     <br>OAuth2 application limit reached</li>
     * </ul>
     *
     * @param  name
     *         The name for new {@link net.dv8tion.jda.client.entities.Application Application}
     *
     * @throws IllegalArgumentException
     *         If the provided name is {@code null}, less than 2 or more than 32 characters long
     * 
     * @return A specific {@link net.dv8tion.jda.client.requests.restaction.ApplicationAction ApplicationAction}
     *         <br>This action allows to set fields for the new application before creating it
     */
    @CheckReturnValue
    ApplicationAction createApplication(String name);

    /**
     * Retrieves all {@link net.dv8tion.jda.client.entities.Application Applications} owned by this user account.
     *
     * @return {@link net.dv8tion.jda.core.requests.RestAction RestAction} - Type: 
     *         {@link java.util.List List}{@literal <}{@link net.dv8tion.jda.client.entities.Application Application}{@literal >}
     *         <br>A list of all Applications owned by this user account.
     */
    @CheckReturnValue
    RestAction<List<Application>> getApplications();

    /**
     * Retrieves a specific {@link net.dv8tion.jda.client.entities.Application Application} owned by this user account.
     *
     * <p>Possible {@link net.dv8tion.jda.core.requests.ErrorResponse ErrorResponses}:
     * <ul>
     *     <li>{@link net.dv8tion.jda.core.requests.ErrorResponse#UNKNOWN_APPLICATION UNKNOWN_APPLICATION}
     *     <br>The Application did not exist (possibly deleted).</li>
     * </ul>
     *
     * @param  id
     *         The id for the {@link net.dv8tion.jda.client.entities.Application Application}
     * 
     * @throws IllegalArgumentException
     *         If the provided id is {@code null} or empty
     *
     * @return {@link net.dv8tion.jda.core.requests.RestAction RestAction} - Type: {@link net.dv8tion.jda.client.entities.Application Application}
     *         <br>The Application behind the provided id.
     */
    @CheckReturnValue
    RestAction<Application> getApplicationById(String id);

    /**
     * Retrieves all {@link net.dv8tion.jda.client.entities.AuthorizedApplication AuthorizedApplications} authorized by this user account.
     *
     * @return {@link net.dv8tion.jda.core.requests.RestAction RestAction} - Type: 
     *         List{@literal <}{@link net.dv8tion.jda.client.entities.AuthorizedApplication AuthorizedApplication}{@literal >}
     *         <br>A list of all AuthorizedApplications authorized by this user account.
     */
    @CheckReturnValue
    RestAction<List<AuthorizedApplication>> getAuthorizedApplications();

    /**
     * Retrieves a specific {@link net.dv8tion.jda.client.entities.AuthorizedApplication AuthorizedApplication} authorized by this user account.
     *
     * <p>Possible {@link net.dv8tion.jda.core.requests.ErrorResponse ErrorResponses}:
     * <ul>
     *     <li>{@link net.dv8tion.jda.core.requests.ErrorResponse#UNKNOWN_TOKEN UNKNOWN_TOKEN}
     *     <br>The Application either doesn't exist or isn't authorized by this user account.</li>
     * </ul>
     *
     * @param  id
     *         The id of the {@link net.dv8tion.jda.client.entities.AuthorizedApplication AuthorizedApplication}
     * 
     * @throws IllegalArgumentException If the provided id is {@code null} or empty
     *
     * @return {@link net.dv8tion.jda.core.requests.RestAction RestAction} - Type: 
     *         {@link net.dv8tion.jda.client.entities.AuthorizedApplication AuthorizedApplication}
     *         <br>The Application behind the provided id.
     */
    @CheckReturnValue
    RestAction<AuthorizedApplication> getAuthorizedApplicationById(String id);
}

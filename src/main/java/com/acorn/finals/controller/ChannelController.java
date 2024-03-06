package com.acorn.finals.controller;

import com.acorn.finals.model.dto.*;
import com.acorn.finals.service.ChannelService;
import com.acorn.finals.service.MemberService;
import com.acorn.finals.service.MessageService;
import com.acorn.finals.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/channel")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;
    private final TopicService topicService;
    private final MessageService messageService;
    private final MemberService memberService;

    /**
     * find list all channels
     *
     * @return list of the channels
     */
    @GetMapping
    public List<ChannelDto> listAllChannels() {
        return channelService.listAllChannels();
    }

    /**
     * @param inviteCode
     * @return ChannelDto return
     */
    @PostMapping("/invite/{code}")
    public ChannelDto responseChInfo(@PathVariable("code") String inviteCode) {
        return channelService.findChannelInfoByInviteCode(inviteCode);
    }

    /**
     * find a channel by id
     *
     * @param channelId id of the channel
     * @return found channel
     */
    @GetMapping("/{channelId}")
    public ChannelDto findChannelById(@PathVariable int channelId) {
        return channelService.findChannelById(channelId);
    }

    /**
     * create a new channel
     *
     * @param channelCreateRequest channel create request with channel name
     * @return created channel
     */
    @PostMapping
    public ChannelDto createNewChannel(@RequestBody ChannelDto channelCreateRequest, Authentication authentication) {
        return channelService.createNewChannel(channelCreateRequest, authentication);
    }

    /**
     * @param code(inviteCode)
     * @return ResponserEntity<ChannelDto>
     */
    @PostMapping("/join/{code}")
    public ResponseEntity<ChannelDto> joinChannel(@PathVariable String code, Authentication auth) {
        if (auth == null) {
            return ResponseEntity.badRequest().build();
        }
        var channelInfo = channelService.joinMember(auth.getName(), code, "guest");
        return ResponseEntity.ok(channelInfo);
    }

    @PatchMapping("/role")
    public boolean changeRole(@RequestBody ChannelMemberDto dto) {
        return channelService.changeRole(dto);
    }

    /**
     * update channel
     *
     * @param channelUpdateRequest channel update request with new channel name, or thumbnail url
     * @return updated channel
     */
    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelDto> updateChannel(@PathVariable int channelId, @RequestBody ChannelDto channelUpdateRequest) {
        return ResponseEntity.ok(channelService.updateChannel(channelId, channelUpdateRequest));
    }

    /**
     * delete channel
     *
     * @param channelId id of the channel
     * @return HTTP STATUS 200 on success
     */
    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable int channelId) {
        if (!channelService.deleteChannel(channelId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * list all members of channel
     *
     * @param channelId id of the channel
     * @return list of members of the channel
     */
    @GetMapping("/{channelId}/member")
    public List<MemberDto> listAllMembers(@PathVariable int channelId) {
        return channelService.listChannelMembers(channelId);
    }


    /**
     * list all topics of channel
     *
     * @param channelId id of the channel
     * @return list of topics of channel
     */
    @GetMapping("/{channelId}/topic")
    public List<TopicDto> listAllTopics(@PathVariable int channelId) {
        return topicService.findAllByChannelId(channelId);
    }

    /**
     * create new topic
     *
     * @param channelId          id of the channel that references topic
     * @param topicCreateRequest topic create request with title
     * @return created topic with title and url
     */
    @PostMapping("/{channelId}/topic")
    public TopicDto createNewTopic(@PathVariable int channelId, @RequestBody TopicDto topicCreateRequest) {
        return topicService.createNewTopic(channelId, topicCreateRequest);
    }


    /**
     * remove topic
     *
     * @param channelId          id of the channel that references topic
     * @param topicDeleteRequest topic delete request with url
     * @return HTTP STATUS 200 on success
     */
    @DeleteMapping("/{channelId}/topic/{topicId}")
    public ResponseEntity<Void> removeTopic(@PathVariable int channelId, @PathVariable int topicId, Authentication auth ) {
        var email = auth.getName();
        var role = memberService.getMemberChannelRole(email, channelId);
        if (! "owner".equals(role) && ! "manager".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (!topicService.removeTopic(topicId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(null);
    }

    /**
     * update topic
     *
     * @param channelId          id of the channel that references topic
     * @param topicId            id of the topic that will be updated
     * @param topicUpdateRequest topic update request with existing url and new title
     * @return on success, updated topic data with HTTP STATUS 200
     */
    @PatchMapping("/{channelId}/topic/{topicId}")
    public ResponseEntity<TopicDto> updateTopic(@PathVariable int channelId, @PathVariable int topicId, @RequestBody TopicDto topicUpdateRequest, Authentication auth) {
        String email = auth.getName();
        String role = memberService.getMemberChannelRole(email, channelId);

        if (!role.equals("owner") && !role.equals("manager")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (!topicService.updateTopic(channelId, topicId, topicUpdateRequest)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(topicUpdateRequest);
    }

    /**
     * list all messages of the topic
     *
     * @param channelId id of the channel that references topic
     * @param topicId   id of the topic that references message
     * @return list of the messages of the topic
     */
    @GetMapping("/{channelId}/topic/{topicId}/message")
    public List<MessageDto> listAllMessages(@PathVariable int channelId, @PathVariable int topicId) {
        return messageService.findAllByChannelIdAndTopicId(channelId, topicId);
    }

    /**
     * delete message
     *
     * @param channelId id of the channel that references topic
     * @param topicId   id of the topic that references message
     * @param messageId id of the message that will be deleted
     * @return HTTP STATUS 200 on success
     */
    @DeleteMapping("/{channelId}/topic/{topicId}/message/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable String channelId, @PathVariable String topicId, @PathVariable String messageId) {
        return ResponseEntity.ok().build();
    }

    /**
     * update message
     *
     * @param channelId            id of the channel that references topic
     * @param topicId              id of the channel that references message
     * @param messageId            id of the message will be updated
     * @param updateMessageRequest update message request with new content
     * @return updated message
     */
    @PatchMapping("/{channelId}/topic/{topicId}/message/{messageId}")
    public MessageDto updateMessage(@PathVariable String channelId, @PathVariable String topicId, @PathVariable String messageId, MessageDto updateMessageRequest) {
        return updateMessageRequest;
    }

}

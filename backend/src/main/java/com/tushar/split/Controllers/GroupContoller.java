package com.tushar.split.Controllers;

import com.tushar.split.DTO.GroupDto;
import com.tushar.split.DTO.GroupResponse;
import com.tushar.split.Service.GroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupContoller {

    @Autowired
    GroupService groupService;

    @PostMapping
    public ResponseEntity<GroupResponse> groupCreation(@RequestBody GroupDto request){
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        String username= auth.getName();
        GroupResponse response=groupService.createGroup(request,username);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    ///  get group but id
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupResponse> getGroupById(@PathVariable Long groupId ){
      GroupResponse response=groupService.getGroupById(id);
      return ResponseEntity.ok(response);
    }


    ///  get all the groups of the
    @GetMapping("/my-groups")
    public ResponseEntity<List<GroupResponse>> getUserGroups() {
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        String username= auth.getName();
        List<GroupResponse> responses = groupService.getUserGroups(username);
        return ResponseEntity.ok(responses);
    }

    /// update the group name
    @PutMapping("{groupId}")
    public ResponseEntity<GroupResponse> updateGroup(@PathVariable Long groupId , @RequestBody GroupDto request){
        GroupResponse response=groupService.updateGroup(groupId,request);
        return ResponseEntity.ok(response);
    }

    /// add members to group
    @PostMapping("/{groupId}/members")
    public ResponseEntity<GroupResponse> addMembers(@PathVariable Long groupId , @RequestBody GroupDto request){
        GroupResponse response=groupService.addMembers(groupId,request);
        return ResponseEntity.ok(response);
    }

    /// delete member from the group
    @DeleteMapping("/{groupId}/members/{userId}")
    public ResponseEntity<GroupResponse> removeMember(@PathVariable Long groupId, @PathVariable Long userId) {
        GroupResponse response = groupService.removeMember(groupId, userId);
        return ResponseEntity.ok(response);
    }

    ///  leave the group themselves
    @PostMapping("/{groupId}/leave")
    public ResponseEntity<GroupResponse> leaveGroup(@PathVariable Long groupId) {
        GroupResponse response = groupService.leaveGroup(groupId);
        return ResponseEntity.ok(response);
    }

    ///  delete a group
    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long groupId) {
        groupService.deleteGroup(groupId);
        return ResponseEntity.noContent().build();
    }


    ///  search group by name
    @GetMapping("/search")
    public ResponseEntity<List<GroupResponse>> searchGroups(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {
        List<GroupResponse> responses = groupService.searchGroups(query, limit);
        return ResponseEntity.ok(responses);
    }




}

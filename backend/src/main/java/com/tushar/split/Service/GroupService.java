package com.tushar.split.Service;

import com.tushar.split.DTO.GroupDto;
import com.tushar.split.DTO.GroupResponse;
import com.tushar.split.DTO.UserSummary;
import com.tushar.split.Model.SplitGroups;
import com.tushar.split.Model.Users;
import com.tushar.split.Repo.GroupRepo;
import com.tushar.split.Repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;

import java.sql.ClientInfoStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GroupService {

    @Autowired
    GroupRepo groupRepo;

    @Autowired
    UserRepo userRepo;


    ///  creating the group
    public GroupResponse createGroup(GroupDto request, String username) {
        SplitGroups entity=new SplitGroups();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        Users user =userRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("Group can't be created"));
        entity.setCreatedBy(user);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setLastUpdate(LocalDateTime.now());

        List<Users> members=new ArrayList<>();
        members.add(user);
        if(request.getMemberIds()!=null && !request.getMemberIds().isEmpty()){
            members.addAll(userRepo.findAllById(request.getMemberIds()));
        }
        entity.setMembers(members);
        groupRepo.save(entity);

        ///  group response
        GroupResponse response=new GroupResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());

        ///  user summary
        UserSummary summary=new UserSummary();
        summary.setUsername(username);
        summary.setEmail(user.getEmail());
        summary.setId(user.getId());
        response.setCreatedBy(summary);
        response.setMemberCount(members.size());
        response.setCreatedAt(entity.getCreatedAt());
        response.setLastUpdate(entity.getLastUpdate());
        response.setIsAdmin(true);

        ///  set the summary

        List<UserSummary> userSummaries=new ArrayList<>();
        for(Users member: members){
            UserSummary summ=new UserSummary();
            summ.setUsername(member.getUsername());
            summ.setEmail(member.getEmail());
            summ.setId(member.getId());
            userSummaries.add(summ);
        }
        response.setMembers(userSummaries);
       return response;
    }

    ///  get the group by id
    public GroupResponse getGroupById(int id){

        Authentication auth= SecurityContextHolder.getContext().getAuthentication();

        Users currentUser=userRepo.findByUsername(auth.getName()).orElse(null);

        SplitGroups group = findGroupById(id);

        if(!isUserMember(group,currentUser)){
            log.warn("user {} is not the memeber of the group{}",currentUser.getUsername(),group.getName());
            throw new RuntimeException("You not the memeber of this group ");
        }

        return maptoResponse(group,currentUser);
    }



    ///  get all the groups of the user
    public List<GroupResponse> getUserGroups(String username) {

        Users currentUser =userRepo.findByUsername(username).orElseThrow(
                ()-> new RuntimeException("Current user Not Found")
        );
        List<SplitGroups> groups=currentUser.getGroups();
        ///  if user have no groups
        if(groups==null) return new ArrayList<>();


        List<GroupResponse> groupResponses=new ArrayList<>();
        for(SplitGroups group : groups){
            GroupResponse response=maptoResponse(group,currentUser);
            groupResponses.add(response);
        }
        return groupResponses;

    }



    ///  update the group name
    public GroupResponse updateGroup(int  groupId, GroupDto request) {
        SplitGroups oldGroup=groupRepo.findById(groupId).orElseThrow(
                ()-> new RuntimeException("NO Such Group Exist")
        );

        Authentication auth =SecurityContextHolder.getContext().getAuthentication();
        if(!oldGroup.getCreatedBy().getUsername().equals(auth.getName()))
        {
            throw new RuntimeException("Only group creator can update group");
        }
        oldGroup.setName(request.getName());
        oldGroup.setDescription(request.getDescription());
        oldGroup.setLastUpdate(LocalDateTime.now());
        groupRepo.save(oldGroup);

        Users currentUser=oldGroup.getCreatedBy();
        return maptoResponse(oldGroup,currentUser);
    }



    ///  add the member of the group(only creator can add the memebers)
    public GroupResponse addMembers(int groupId, List<Integer> memberIds) {
        SplitGroups group=groupRepo.findById(groupId).orElseThrow(
                ()-> new RuntimeException("Group Not Found")
        );

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        if (!group.getCreatedBy().getUsername().equals(currentUsername)) {
            throw new RuntimeException("Only group creator can add members");
        }


        List<Integer> existingMemberIds = group.getMembers().stream()
                .map(Users::getId)
                .collect(Collectors.toList());

        List<Integer> newMemberIds = memberIds.stream()
                .filter(id -> !existingMemberIds.contains(id))
                .collect(Collectors.toList());

        if (newMemberIds.isEmpty()) {
            return maptoResponse(group, group.getCreatedBy());
        }

        List<Users> newMembers = userRepo.findAllById(newMemberIds);


        group.getMembers().addAll(newMembers);
        group.setLastUpdate(LocalDateTime.now());


        SplitGroups updatedGroup = groupRepo.save(group);
        return maptoResponse(updatedGroup, group.getCreatedBy());

    }


    /// remove the member of the group(only creator can )

    public GroupResponse removeMember(int groupId, Long userId) {

        SplitGroups group= groupRepo.findById(groupId).orElseThrow(
                ()-> new RuntimeException("Group Not Found")
        );
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
         Users currentUser= userRepo.findByUsername(auth.getName()).orElseThrow(
                 ()-> new RuntimeException("user Not Found")
         );


         if(!currentUser.getUsername().equals(group.getCreatedBy().getUsername())) throw new RuntimeException("Only cretor can Remove");


        boolean isMemberOfGroup=group.getMembers().stream()
                .anyMatch(m -> m.getId()==(userId));

        if(!isMemberOfGroup) throw new RuntimeException("The User is not The Member Of The Group");

        if(currentUser.getId()==(userId)) throw new RuntimeException("Creator can't be Removed");


        boolean removed = group.getMembers().removeIf(member -> member.getId()==(userId));

        if(removed){
            group.setLastUpdate(LocalDateTime.now());
            groupRepo.save(group);
        }
        return maptoResponse(group,currentUser);

    }



    ///  delete the group(only the creator of the group can delete the group)
    public void deleteGroup(int groupId) {

        SplitGroups group =groupRepo.findById(groupId).orElseThrow(
                ()-> new RuntimeException("group Not Found")
        );

        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        Users currentUser=userRepo.findByUsername(auth.getName()).orElseThrow(
                ()-> new RuntimeException("User Not Found")
        );
        if (!(group.getCreatedBy().getId()==currentUser.getId())) {
            throw new RuntimeException("Only group creator can delete the group");
        }
        groupRepo.deleteById(groupId);
    }


    ///  leave the group
    public GroupResponse leaveGroup( int groupId) {
        SplitGroups group = groupRepo.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));

        // Get current authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        Users currentUser = userRepo.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //  Check if user is actually a member of the group
        boolean isMember = group.getMembers().stream()
                .anyMatch(member -> member.getId()==(currentUser.getId()));

        if (!isMember) {
            throw new RuntimeException("You are not a member of this group");
        }

        //  creator cannot leave, must delete group
        if (group.getCreatedBy().getId()==(currentUser.getId())) {
            throw new RuntimeException("Creator cannot leave the group. Use DELETE endpoint to delete the group instead.");
        }

        //  Remove the user from members list
        boolean removed = group.getMembers().removeIf(member -> member.getId()==(currentUser.getId()));

        if (!removed) {
            throw new RuntimeException("Failed to remove user from group");
        }

        //  Update timestamp and save
        group.setLastUpdate(LocalDateTime.now());
        SplitGroups updatedGroup = groupRepo.save(group);

        //  Log the action
        log.info("User '{}' (ID: {}) left group '{}' (ID: {})", currentUser.getUsername(), currentUser.getId(), updatedGroup.getName(), updatedGroup.getId());


        return maptoResponse(updatedGroup, updatedGroup.getCreatedBy());
    }




    /// Helper Methods_-------------------------------------------------------------->>

    private SplitGroups findGroupById(int groupId) {
        return groupRepo.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));
    }

    private boolean isUserMember(SplitGroups group, Users user) {
        return group.getMembers().stream()
                .anyMatch(member -> member.getId()==user.getId());
    }

    private GroupResponse maptoResponse(SplitGroups group, Users currentUser){

        boolean isAdmin = group.getCreatedBy().getId()==(currentUser.getId());
        UserSummary creatorSummary=new UserSummary();
        creatorSummary.setId(group.getCreatedBy().getId());
        creatorSummary.setUsername(group.getCreatedBy().getUsername());
        creatorSummary.setEmail(group.getCreatedBy().getEmail());


        /// members summaries

        List<UserSummary > memberSummaries=new ArrayList<>();

        for(Users member: group.getMembers()){
            UserSummary summary=new UserSummary();
            summary.setId(member.getId());
            summary.setUsername(member.getUsername());
            summary.setEmail(member.getEmail());
            memberSummaries.add(summary);
        }

        ///  group Response

        GroupResponse response = new GroupResponse();
        response.setId(group.getId());
        response.setName(group.getName());
        response.setDescription(group.getDescription());
        response.setCreatedBy(creatorSummary);
        response.setMembers(memberSummaries);
        response.setMemberCount(group.getMembers().size());
        response.setCreatedAt(group.getCreatedAt());
        response.setLastUpdate(group.getLastUpdate());
        response.setIsAdmin(isAdmin);


        return response;
    }


    public List<GroupResponse> searchGroups(String query, int limit) {
        Authentication auth =SecurityContextHolder.getContext().getAuthentication();

        Users currentUser = userRepo.findByUsername(auth.getName()).orElseThrow(
                ()-> new RuntimeException( "User Not Found")
        );

        // returns ONLY matching groups user belongs to
        List<SplitGroups> groups = groupRepo.findUserGroupsByNameContaining(currentUser.getId(), query);

        return groups.stream()
                .limit(limit)
                .map(group -> maptoResponse(group, currentUser))
                .collect(Collectors.toList());
    }
}

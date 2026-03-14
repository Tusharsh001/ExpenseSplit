package com.tushar.split.Service;

import com.tushar.split.DTO.GroupDto;
import com.tushar.split.DTO.GroupResponse;
import com.tushar.split.DTO.UserSummary;
import com.tushar.split.Model.SplitGroups;
import com.tushar.split.Model.Users;
import com.tushar.split.Repo.GroupRepo;
import com.tushar.split.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    GroupRepo groupRepo;

    @Autowired
    UserRepo userRepo;

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



}

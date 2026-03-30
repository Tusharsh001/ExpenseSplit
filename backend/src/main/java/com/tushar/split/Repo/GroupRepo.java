package com.tushar.split.Repo;

import com.tushar.split.Model.SplitGroups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepo extends JpaRepository<SplitGroups,Integer> {


    @Query("SELECT g FROM SplitGroups g " +
            "JOIN g.members m " +
            "WHERE m.id = :userId " +
            "AND LOWER(g.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<SplitGroups> findUserGroupsByNameContaining(@Param("userId") int userId, @Param("query") String query);

}

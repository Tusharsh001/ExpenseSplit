package com.tushar.split.Repo;

import com.tushar.split.Model.SplitGroups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepo extends JpaRepository<SplitGroups,Long> {


}

package com.example.SamvaadProject.usermasterpackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.management.relation.Role;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserMaster,Long> {

    Optional<UserMaster> findByUsernameAndPassword(String username, String password);

    UserMaster getByUsernameAndPassword(String username, String password);

    @Query("select MAX(a.userId) from  UserMaster a")
    public Long getMaxCount();

    List<UserMaster> findByRoleOrderByFullNameAsc(UserMaster.Role role);
}

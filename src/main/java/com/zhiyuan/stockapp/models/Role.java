package com.zhiyuan.stockapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Zhiyuan Yao
 */

@Slf4j
@Entity
@Data
@EqualsAndHashCode(exclude = {"users"})
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    private String roleName;

    @ManyToMany(mappedBy = "roles",fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}

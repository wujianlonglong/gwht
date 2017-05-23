package com.longer.domain;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by wujianlong on 2017/5/5.
 */
@Data
@Entity(name="ROLES_INFO")
@DynamicInsert(true)
@DynamicUpdate(true)
public class RoleInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roleGenerator")
    @SequenceGenerator(name = "roleGenerator", sequenceName = "ROLE_INFO_ID_SEQ", allocationSize = 1)
    private Long roleId;

    @Column(nullable=false,unique=true,length=30)
    private String roleName;

    @Column(nullable=false)
    private Integer showOder;

    @Column(length = 100)
    private String comments;

    @Column(nullable=false)
    private Integer status;

    @Column(nullable = false,columnDefinition="date default sysdate")
    private Date createTime;

    @Transient
    private Integer chooseRole;

}

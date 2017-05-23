package com.longer.domain;

import com.longer.model.RolesMenusPk;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.util.Date;

/**
 * Created by wujianlong on 2017/5/5.
 */
@Data
@Entity(name = "ROLES_MENUS")
@IdClass(RolesMenusPk.class)
@DynamicInsert(true)
@DynamicUpdate(true)
public class RolesMenus {

    @Id
    private Long roleId;

    @Id
    private Integer menuId;

    @Column(nullable = false)
    private Integer status;

    @Column(nullable = false, columnDefinition = "date default sysdate")
    private Date createTime;
}

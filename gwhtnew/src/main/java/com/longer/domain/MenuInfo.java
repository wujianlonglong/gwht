package com.longer.domain;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wujianlong on 2017/5/4.
 */
@Data
@Entity(name = "MENUS_INFO")
@DynamicInsert(true)
@DynamicUpdate(true)
public class MenuInfo {
    @Id
    private Integer menuId;

    @Column(nullable = false, length = 30)
    private String menuName;

    @Column(nullable = false)
    private Integer parentMenuId;

    @Column(nullable = true, length = 50)
    private String menuUrl;

    @Column(nullable = false)
    private Integer showOrder;

    @Column(nullable = true, length = 100)
    private String comments;

    @Column(nullable = false)
    private Integer status;

    @Column(nullable = false, columnDefinition = "date default sysdate")
    private Date createTime;

    @Transient
    private List<MenuInfo> childMenus;

    @Transient
    private Integer chooseMenu;

    public MenuInfo() {
        childMenus = new ArrayList<>();
    }

}

package com.longer.domain;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wujianlong on 2017/7/13.
 */
@Data
@Entity(name="STORE_INFOS")
@DynamicInsert(true)
@DynamicUpdate(true)
public class StoreInfos {

    @Id
    @Column(nullable = false, length = 20)
    private String shopCode;

    @Column(nullable = false, length = 40)
    private String shopName;

    @Column(nullable = false, length = 40)
    private String shopArea;

    @Column(nullable = false, length = 60)
    private String shopPhone;

    @Column(nullable = false,length=40)
    private String shopAdminArea;

    @Column(nullable=false,length=120)
    private String shopAddress;

    @Column(nullable = false,columnDefinition="date default sysdate")
    private Date createTime;
}

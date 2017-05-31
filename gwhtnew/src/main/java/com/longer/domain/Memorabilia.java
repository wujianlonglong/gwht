package com.longer.domain;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wujianlong on 2017/5/23.
 */
@Data
@Entity(name = "MEMORABILIA")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Memorabilia {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "memorabiliaGenerator")
    @SequenceGenerator(name = "memorabiliaGenerator", sequenceName = "MEMORABILIA_SEQ", allocationSize = 1)
    private Integer id;

    /**
     * 发生时间
     */
    @Column(nullable = false, columnDefinition = "date")
    private Date happenDate;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = true, columnDefinition = "CLOB")
    private String content;

    @Column(nullable = false, columnDefinition = "date default sysdate")
    private Date createTime;

    @Column(nullable=false,columnDefinition = "number default 1")
    private Integer status;

}

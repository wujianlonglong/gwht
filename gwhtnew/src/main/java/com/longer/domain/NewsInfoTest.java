package com.longer.domain;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wujianlong on 2017/5/19.
 */
@Data
@Entity(name = "NEWS_INFO")
@DynamicInsert(true)
@DynamicUpdate(true)
public class NewsInfoTest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "newsInfoTestGenerator")
    @SequenceGenerator(name = "newsInfoTestGenerator", sequenceName = "SEQ_NEWS_INFO_ID", allocationSize = 1)//ews_INFO_Test_ID_SEQ
    private Integer newsId;

    @Column(nullable = false, columnDefinition = "varchar2(2) default '0'")
    private String newType;

    @Column(nullable = false,columnDefinition = "varchar2(128)")
    private String newsTitle;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = true, columnDefinition = "CLOB")
    private String newsContent;

    @Column(nullable = false, columnDefinition = "char(1) default '0'")
    private String isTop;

    @Column(nullable = false,columnDefinition="date default sysdate")
    private Date createTime;

    @Column(nullable=false,columnDefinition="date default sysdate")
    private Date modTime;

    @Column(nullable=true,columnDefinition = "varchar2(20)")
    private String modUser;

    @Column(nullable = true,columnDefinition = "varchar(15)")
    private String modIp;

    @Column(nullable = false,columnDefinition = "char(1) default '0'")
    private String isValid;

    @Column(nullable=true,columnDefinition = "varchar2(20) default 'admin'")
    private String showUser;

    @Column(nullable=true,columnDefinition = "date")
    private Date showTime;

    @Column(nullable = false,columnDefinition = "number default 0")
    private Integer clickCount;

    @Column(nullable=false,columnDefinition = "varchar2(255) default '三江购物'")
    private String showSource;


}

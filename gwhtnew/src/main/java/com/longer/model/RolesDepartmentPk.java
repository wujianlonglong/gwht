package com.longer.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by wujianlong on 2017/5/5.
 */
@Data
public class RolesDepartmentPk implements Serializable{
    private Long roleId;

    private String department;

}

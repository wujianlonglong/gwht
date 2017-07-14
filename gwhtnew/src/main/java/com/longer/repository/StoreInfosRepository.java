package com.longer.repository;

import com.longer.domain.StoreInfos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by wujianlong on 2017/7/13.
 */
public interface StoreInfosRepository extends JpaRepository<StoreInfos,String> {

}

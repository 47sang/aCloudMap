package com.galigeigei.acloudmap.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.galigeigei.acloudmap.infrastructure.mapper.entity.AInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 股票基础信息 Mapper 接口
 * 基础设施层：数据访问实现
 *
 * @author DDD实践
 * @since 2024-07-26
 */
@Mapper
public interface AInfoMapper extends BaseMapper<AInfoEntity> {
    
    /**
     * 根据板块代码查询股票
     */
    @Select("SELECT * FROM a_info WHERE bk_id = #{bkId}")
    List<AInfoEntity> selectByBkId(@Param("bkId") String bkId);
    
    /**
     * 根据二级板块代码查询股票
     */
    @Select("SELECT * FROM a_info WHERE ej_id = #{ejId}")
    List<AInfoEntity> selectByEjId(@Param("ejId") String ejId);
}

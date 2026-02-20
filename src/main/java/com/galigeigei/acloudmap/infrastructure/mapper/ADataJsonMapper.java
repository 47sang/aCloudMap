package com.galigeigei.acloudmap.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.galigeigei.acloudmap.infrastructure.mapper.entity.ADataJsonEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 每日数据 Mapper 接口
 * 基础设施层：数据访问实现
 *
 * @author DDD实践
 * @since 2024-07-27
 */
@Mapper
public interface ADataJsonMapper extends BaseMapper<ADataJsonEntity> {
    
    /**
     * 查询最新的一条数据
     */
    @Select("SELECT * FROM a_data_json ORDER BY id DESC LIMIT 1")
    ADataJsonEntity selectLatest();
    
    /**
     * 查询最近的两条数据
     */
    @Select("SELECT * FROM a_data_json ORDER BY id DESC LIMIT 2")
    java.util.List<ADataJsonEntity> selectLastTwo();
}

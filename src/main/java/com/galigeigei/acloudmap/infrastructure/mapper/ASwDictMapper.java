package com.galigeigei.acloudmap.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.galigeigei.acloudmap.infrastructure.mapper.entity.ASwDictEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 申万字典 Mapper 接口
 * 基础设施层：数据访问实现
 *
 * @author DDD实践
 * @since 2024-07-27
 */
@Mapper
public interface ASwDictMapper extends BaseMapper<ASwDictEntity> {
    
    /**
     * 根据类型查询
     */
    @Select("SELECT * FROM a_sw_dict WHERE type LIKE CONCAT('%', #{type}, '%')")
    List<ASwDictEntity> selectByType(@Param("type") String type);
}

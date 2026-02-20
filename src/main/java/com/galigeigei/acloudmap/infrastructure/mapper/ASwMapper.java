package com.galigeigei.acloudmap.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.galigeigei.acloudmap.infrastructure.mapper.entity.ASwEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 申万板块 Mapper 接口
 * 基础设施层：数据访问实现
 *
 * @author DDD实践
 * @since 2024-07-26
 */
@Mapper
public interface ASwMapper extends BaseMapper<ASwEntity> {
    
    /**
     * 根据行业类型查询
     */
    @Select("SELECT * FROM a_sw WHERE industry_type LIKE CONCAT('%', #{type}, '%')")
    List<ASwEntity> selectByIndustryType(@Param("type") String type);
    
    /**
     * 根据申万代码查询
     */
    @Select("SELECT * FROM a_sw WHERE sw_code = #{swCode}")
    List<ASwEntity> selectBySwCode(@Param("swCode") String swCode);
}

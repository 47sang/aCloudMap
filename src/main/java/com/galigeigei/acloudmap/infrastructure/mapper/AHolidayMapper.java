package com.galigeigei.acloudmap.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.galigeigei.acloudmap.infrastructure.mapper.entity.AHolidayEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 节假日 Mapper 接口
 * 基础设施层：数据访问实现
 *
 * @author DDD实践
 * @since 2024-10-01
 */
@Mapper
public interface AHolidayMapper extends BaseMapper<AHolidayEntity> {
    
    /**
     * 根据日期查询
     */
    @Select("SELECT * FROM a_holiday WHERE date = #{date}")
    AHolidayEntity selectByDate(@Param("date") String date);
}

package com.galigeigei.acloudmap.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.galigeigei.acloudmap.infrastructure.mapper.entity.ATodayEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 今日数据 Mapper 接口
 * 基础设施层：数据访问实现
 *
 * @author DDD实践
 * @since 2024-07-27
 */
@Mapper
public interface ATodayMapper extends BaseMapper<ATodayEntity> {
    
    /**
     * 根据日期查询股票数据
     */
    @Select("SELECT * FROM a_today WHERE today = #{date}")
    List<ATodayEntity> selectByDate(@Param("date") String date);
    
    /**
     * 查询最新日期的数据
     */
    @Select("SELECT * FROM a_today WHERE today = (SELECT MAX(today) FROM a_today) ORDER BY total DESC")
    List<ATodayEntity> selectLatest();
}

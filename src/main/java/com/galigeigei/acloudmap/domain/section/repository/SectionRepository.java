package com.galigeigei.acloudmap.domain.section.repository;

import com.galigeigei.acloudmap.domain.section.model.Section;
import com.galigeigei.acloudmap.domain.shared.valueobject.SectionCode;

import java.util.List;
import java.util.Optional;

/**
 * 板块仓储接口
 * 属于领域层，定义领域对象的持久化操作
 * 具体实现在基础设施层
 *
 * @author DDD实践
 * @since 2024-07-26
 */
public interface SectionRepository {
    
    /**
     * 根据板块代码查找板块
     *
     * @param code 板块代码
     * @return Optional<Section>
     */
    Optional<Section> findByCode(SectionCode code);
    
    /**
     * 查找所有一级板块
     *
     * @return 一级板块列表
     */
    List<Section> findAllPrimarySections();
    
    /**
     * 查找指定一级板块下的所有二级板块
     *
     * @param parentCode 父板块代码
     * @return 二级板块列表
     */
    List<Section> findSecondarySectionsByParentCode(SectionCode parentCode);
    
    /**
     * 查找所有板块
     *
     * @return 所有板块列表
     */
    List<Section> findAll();
    
    /**
     * 保存板块
     *
     * @param section 板块实体
     * @return 保存后的板块
     */
    Section save(Section section);
    
    /**
     * 批量保存板块
     *
     * @param sections 板块列表
     * @return 保存后的板块列表
     */
    List<Section> saveAll(List<Section> sections);
}

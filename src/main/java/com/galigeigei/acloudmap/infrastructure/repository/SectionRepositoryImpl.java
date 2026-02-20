package com.galigeigei.acloudmap.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.galigeigei.acloudmap.domain.section.model.Section;
import com.galigeigei.acloudmap.domain.section.repository.SectionRepository;
import com.galigeigei.acloudmap.domain.shared.valueobject.SectionCode;
import com.galigeigei.acloudmap.infrastructure.mapper.AInfoMapper;
import com.galigeigei.acloudmap.infrastructure.mapper.ASwDictMapper;
import com.galigeigei.acloudmap.infrastructure.mapper.entity.AInfoEntity;
import com.galigeigei.acloudmap.infrastructure.mapper.entity.ASwDictEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 板块仓储实现
 * 基础设施层：实现领域层定义的仓储接口
 * 负责领域对象与数据库实体之间的转换
 *
 * @author DDD实践
 * @since 2024-07-26
 */
@Repository
public class SectionRepositoryImpl implements SectionRepository {
    
    private final ASwDictMapper aSwDictMapper;
    private final AInfoMapper aInfoMapper;
    
    public SectionRepositoryImpl(ASwDictMapper aSwDictMapper, AInfoMapper aInfoMapper) {
        this.aSwDictMapper = aSwDictMapper;
        this.aInfoMapper = aInfoMapper;
    }
    
    @Override
    public Optional<Section> findByCode(SectionCode code) {
        LambdaQueryWrapper<ASwDictEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ASwDictEntity::getCode, code.getValue());
        ASwDictEntity entity = aSwDictMapper.selectOne(wrapper);
        return Optional.ofNullable(convertToDomain(entity));
    }
    
    @Override
    public List<Section> findAllPrimarySections() {
        LambdaQueryWrapper<ASwDictEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(ASwDictEntity::getType, "一");
        List<ASwDictEntity> entities = aSwDictMapper.selectList(wrapper);
        return entities.stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Section> findSecondarySectionsByParentCode(SectionCode parentCode) {
        // 从a_info表中获取二级板块信息
        LambdaQueryWrapper<AInfoEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AInfoEntity::getBkId, parentCode.getValue());
        List<AInfoEntity> entities = aInfoMapper.selectList(wrapper);
        
        // 去重并转换为Section
        return entities.stream()
                .collect(Collectors.toMap(
                        AInfoEntity::getEjId,
                        entity -> entity,
                        (existing, replacement) -> existing
                ))
                .values()
                .stream()
                .map(entity -> Section.createSecondary(
                        entity.getEjId(),
                        entity.getEjName(),
                        entity.getBkId()
                ))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Section> findAll() {
        List<ASwDictEntity> entities = aSwDictMapper.selectList(null);
        return entities.stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public Section save(Section section) {
        // 板块字典通常是只读的，不需要保存
        // 如果需要保存，可以在这里实现
        return section;
    }
    
    @Override
    public List<Section> saveAll(List<Section> sections) {
        // 板块字典通常是只读的
        return sections;
    }
    
    /**
     * 将数据库实体转换为领域对象
     */
    private Section convertToDomain(ASwDictEntity entity) {
        if (entity == null) {
            return null;
        }
        SectionCode.SectionLevel level = entity.getType() != null && entity.getType().contains("一") 
                ? SectionCode.SectionLevel.PRIMARY 
                : SectionCode.SectionLevel.SECONDARY;
        
        if (level == SectionCode.SectionLevel.PRIMARY) {
            return Section.createPrimary(entity.getCode(), entity.getName());
        } else {
            return Section.createSecondary(entity.getCode(), entity.getName(), null);
        }
    }
}

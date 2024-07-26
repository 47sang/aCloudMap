package com.galigeigei.acloudmap.service.impl;

import com.galigeigei.acloudmap.entity.ASw;
import com.galigeigei.acloudmap.entity.ASwDict;
import com.galigeigei.acloudmap.service.ASwDictService;
import com.galigeigei.acloudmap.service.ASwService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 周士钰
 * @date 2024/7/27 上午2:07
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ASwDictServiceImplTest {

    @Resource
    private ASwDictService aSwDictService;
    @Resource
    private ASwService aSwService;


    /**
     * 从申万表中过滤,保存字典列表
     */
    @Test
    public void saveDictList() {
        List<ASw> list = aSwService.list();

        // 根据 swCode 字段去重
        list = list.stream()
                .filter(item -> item.getSwCode() != null && !item.getSwCode().isEmpty())
                .collect(Collectors.toMap(
                        ASw::getSwCode, // 使用 swCode 作为键
                        Function.identity(), // 使用对象本身作为值
                        (existing, replacement) -> existing // 如果有重复的键，保留现有的对象
                ))
                .values()
                .stream()
                .collect(Collectors.toList());

        List<ASwDict> dictList = new ArrayList<>();
        list.forEach(item -> {
            ASwDict dict = new ASwDict();
            dict.setCode(item.getSwCode());
            dict.setName(item.getIndustryName());
            dict.setType(item.getIndustryType());
            dictList.add(dict);
        });

        aSwDictService.saveBatch(dictList);

    }

}
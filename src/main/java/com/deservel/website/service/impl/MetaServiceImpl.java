/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.deservel.website.service.impl;

import com.deservel.website.common.bean.ExceptionType;
import com.deservel.website.common.exception.TipRestException;
import com.deservel.website.dao.ContentsMapper;
import com.deservel.website.dao.MetasMapper;
import com.deservel.website.dao.RelationshipsMapper;
import com.deservel.website.model.dto.MetaDto;
import com.deservel.website.model.dto.Types;
import com.deservel.website.model.po.Contents;
import com.deservel.website.model.po.Metas;
import com.deservel.website.model.po.Relationships;
import com.deservel.website.service.MetaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author DeserveL
 * @date 2017/12/6 0006 下午 20:40
 * @since 1.0.0
 */
@Service
public class MetaServiceImpl implements MetaService {

    @Autowired
    MetasMapper metasMapper;

    @Autowired
    ContentsMapper contentsMapper;

    @Autowired
    RelationshipsMapper relationshipsMapper;

    /**
     * 根据类别获取
     *
     * @param type
     * @return
     */
    @Override
    public List<Metas> getMetas(String type) {
        if (StringUtils.isNotBlank(type)) {
            return metasMapper.selectByType(type);
        }
        return null;
    }

    /**
     * 根据类型和名字查询项
     *
     * @param type 类型，tag or category
     * @param name 类型名
     */
    @Override
    public Metas getMeta(String type, String name) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(name)) {
            return metasMapper.selectByTypeAndName(type, name);
        }
        return null;
    }

    /**
     * 保存多个项目
     *
     * @param cid   文章id
     * @param names 类型名称列表
     * @param type  类型，tag or category
     */
    @Override
    public void saveMetas(Integer cid, String names, String type) {
        if (null == cid) {
            throw new TipRestException(ExceptionType.MTEA_CID_BLANK);
        }
        if (StringUtils.isNotBlank(names) && StringUtils.isNotBlank(type)) {
            String[] nameArr = names.split(",");
            for (String name : nameArr) {
                this.saveOrUpdate(cid, name, type);
            }
        }
    }

    /**
     * 根据类型查询项目列表，带项目下面的文章数
     *
     * @param type
     * @param maxPosts
     * @return
     */
    @Override
    public List<MetaDto> getMetaWhitContentList(String type, Integer maxPosts) {
        List<MetaDto> metasList = metasMapper.selectMetaWhitContentList(type, maxPosts);
        return metasList;
    }

    /**
     * 保存或修改分类
     *
     * @param type
     * @param name
     * @param mid
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveMeta(String type, String name, Integer mid) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(name)) {
            Metas metasCond = new Metas();
            metasCond.setType(type);
            metasCond.setName(name);
            List<Metas> metaList = metasMapper.select(metasCond);
            if (metaList.size() != 0) {
                throw new TipRestException(ExceptionType.MTEA_EXIST);
            } else {
                //修改
                if (null != mid) {
                    Metas metaForU = metasMapper.selectByPrimaryKey(mid);
                    if (null == metaForU) {
                        throw new TipRestException(ExceptionType.MTEA_NOT_EXIST);
                    }
                    metasCond.setMid(mid);
                    //更新标签分类表
                    metasMapper.updateByPrimaryKeySelective(metasCond);
                    //更新原有文章的categories
                    Relationships relationshipsCond = new Relationships();
                    relationshipsCond.setMid(mid);
                    List<Relationships> rlist = relationshipsMapper.select(relationshipsCond);
                    if (rlist != null) {
                        rlist.forEach(r -> updateCategoriesOrTag(false, r.getCid(), name, type, metaForU.getName()));
                    }
                    return true;
                //保存
                } else {
                    metasMapper.insertSelective(metasCond);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 删除分类
     *
     * @param mid
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMeta(Integer mid) {
        Metas metas = metasMapper.selectByPrimaryKey(mid);
        if (null == metas) {
            throw new TipRestException(ExceptionType.MTEA_NOT_EXIST);
        }
        //记录
        String type = metas.getType();
        String name = metas.getName();
        //删除mets库
        metasMapper.deleteByPrimaryKey(mid);

        Relationships relationshipsCond = new Relationships();
        relationshipsCond.setMid(mid);
        List<Relationships> rlist = relationshipsMapper.select(relationshipsCond);
        //更新删除原有文章的categories
        if (rlist != null) {
            rlist.forEach(r -> updateCategoriesOrTag(true, r.getCid(), name, type, null));
        }
        //删除依赖表中的依赖关系
        relationshipsMapper.delete(relationshipsCond);
        return true;
    }

    /**
     * 修改文章表的标签分类信息
     *
     * @param flag true 删除，false 修改
     * @param cid
     * @param name 需要替换或者删除的标签分类
     * @param type 标签或者分类
     * @param oldName 原名字
     */
    private void updateCategoriesOrTag(boolean flag, Integer cid, String name, String type, String oldName) {
        Contents contents = contentsMapper.selectByPrimaryKey(cid);
        //更新文章表的分类
        if (null != contents) {
            Contents temp = new Contents();
            temp.setCid(cid);
            boolean isUpdate = false;
            if (type.equals(Types.CATEGORY)) {
                //删除
                if (flag) {
                    temp.setCategories(reMeta(name, contents.getCategories()));
                    //替换
                } else {
                    temp.setCategories(upMeta(name, contents.getCategories(), oldName));
                }
                isUpdate = true;
            }
            if (type.equals(Types.TAG)) {
                //删除
                if (flag) {
                    temp.setTags(reMeta(name, contents.getTags()));
                    //替换
                } else {
                    temp.setTags(upMeta(name, contents.getTags(), oldName));
                }
                isUpdate = true;
            }
            if (isUpdate) {
                contentsMapper.updateByPrimaryKeySelective(temp);
            }
        }
    }

    /**
     * 删除文章表的meta
     *
     * @param name
     * @param metas
     * @return
     */
    private String reMeta(String name, String metas) {
        String[] ms = StringUtils.split(metas, ",");
        StringBuilder sbuf = new StringBuilder();
        for (String m : ms) {
            if (!name.equals(m)) {
                sbuf.append(",").append(m);
            }
        }
        if (sbuf.length() > 0) {
            return sbuf.substring(1);
        }
        return "";
    }

    /**
     * 替换文章表的meta
     *
     * @param name
     * @param metas
     * @return
     */
    private String upMeta(String name, String metas, String oldName) {
        String[] ms = StringUtils.split(metas, ",");
        StringBuilder sbuf = new StringBuilder();
        for (String m : ms) {
            if (oldName.equals(m)) {
                m = name;
            }
            sbuf.append(",").append(m);
        }
        if (sbuf.length() > 0) {
            return sbuf.substring(1);
        }
        return "";
    }

    /**
     * 标签 依赖关系
     *
     * @param cid
     * @param name
     * @param type
     */
    private void saveOrUpdate(Integer cid, String name, String type) {
        Metas metaCondition = new Metas();
        metaCondition.setName(name);
        metaCondition.setType(type);
        List<Metas> select = metasMapper.select(metaCondition);
        //对应关系 用
        int mid;
        if (select != null && select.size() == 1) {
            mid = select.get(0).getMid();
            //存入标签
        } else if (select != null && select.size() == 0) {
            Metas metas = new Metas();
            metas.setSlug(name);
            metas.setName(name);
            metas.setType(type);
            metasMapper.insertSelective(metas);
            mid = metas.getMid();
        } else {
            return;
        }
        //依赖关系
        if (mid != 0) {
            Relationships relationshipsCond = new Relationships();
            relationshipsCond.setCid(cid);
            relationshipsCond.setMid(mid);
            int count = relationshipsMapper.selectCount(relationshipsCond);
            if (count == 0) {
                relationshipsMapper.insert(relationshipsCond);
            }

        }
    }
}

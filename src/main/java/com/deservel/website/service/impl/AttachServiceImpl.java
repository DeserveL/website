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
import com.deservel.website.common.utils.DateUtils;
import com.deservel.website.config.WebSiteTools;
import com.deservel.website.dao.AttachMapper;
import com.deservel.website.dao.LogsMapper;
import com.deservel.website.model.dto.LogActions;
import com.deservel.website.model.po.Attach;
import com.deservel.website.model.po.Logs;
import com.deservel.website.service.AttachService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import java.io.File;
import java.util.List;

/**
 * @author DeserveL
 * @date 2017/12/10 0010 下午 14:55
 * @since 1.0.0
 */
@Service
public class AttachServiceImpl implements AttachService {

    @Autowired
    AttachMapper attachMapper;
    @Autowired
    LogsMapper logsMapper;

    /**
     * 获取附件列表
     *
     * @param page
     * @param limit
     * @return
     */
    @Override
    public PageInfo<Attach> getAttachs(int page, int limit) {
        Condition condition = new Condition(Attach.class);
        condition.setOrderByClause("id desc");
        PageHelper.startPage(page, limit);
        List<Attach> attaches = attachMapper.selectByCondition(condition);
        return new PageInfo<>(attaches);
    }

    /**
     * 保存文件到数据库
     *
     * @param fname
     * @param fkey
     * @param ftype
     * @param author
     * @return
     */
    @Override
    public Attach save(String fname, String fkey, String ftype, Integer author) {
        Attach attach = new Attach();
        attach.setFname(fname);
        attach.setAuthorId(author);
        attach.setFkey(fkey);
        attach.setFtype(ftype);
        attach.setCreated(DateUtils.getCurrentUnixTime());
        attachMapper.insertSelective(attach);
        return attach;
    }

    /**
     * 删除附件
     *
     * @param id
     * @param uid
     * @param remoteIp
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Integer id, Integer uid, String remoteIp) {
        Attach attach = attachMapper.selectByPrimaryKey(id);
        if (null == attach) {
            throw new TipRestException(ExceptionType.ATTACH_NOT_EXIST);
        }
        int i = attachMapper.deleteByPrimaryKey(id);
        if (i > 0) {
            new File(WebSiteTools.getUplodFilePath() + attach.getFkey()).delete();
            Logs logs = LogActions.logInstance(LogActions.DEL_ATTACH, attach.getFkey(), uid, remoteIp);
            logsMapper.insertSelective(logs);
        }
        return false;
    }
}

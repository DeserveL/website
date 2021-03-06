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
package com.deservel.website.model.dto;

import com.deservel.website.model.po.Comments;
import com.deservel.website.model.po.Contents;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author DeserveL
 * @date 2017/12/12 19:05
 * @since 1.0.0
 */
@Data
public class IndexDto {

    private List<Contents> recentArticles;

    private List<Comments> recentComments;

    private Map<String, String> options;
}

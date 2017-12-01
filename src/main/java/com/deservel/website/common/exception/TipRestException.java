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
package com.deservel.website.common.exception;

import com.deservel.website.common.bean.ExceptionType;

/**
 * 异常类 返回页面
 *
 * @author DeserveL
 * @date 2017/12/1 17:17
 * @since 1.0.0
 */
public class TipRestException extends RuntimeException {

    private int code;

    private String message;

    public TipRestException(ExceptionType exceptionType) {
        this.code = exceptionType.getCode();
        this.message = exceptionType.getMessage();
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

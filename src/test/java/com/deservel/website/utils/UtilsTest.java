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
package com.deservel.website.utils;

import com.deservel.website.common.utils.AesUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

/**
 * @author DeserveL
 * @date 2017/12/1 14:46
 * @since 1.0.0
 */
public class UtilsTest {
    @Test
    public void aes() throws Exception {
        String lcc = AesUtils.deAes("SVHUCWHU9FT9MVbjPyhd4A==", "012345abcdef6789");
        System.out.println(lcc);
    }

    @Test
    public void md5(){
        String s = DigestUtils.md5Hex("admin" + "admin");
        System.out.println(s);
    }
}


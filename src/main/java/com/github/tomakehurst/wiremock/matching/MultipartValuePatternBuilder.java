/*
 * Copyright (C) 2017 Arjan Duijzer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.tomakehurst.wiremock.matching;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;

public class MultipartValuePatternBuilder {
    private String name = null;
    private Map<String, List<StringValuePattern>> headerPatterns = newLinkedHashMap();
    private List<ContentPattern<?>> bodyPatterns = new LinkedList<>();

    public MultipartValuePatternBuilder() {
    }

    public MultipartValuePatternBuilder(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public MultipartValuePatternBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public MultipartValuePatternBuilder withHeader(String name, StringValuePattern headerPattern) {
        List<StringValuePattern> patterns = headerPatterns.get(name);
        if (patterns == null) {
            patterns = new LinkedList<>();
        }
        patterns.add(headerPattern);
        headerPatterns.put(name, patterns);

        return this;
    }

    public MultipartValuePatternBuilder withMultipartBody(ContentPattern<?> bodyPattern) {
        bodyPatterns.add(bodyPattern);
        return this;
    }

    public MultipartValuePattern build() {
        return headerPatterns.isEmpty() && bodyPatterns.isEmpty() ? null :
                headerPatterns.isEmpty() ? new MultipartValuePattern(null, bodyPatterns) :
                        new MultipartValuePattern(headerPatterns, bodyPatterns);
    }
}

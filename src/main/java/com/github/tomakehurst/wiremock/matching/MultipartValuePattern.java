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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.google.common.base.Charsets;
import com.google.common.base.Function;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.common.Strings.stringFromBytes;
import static com.google.common.collect.FluentIterable.from;

public class MultipartValuePattern implements ValueMatcher<MultipartRequestPart> {
    final Map<String, List<StringValuePattern>> multipartHeaders;
    final List<ContentPattern<?>> bodyPatterns;

    @JsonCreator
    public MultipartValuePattern(@JsonProperty("multipartHeaders") Map<String, List<StringValuePattern>> headers,
                                 @JsonProperty("bodyPatterns") List<ContentPattern<?>> body) {
        this.multipartHeaders = headers;
        this.bodyPatterns = body;
    }

    @Override
    public MatchResult match(final MultipartRequestPart value) {
        if (multipartHeaders != null && bodyPatterns != null) {
            return MatchResult.aggregate(
                    from(multipartHeaders.entrySet()).transform(new Function<Map.Entry<String, List<StringValuePattern>>, MatchResult>() {
                        @Override
                        public MatchResult apply(Map.Entry<String, List<StringValuePattern>> input) {
                            final HttpHeader header = value.headers.getHeader(input.getKey());
                            if (header == null) {
                                return MatchResult.noMatch();
                            }
                            return MatchResult.aggregate(
                                    from(input.getValue()).transform(new Function<StringValuePattern, MatchResult>() {
                                        @Override
                                        public MatchResult apply(StringValuePattern pattern) {
                                            return (MultiValuePattern.of(pattern)).match(header);
                                        }
                                    }).toList()
                            );
                        }
                    })
                            .append(matchBodyPatterns(value))
                            .toList()

            );
        }

        return MatchResult.exactMatch();
    }

    private MatchResult matchBodyPatterns(final MultipartRequestPart value) {
        return MatchResult.aggregate(
                from(bodyPatterns).transform(new Function<ContentPattern, MatchResult>() {
                    @Override
                    public MatchResult apply(ContentPattern bodyPattern) {
                        return matchBody(value, bodyPattern);
                    }
                }).toList()
        );
    }

    private MatchResult matchBody(MultipartRequestPart value, ContentPattern<?> bodyPattern) {
        if (StringValuePattern.class.isAssignableFrom(bodyPattern.getClass())) {
            Charset charset = value.headers.getContentTypeHeader().charset();
            if (charset == null) {
                charset = Charsets.UTF_8;
            }
            return ((StringValuePattern) bodyPattern).match(stringFromBytes(value.body, charset));
        }

        return ((BinaryEqualToPattern) bodyPattern).match(value.body);
    }

    public Map<String, List<StringValuePattern>> getMultipartHeaders() {
        return multipartHeaders;
    }

    public List<ContentPattern<?>> getBodyPatterns() {
        return bodyPatterns;
    }
}

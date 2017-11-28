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

import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.google.common.base.Charsets;
import com.google.common.base.Function;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.tomakehurst.wiremock.common.Strings.stringFromBytes;
import static com.google.common.collect.FluentIterable.from;

public class MultipartRequestPart {
    final HttpHeaders headers;
    final byte[] body;

    public MultipartRequestPart(byte[] rawPart, Charset charset, String lineBreak) {
        if (charset == null) {
            charset = Charsets.UTF_8;
        }
        if (lineBreak == null) {
            lineBreak = "\r\n";
        }
        String headerPart = stringFromBytes(rawPart, charset);
        int headerBoundary = headerPart.indexOf(lineBreak + lineBreak, 0);
        List<HttpHeader> mHeaders = new LinkedList<>();
        for (String s : headerPart.substring(0, headerBoundary).split(lineBreak)) {
            String[] parts = s.split(":");
            String key = parts[0];
            List<String> values = from(parts[1].split(";")).transform(new Function<String, String>() {
                @Override
                public String apply(String input) {
                    return input.trim();
                }
            }).toList();
            mHeaders.add(new HttpHeader(key, values));
        }

        headers = new HttpHeaders(mHeaders);
        body = Arrays.copyOfRange(rawPart, headerBoundary + 2 * lineBreak.length(), rawPart.length - lineBreak.length());
    }

    public String getKey() {
        List<String> values = headers.getHeader("Content-Disposition").values();
        Pattern pattern = Pattern.compile("^name=\"(.*)\"");

        for (String value : values) {
            Matcher nameMatcher = pattern.matcher(value);
            if (nameMatcher.find()) {
                return nameMatcher.group(1);
            }
        }
        return null;
    }
}

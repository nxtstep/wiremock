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

import com.github.tomakehurst.wiremock.http.Request;
import com.google.common.base.Charsets;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MultipartRequestHelper {
    private MultipartRequestHelper() {
    }

    static Map<String, MultipartRequestPart> getMultiparts(Request request, String lineBreak) {
        String contentType = request.contentTypeHeader().toString();
        Pattern patternBoundary = Pattern.compile("^.*:?multipart/form-data; boundary=(.*)?");
        Matcher boundaryMatcher = patternBoundary.matcher(contentType);
        if (!boundaryMatcher.find()) {
            return null;
        }
        String boundary = "--" + boundaryMatcher.group(1);

        byte[] rawBody = request.getBody();
        String stringBody = request.getBodyAsString();
        Map<String, MultipartRequestPart> requestParts = new HashMap<>();

        int needle = 0;
        int last = -1;
        Charset charset = request.contentTypeHeader().charset();
        if (charset == null) {
            charset = Charsets.UTF_8;
        }
        while ((needle = stringBody.indexOf(boundary, needle)) > -1) {
            if (last > -1) {
                // Read part
                MultipartRequestPart part = new MultipartRequestPart(Arrays.copyOfRange(rawBody, last + boundary.length() + 2, needle), charset, lineBreak);
                requestParts.put(part.getKey(), part);
            }
            last = needle;
            needle += boundary.length();
        }

        return requestParts;
    }
}
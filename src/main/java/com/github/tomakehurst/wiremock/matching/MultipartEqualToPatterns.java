package com.github.tomakehurst.wiremock.matching;


import com.github.tomakehurst.wiremock.matching.ContentPattern;
import com.github.tomakehurst.wiremock.matching.MatchResult;
import com.google.common.io.BaseEncoding;

import java.util.Arrays;
import java.util.List;

public class MultipartEqualToPatterns extends ContentPattern<byte[]> {

//    public BinaryEqualToPattern(byte[] expected) {
//        super(expected);
//    }
//
//    @JsonCreator
//    public BinaryEqualToPattern(@JsonProperty("binaryEqualTo") String expected) {
//        this(BaseEncoding.base64().decode(expected));
//    }

    private final List<ContentPattern<?>> parts;

    public MultipartEqualToPatterns(List<ContentPattern<?>> parts) {
        super(new byte[10]);
        this.parts = parts;
    }

    @Override
    public MatchResult match(byte[] actual) {
        return MatchResult.of(
                Arrays.equals(actual, expectedValue)
        );
    }

    @Override
    public String getName() {
        return "multipartEqualTo";
    }

    @Override
    public String getExpected() {
        return BaseEncoding.base64().encode(expectedValue);
    }

//    public String getBinaryEqualTo() {
//        return getExpected();
//    }

    @Override
    public String toString() {
        return getName() + " " + getExpected();
    }
}

package com.github.tomakehurst.wiremock.matching;


import org.apache.commons.codec.binary.Base64;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.io.BaseEncoding;

public class PrintBodyMatcher extends ContentPattern<byte[]> {

    public PrintBodyMatcher(byte[] expected) {
        super(expected);
    }

    @JsonCreator
    public PrintBodyMatcher(@JsonProperty("printBodyMatcher") String expected) {
        this(BaseEncoding.base64().decode(expected));
    }

    @Override
    public MatchResult match(byte[] actual) {
        System.out.print("Request body:[" + Base64.encodeBase64String(actual) + "]");

        return MatchResult.exactMatch();

//        return MatchResult.of(
//                Arrays.equals(actual, expectedValue)
//        );
    }

    @Override
    @JsonIgnore
    public String getName() {
        return "printBodyMatcher";
    }

    @Override
    @JsonIgnore
    public String getExpected() {
        return BaseEncoding.base64().encode(expectedValue);
    }

    public String getBinaryEqualTo() {
        return getExpected();
    }

    @Override
    public String toString() {
        return getName() + " " + getExpected();
    }
}

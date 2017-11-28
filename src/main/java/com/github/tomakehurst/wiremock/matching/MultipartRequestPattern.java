package com.github.tomakehurst.wiremock.matching;


public class MultipartRequestPattern {//implements NamedValueMatcher<Request> {

//    private final RequestPattern requestPattern;
//    private final Map<String, List<ContentPattern<?>>> multipartMatchers;
//
//    @Override
//    public String getName() {
//        return "multipartRequestMatching";
//    }
//
//    @Override
//    public String getExpected() {
//        return toString();
//    }
//
//    @Override
//    public MatchResult match(Request request) {
//        return MatchResult.aggregateWeighted(
//                weight(requestPattern.match(request), 10.0)
//                weight(allMultipartPatternsMatch(request), 3.0)
//        );
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(requestPattern, multipartMatchers);
//    }
//
//    @Override
//    public String toString() {
//        return Json.write(this);
//    }
//
//    @SuppressWarnings("unchecked")
//    private MatchResult allMultipartPatternsMatch(final Request request) {
//        if (multipartMatchers != null && !multipartMatchers.isEmpty() && request.getBody() != null) {
//            // Parse multiparts
//            String contentType = request.contentTypeHeader().mimeTypePart();
//            Pattern patternBoundary = Pattern.compile("^multipart/form-data; boundary=(.*)?");
//            Matcher boundaryMatcher = patternBoundary.matcher(contentType);
//            if (!boundaryMatcher.find()) {
//                return MatchResult.noMatch();
//            }
//            String boundary = boundaryMatcher.group(1);
//
//            return MatchResult.aggregate(
//                    from(multipartMatchers.keySet()).transform(new Function<String, MatchResult>() {
//                        @Override
//                        public MatchResult apply(String key) {
//                            return MatchResult.aggregate(multipartMatchers.get(key)).transfrom(new Function<ContentPattern, MatchResult>() {
//                                @Override
//                                public MatchResult apply(ContentPattern input) {
//                                    if (StringValuePattern.class.isAssignableFrom(pattern.getClass())) {
//                                        return pattern.match(request.getBodyAsString());
//                                    }
//
//                                    return ((BinaryEqualToPattern) pattern).match(request.getBody());
//                                }
//                            }).toList();
//                        }
//
//
//                    }).toList()
//            );
//        }
//
//        return MatchResult.exactMatch();
//    }
//
//    private MatchResult matchBinaryBasedMultipartBodyMatch(final List<ContentPattern<?>> bodyPatterns, final byte[] part) {
//
//    }
//
//    private MatchResult matchStringBasedMultipartBodyMatch(final List<ContentPattern<?>> bodyPatterns, final String part) {
//
//    }
}
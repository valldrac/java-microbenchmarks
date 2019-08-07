package benchmark.sig4a.scrubber;

import org.openjdk.jmh.annotations.*;

import org.apache.commons.io.IOUtils;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
public class ScrubberBenchmark {

    String logMatchingLine = "2019-01-01 00:00:00.443 UTC W PushServiceSocket: Opening URL: https://textsecure-service.whispersystems.org/v1/messages/+1234567890";
    String logRandomLine = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut vitae condimentum eros. Morbi venenatis vitae ex non mattis massa nunc.";

    String shortDebugLog = getResourceFileAsString("debuglog.short");
    String longDebugLog = getResourceFileAsString("debuglog.long");

    private String getResourceFileAsString(String name) {
        try {
            return IOUtils.toString(getClass().getResourceAsStream("/" + name));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String scrubLogMatchingLine() {
        return new Scrubber().scrub(logMatchingLine);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String scrubLogMatchingLineV2() {
        return new ScrubberV2().scrub(logMatchingLine);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String scrubLogRandomLine() {
        return new Scrubber().scrub(logRandomLine);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String scrubLogRandomLineV2() {
        return new ScrubberV2().scrub(logRandomLine);
    }

    @Benchmark
    public String scrubShortDebugLog() {
        return new Scrubber().scrub(shortDebugLog);
    }

    @Benchmark
    public String scrubShortDebugLogV2() {
        return new ScrubberV2().scrub(shortDebugLog);
    }

    @Benchmark
    public String scrubLongDebugLog() {
        return new Scrubber().scrub(longDebugLog);
    }

    @Benchmark
    public String scrubLongDebugLogV2() {
        return new ScrubberV2().scrub(longDebugLog);
    }
}

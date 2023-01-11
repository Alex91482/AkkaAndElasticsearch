package benchmarks;

import org.openjdk.jmh.annotations.Benchmark;

public class BenchmarkMyTests {

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

    @Benchmark
    public void benchmark1(){
        // вписываем что будем замерять
    }
}

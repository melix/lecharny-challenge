/*
 * Copyright 2003-2015 the original author or authors.
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

package replace;

import org.openjdk.jmh.annotations.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class ReplaceAllBenchmark {

    public static final String SOURCEFILE_DIR = System.getProperty("datasetdir", "build");

    @Param({"10", "100", "1000"})
    private int size;

    String string;

    @Setup
    public void setup() throws Throwable {
        string = RandomStringGenerator.readFile(SOURCEFILE_DIR, size);
    }

    /**
     * This is our baseline
     */
    @Benchmark
    public String unfold_regexp() {
        return Replacer.unfold_regexp(string);
    }

    /**
     * Strangely, compiling the regex brings the same result
     */
    @Benchmark
    public String unfold_regexp_compiled() {
        return Replacer.unfold_regexp_compiled(string);
    }

    /**
     * Commons lang does better. Twice as fast
     */
    @Benchmark
    public String unfold_unfold_common() {
        return Replacer.unfold_common(string);
    }

    @Benchmark
    public String unfold_cedric() {
        return Replacer.unfold_cedric(string);
    }

    @Benchmark
    public String unfold_cedric_improved() {
        return Replacer.unfold_cedric_improved(string);
    }

    @Benchmark
    public String unfold_cedric_ultimate() {
        return Replacer.unfold_cedric_ultimate(string);
    }

    @Benchmark
    public String unfold_cedric_ultimate2() {
        return Replacer.unfold_cedric_ultimate2(string);
    }

    /**
     * Adding the missing else doesn't improve anything. It might even be harmful
     */
    @Benchmark
    public String unfold_cedric_ultimate2_with_else() {
        return Replacer.unfold_cedric_ultimate2_with_else(string);
    }

    /**
     * First seemed to improve a bit but now it doesn't that much
     */
    @Benchmark
    public String unfold_cedric_ultimate2_ternary() {
        return Replacer.unfold_cedric_ultimate2_ternary(string);
    }

    @Benchmark
    public String unfold_cedric_groovy() {
        return ReplaceGroovy.unfold_groovy(string);
    }

    @Benchmark
    public String unfold_cedric_groovy_bytecode() {
        return ReplaceGroovy.unfold_groovy_bytecode(string);
    }

    /**
     * Removing the ++ leave it more or less the same speed
     */
    @Benchmark
    public String unfold_henri_noplusplus() {
        return Replacer.unfold_henri_noplusplus(string);
    }

    /**
     * But separating in submethods gets almost 10% faster
     */
    @Benchmark
    public String unfold_henri_submethods() {
        return Replacer.unfold_henri_submethods(string);
    }

    /**
     * Trying to prevent the toCharArray to clone the string is in fact really slow
     */
    @Benchmark
    public String unfold_henri_newarray() {
        return Replacer.unfold_henri_newarray(string);
    }

    /**
     * Bulk array copy. Nice but twice slower
     */
    @Benchmark
    public String unfold_henri_arraycopy() {
        return Replacer.unfold_henri_arraycopy(string);
    }

    /**
     * Black magic, slower, but I'm pretty sure we can improve on that
     */
    @Benchmark
    public String unfold_henri_unsafe() {
        return Replacer.unfold_henri_unsafe(string);
    }

    @Benchmark
    public String unfold_olivier2() {
        return Replacer.unfold_olivier2(string);
    }

    @Benchmark
    public String unfold_mbo() {
        return Replacer.unfold_mbo(string);
    }

} 

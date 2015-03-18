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

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class ReplaceAllBenchmark {

    @Param({"10", "100", "1000"})
    private int size;

    String string;

    @Setup
    public void setup() throws Throwable {
        string = RandomStringGenerator.randomAlphanumericString(size / 3) + "\n\r " + RandomStringGenerator.randomAlphanumericString(size / 3) + "\n "
                + RandomStringGenerator.randomAlphanumericString(size / 3);
    }

    @Benchmark
    public String unfold_all_regexp() {
        return Replacer.unfold_regexp(string);
    }

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

    @Benchmark
    public String unfold_cedric_groovy() {
        return ReplaceGroovy.unfold_groovy(string);
    }

    @Benchmark
    public String unfold_olivier2() {
        return Replacer.unfold_olivier2(string);
    }


    public static void main(String[] args) {
        // Dummy main to check empirical correctness of an algorithm, using the regexp version as the reference
        Method[] methods = Replacer.class.getDeclaredMethods();
        List<Method> impls = new ArrayList<Method>();
        for (Method method : methods) {
            if (method.getParameterTypes().length==1 && (method.getModifiers()& Modifier.STATIC)==Modifier.STATIC) {
                impls.add(method);
            }
        }
        Set<Method> correct = new HashSet<Method>(impls);
        for (int i = 0; i < 10000; i++) {
            String str = RandomStringGenerator.randomAlphanumericString(20) + "\n\r " + RandomStringGenerator.randomAlphanumericString(30) + "\n "
                    + RandomStringGenerator.randomAlphanumericString(30);
            String orig = str.replace('\n', 'N').replace('\r', 'R').replace(' ', '_');
            String ref = Replacer.unfold_regexp(str).replace('\n', 'N').replace('\r', 'R').replace(' ', '_');
            for (Method method : methods) {
                if (correct.contains(method)) {
                    try {
                        String res = (String) method.invoke(null, str);
                        String cmp = res.replace('\n', 'N').replace('\r', 'R').replace(' ', '_');
                        boolean equals = ref.equals(cmp);
                        if (!equals) {
                            System.err.println("Reflection."+method.getName() + " is incorrect!");
                            System.err.println(orig);
                            System.err.println(ref);
                            System.err.println(cmp);
                            System.err.println("---------------------------");
                            correct.remove(method);
                        }
                    } catch (InvocationTargetException e) {
                        correct.remove(method);
                    } catch (IllegalAccessException e) {
                        correct.remove(method);
                    }
                }
            }
        }
        System.err.println("The following methods are deemed to be correct:");
        for (Method method : correct) {
            System.err.println("   Reflection."+method.getName());
        }

        System.err.println("The following methods are proved to be incorrect:");
        Set<Method> incorrect = new HashSet<Method>();
        Collections.addAll(incorrect, methods);
        incorrect.removeAll(correct);
        for (Method method : incorrect) {
            System.err.println("   Reflection."+method.getName());
        }
    }
} 

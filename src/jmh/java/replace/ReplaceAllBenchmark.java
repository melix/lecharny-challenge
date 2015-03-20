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
     * Black magic but faster
     */
    @Benchmark
    public String unfold_henri_unsafe() {
        return Replacer.unfold_henri_unsafe(string);
    }

    @Benchmark
    public String unfold_olivier2() {
        return Replacer.unfold_olivier2(string);
    }


    public static void main(String[] args) {
        // Dummy main to check empirical correctness of an algorithm, using the regexp version as the reference
        Method[] methods = Replacer.class.getMethods();
        List<Method> impls = new ArrayList<Method>();
        for (Method method : methods) {
            if (method.getName().startsWith("unfold_") && method.getParameterTypes().length==1 && Modifier.isStatic(method.getModifiers())) {
                impls.add(method);
            }
        }
        Set<Method> correct = new HashSet<Method>(impls);
        for (int i = 0; i < 10000; i++) {
            String str = RandomStringGenerator.randomAlphanumericString(20) + "\n\r " + RandomStringGenerator.randomAlphanumericString(30) + "\n "
                    + RandomStringGenerator.randomAlphanumericString(30);
            String orig = str.replace('\n', 'N').replace('\r', 'R').replace(' ', '_');
            String ref = Replacer.unfold_regexp(str).replace('\n', 'N').replace('\r', 'R').replace(' ', '_');
            for (Method method : impls) {
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
        Set<Method> incorrect = new HashSet<Method>(impls);
        incorrect.removeAll(correct);
        for (Method method : incorrect) {
            System.err.println("   Reflection."+method.getName());
        }
    }
} 

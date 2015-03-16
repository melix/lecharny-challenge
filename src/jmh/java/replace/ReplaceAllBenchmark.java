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

import org.apache.commons.lang3.StringUtils;
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
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

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

    private final static Random RANDOM = new Random();

    private static String randomAlphanumericString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            int r = RANDOM.nextInt(10);
            switch (r) {
                case 0:
                    sb.append('\r');
                    break;
                case 1:
                    sb.append('\n');
                    break;
                case 2:
                    sb.append(' ');
                    break;
                default:
                    sb.append((char) ('a' + RANDOM.nextInt(26)));
            }
        }
        return sb.toString();
    }

    @Setup
    public void setup() throws Throwable {
        string = randomAlphanumericString(size / 3) + "\n\r " + randomAlphanumericString(size / 3) + "\n "
                + randomAlphanumericString(size / 3);
    }

    static class Replacer {

        static String unfold_regexp(String s) {
            s = s.replaceAll("\n\r |\r\n |\n |\r ", "");
            return s;
        }

        static String unfold_cedric(String s) {
            if (s == null || s.length() < 2) {
                return s;
            }
            int len = s.length();
            char p1 = 'x';
            char p2 = 'x';
            char[] sb = new char[len];
            int wrtAt = 0;
            for (int i = 0; i < len; i++) {
                char c = s.charAt(i);
                if (' ' == c) {
                    if ('\n' == p1) {
                        if ('\r' == p2) {
                            wrtAt--;
                        }
                        wrtAt--;
                    } else if ('\r' == p1) {
                        if ('\n' == p2) {
                            wrtAt--;
                        }
                        wrtAt--;
                    } else {
                        sb[wrtAt++] = c;
                    }
                } else {
                    sb[wrtAt++] = c;
                }

                p2 = p1;
                p1 = c;
            }

            return new String(sb, 0, wrtAt);
        }

        static String unfold_cedric_improved(String s) {
            if (s == null || s.length() < 2) {
                return s;
            }

            int len = s.length();
            char p1 = 'x';
            char p2 = 'x';
            char[] chars = s.toCharArray();
            int wrtAt = 0;

            for (int i = 0; i < len; i++) {
                char c = chars[i];

                if (' ' == c) {
                    if ('\n' == p1) {
                        if ('\r' == p2) {
                            wrtAt--;
                        }
                        wrtAt--;
                    } else if ('\r' == p1) {
                        if ('\n' == p2) {
                            wrtAt--;
                        }
                        wrtAt--;
                    } else {
                        chars[wrtAt++] = c;
                    }
                } else {
                    chars[wrtAt++] = c;
                }

                p2 = p1;
                p1 = c;
            }

            return new String(chars, 0, wrtAt);
        }

        static String unfold_cedric_ultimate(String s) {
            if (s == null || s.length() < 2) {
                return s;
            }

            char p1 = 'x';
            char p2 = 'x';
            char[] chars = s.toCharArray();
            int wrtAt = 0;

            for (char c : chars) {
                chars[wrtAt++] = c;
                if (' ' == c) {
                    if ('\n' == p1) {
                        if ('\r' == p2) {
                            wrtAt--;
                        }
                        wrtAt--;
                        wrtAt--;
                    }
                    if ('\r' == p1) {
                        if ('\n' == p2) {
                            wrtAt--;
                        }
                        wrtAt--;
                        wrtAt--;
                    }
                }

                p2 = p1;
                p1 = c;

            }

            return new String(chars, 0, wrtAt);
        }

        static String unfold_cedric_ultimate2(String s) {
            if (s == null || s.length() < 2) {
                return s;
            }

            char p1 = 'x';
            char p2 = 'x';
            char[] chars = s.toCharArray();
            int wrtAt = 0;

            for (char c : chars) {
                chars[wrtAt++] = c;
                if (' ' == c) {
                    if ('\n' == p1) {
                        if ('\r' == p2) {
                            wrtAt -= 3;
                        } else {
                            wrtAt -= 2;
                        }
                    }
                    if ('\r' == p1) {
                        if ('\n' == p2) {
                            wrtAt-=3;
                        } else {
                            wrtAt-=2;
                        }
                    }
                }

                p2 = p1;
                p1 = c;

            }

            return new String(chars, 0, wrtAt);
        }

        private static final String[] TODO = {"\n\r ", "\r\n ", "\r ", "\n "};

        private static final String[] TO = {"", "", "", ""};

        protected static String unfold_common(final String string) {
            return StringUtils.replaceEach(string, TODO, TO);
        }

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


    public static void main(String[] args) {
        // Dummy main to check empirical correctness of an algorithm, using the regexp version as the reference
        Method[] methods = Replacer.class.getDeclaredMethods();
        List<Method> impls = new ArrayList<>();
        for (Method method : methods) {
            if (method.getParameters().length==1 && (method.getModifiers()& Modifier.STATIC)==Modifier.STATIC) {
                impls.add(method);
            }
        }
        Set<Method> correct = new HashSet<>(impls);
        for (int i = 0; i < 10000; i++) {
            String str = randomAlphanumericString(20) + "\n\r " + randomAlphanumericString(30) + "\n "
                    + randomAlphanumericString(30);
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

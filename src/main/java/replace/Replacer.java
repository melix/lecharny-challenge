/*
 * Copyright 2003-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package replace;

import org.apache.commons.lang3.StringUtils;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

public final class Replacer {

    private static final String PATTERN_STRING = "\n\r |\r\n |\n |\r ";
    private static final Pattern PATTERN = Pattern.compile(PATTERN_STRING);
    private static final boolean JAVA6;

    static {
        String version = System.getProperty("java.version");
        JAVA6 = version.startsWith("1.6");
    }
    
    private Replacer() {}

    public static String unfold_regexp(String s) {
        if (s == null) {
            return null;
        }
        return s.replaceAll(PATTERN_STRING, "");
    }


    public static String unfold_regexp_compiled(String s) {
        if (s == null) {
            return null;
        }
        return PATTERN.matcher(s).replaceAll("");
    }

    public static String unfold_cedric(String s) {
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

    public static String unfold_cedric_improved(String s) {
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

    public static String unfold_cedric_ultimate(String s) {
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

    public static String unfold_cedric_ultimate2(String s) {
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

    public static String unfold_henri_noplusplus(String s) {
        if (s == null || s.length() < 2) {
            return s;
        }

        char p1 = 'x';
        char p2 = 'x';
        char[] chars = s.toCharArray();
        int wrtAt = 0;

        for (char c : chars) {
            chars[wrtAt] = c;
            if (' ' == c) {
                if ('\n' == p1) {
                    if ('\r' == p2) {
                        wrtAt -= 2;
                    } else {
                        wrtAt -= 1;
                    }
                }
                else if ('\r' == p1) {
                    if ('\n' == p2) {
                        wrtAt -= 2;
                    } else {
                        wrtAt -= 1;
                    }
                }
                else {
                    wrtAt += 1;
                }
            }
            else {
                wrtAt += 1;
            }

            p2 = p1;
            p1 = c;
        }

        return new String(chars, 0, wrtAt);
    }

    public static String unfold_cedric_ultimate2_ternary(String s) {
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
                    wrtAt -= ('\r' == p2) ? 3 : 2;
                }
                if ('\r' == p1) {
                    wrtAt -= ('\n' == p2) ? 3 : 2;
                }
            }

            p2 = p1;
            p1 = c;

        }

        return new String(chars, 0, wrtAt);
    }

    public static String unfold_cedric_ultimate2_with_else(String s) {
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
                else if ('\r' == p1) {
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

    public static String unfold_henri_submethods(String s) {
        if (s == null || s.length() < 2) {
            return s;
        }

        char p1 = 'x';
        char p2 = 'x';
        char[] chars = s.toCharArray();
        int wrtAt = 0;

        for (char c : chars) {
            chars[wrtAt] = c;
            wrtAt += getWrtAt(p1, p2, c);
            p2 = p1;
            p1 = c;
        }

        return new String(chars, 0, wrtAt);
    }

    private static int getWrtAt(char p1, char p2, char c) {
        if (' ' == c) {
            if ('\n' == p1) {
                return getOffset(p2, '\r');
            }
            if ('\r' == p1) {
                return getOffset(p2, '\n');
            }
        }
        return 1;
    }

    private static int getOffset(char p2, char c) {
        return p2 == c ? -2 : -1;
    }

    public static String unfold_henri_newarray(String s) {
        if (s == null || s.length() < 2) {
            return s;
        }

        char p1 = 'x';
        char p2 = 'x';
        char[] chars = new char[s.length()];
        int wrtAt = 0;

        for (int i = 0; i < chars.length; i++) {
            char c = s.charAt(i);
            chars[wrtAt] = c;
            wrtAt += getWrtAt(p1, p2, c);
            p2 = p1;
            p1 = c;
        }

        return new String(chars, 0, wrtAt);
    }

    public static String unfold_henri_arraycopy(String s) {
        if (s == null || s.length() < 2) {
            return s;
        }

        char p1 = 'x';
        char p2 = 'x';
        char[] chars = s.toCharArray();
        int startAt = 0, wrtAt = 0, lastWrtAt = 0;

        int i = 0;
        for (; i < chars.length; i++) {
            char c = chars[i];
            int offset = getWrtAt(p1, p2, c);
            wrtAt += offset;
            if(offset != 1) {
                System.arraycopy(chars, startAt, chars, lastWrtAt, i + offset - startAt);
                startAt = i + 1;
                lastWrtAt = wrtAt;
            }
            p2 = p1;
            p1 = c;
        }
        if(startAt < chars.length) {
            System.arraycopy(chars, startAt, chars, lastWrtAt, i - startAt);
        }

        return new String(chars, 0, wrtAt);
    }

    private static final Unsafe UNSAFE;
    private static final long STRING_VALUE_FIELD_OFFSET;
    private static final long CHAR_ARRAY_OFFSET;

    private static Unsafe loadUnsafe() {
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            return (Unsafe) unsafeField.get(null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        UNSAFE = loadUnsafe();
    }

    private static long getFieldOffset(String fieldName) {
        try {
            return UNSAFE.objectFieldOffset(String.class.getDeclaredField(fieldName));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        STRING_VALUE_FIELD_OFFSET = getFieldOffset("value");
        CHAR_ARRAY_OFFSET = UNSAFE.arrayBaseOffset(char[].class);
    }

    private static char[] toCharArray(String string) {
        return (char[]) UNSAFE.getObject(string, STRING_VALUE_FIELD_OFFSET);
    }

    private static final Field VALUE;

    static {
        try {
            VALUE = String.class.getDeclaredField("value");
            VALUE.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static void copy(char[] src, char[] dest, int length) {
        UNSAFE.copyMemory(src, CHAR_ARRAY_OFFSET, dest, CHAR_ARRAY_OFFSET, length << 1);
    }

    public static String unfold_henri_unsafe(String s) {
        if (s == null || s.length() < 2) {
            return s;
        }

        if(JAVA6) {
            return unfold_henri_submethods(s);
        }

        char p1 = 'x';
        char p2 = 'x';

        char[] chars = new char[s.length()];
        char[] innerChars = toCharArray(s);
        copy(innerChars, chars, s.length());

        int wrtAt = 0;

        for (char c : chars) {
            chars[wrtAt] = c;
            wrtAt += getWrtAt(p1, p2, c);
            p2 = p1;
            p1 = c;
        }

        try {
            s = (String) UNSAFE.allocateInstance(String.class);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }


        innerChars = new char[wrtAt];
        copy(chars, innerChars, wrtAt);

        UNSAFE.putObject(s, STRING_VALUE_FIELD_OFFSET, innerChars);

        return s;
    }

    public static String unfold_olivier2(String test) {

        // Throws NPE if null, like the original method
        if (test.length() < 2) return test;

        char[] chars = test.toCharArray();
        int p = chars.length - 1;
        int d = p;
        char c, c1, c2;
        while (p > 0) {
            c = chars[p];
            c1 = chars[p - 1];
            if (c == ' ' && (c1 == '\n' || c1 == '\r')) {
                p--;
                if (p > 0) {
                    c2 = chars[p - 1];
                    if ((c2 == '\n' || c2 == '\r') && c2 != c1) {
                        p--;
                    }
                }
            }
            else {
                chars[d] = c;
                d--;
            }
            p--;
        }
        while (p >= 0) {
            chars[d--] = chars[p--];
        }

        return new String(chars, d + 1, chars.length - d - 1);
    }


    private static final String[] TODO = {"\n\r ", "\r\n ", "\r ", "\n "};

    private static final String[] TO = {"", "", "", ""};

    public static String unfold_common(final String string) {
        return StringUtils.replaceEach(string, TODO, TO);
    }

}

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

import java.util.regex.Pattern;

public final class Replacer {

    private Replacer() {}

    public static String unfold_regexp(String s) {
        if (s == null) {
            return null;
        }
        return s.replaceAll("\n\r |\r\n |\n |\r ", "");
    }

    private static final Pattern PATTERN = Pattern.compile("\n\r |\r\n |\n |\r ");

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

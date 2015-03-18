package replace

import groovy.transform.CompileStatic

@CompileStatic
class ReplaceGroovy {
    static String unfold_groovy(String s) {
        if (s == null || s.length() < 2) {
            return s
        }

        char p1 = 'x'
        char p2 = 'x'
        char[] chars = s.toCharArray()
        int wrtAt = 0

        for (char c : chars) {
            chars[wrtAt++] = c
            if (' ' == c) {
                if ('\n' == p1) {
                    if ('\r' == p2) {
                        wrtAt -= 3
                    } else {
                        wrtAt -= 2
                    }
                }
                if ('\r' == p1) {
                    if ('\n' == p2) {
                        wrtAt -= 3
                    } else {
                        wrtAt -= 2
                    }
                }
            }

            p2 = p1
            p1 = c

        }

        new String(chars, 0, wrtAt)
    }
}
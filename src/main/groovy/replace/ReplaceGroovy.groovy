package replace

import groovy.transform.CompileStatic
import groovyx.ast.bytecode.Bytecode

class ReplaceGroovy {
    @CompileStatic
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

    @Bytecode
    static String unfold_groovy_bytecode(String s) {
        // local variable table
        // 1 : char p1
        // 2 : char p2
        // 3 : char[] chars
        // 4 : int wrtAt
        // 5 : int idx (iteration loop)
        // 6 : int len
        // 7 : char c
        aload_0
        ifnonnull l0
        _goto l1

        l0:
        aload 0
        invokevirtual 'java/lang/String.length','()I'
        iconst_2
        if_icmpge l3

        l1:
        aload_0
        areturn

        l3:
        bipush 120
        dup
        istore 1
        istore 2
        aload 0
        invokevirtual 'java/lang/String.toCharArray','()[C'
        astore 3
        iconst_0
        iconst_0
        istore 4
        istore 5
        aload 3     // load char[]
        arraylength
        istore 6

        l4:         // for (c : chars)
        iload 5
        iload 6
        if_icmpge l7
        aload 3
        iload 5
        caload
        istore 7
        iinc 5,1

        // chars[wrtAt++] = c
        aload 3
        iload 4
        iload 7
        castore
        iinc 4,1

        bipush 32
        iload 7
        if_icmpne l6

        bipush 13
        iload 1
        if_icmpne l5
        iinc 4,-2
        bipush 10
        iload 2
        if_icmpne l6
        iinc 4,-1
        _goto l6

        l5:
        bipush 10
        iload 1
        if_icmpne l6
        iinc 4,-2
        bipush 13
        iload 2
        if_icmpne l6
        iinc 4,-1

        l6:
        iload 1
        istore 2
        iload 7
        istore 1

        _goto l4

        l7:
        _new 'java/lang/String'
        dup
        aload 3
        iconst_0
        iload 4
        invokespecial 'java/lang/String.<init>' ,'([CII)V'
        areturn
    }
}
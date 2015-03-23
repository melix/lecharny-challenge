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

import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ReplacerTest {
    @Test
    public void shouldRatherUseSpock() {
        System.err.println("Hey, what if you used Spock instead for testing?");
    }

    @Test
    public void testUnfold_mbo() {
        String content =
                "aa aa\r"
                        + "b b\n"
                        + "c c\r\n"
                        + "dd d d\n\r"
                        + "ee\n"
                        + "b b\n "
                        + "c c\n "
                        + "dd d d\n "
                        + "\n "
                        + "ee\r";

        final String ref = Replacer.unfold_common(content);
        final String res = Replacer.unfold_mbo(content);
        assertEquals(ref, res);
    }

    @Test
    public void testCopyPartialArray() {
        char[] source = "Ceci est un test".toCharArray();
        char[] dest = {'x', 'x', 'x', 'x', 'x', 'x'};
        Replacer.copy(source, 12 ,dest, 1, 4);
        System.out.println(source);
        System.out.println(dest);
        assertEquals("xtestx", new String(dest));
    }

    @Test
    public void testFindPostitionsToDelete() {
        String content =
                 //+---+ ---+-- -+---+---+---+---+---+---+
                  "test\r test\n test\n\r test\r\n test";
        BitSet ptd = Replacer.findPostitionsToDelete(content.toCharArray());
        int[][] expected = { {4, 5}, {10, 11}, {16, 18}, {23, 25} };
        int[][] res = new int[4][2];
        int idx = 0;
        int ns = ptd.nextSetBit(0);
        int nc = 0;
        while(ns >= 0 && nc >= 0 && ns != Integer.MAX_VALUE && nc != Integer.MAX_VALUE) {
            nc = ptd.nextClearBit(ns+1);
            res[idx][0] = ns;
            res[idx][1] = nc-1;
            ns = ptd.nextSetBit(nc+1);
            idx++;
        }
        assertArrayEquals(expected, res);
    }
}

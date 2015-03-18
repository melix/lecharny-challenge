package replacer

import replace.ReplaceGroovy

class GroovyReplacerTest extends GroovyTestCase {

    void testGroovy() {
        def x = 'xxx\r\n '
        assert ReplaceGroovy.unfold_groovy(x) == 'xxx'
    }
}

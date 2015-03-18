package replacer

import spock.lang.Specification
import spock.lang.Unroll

import static replace.ReplaceGroovy.unfold_groovy
import static replace.Replacer.unfold_regexp

@Unroll
class ReplacerSpecification extends Specification {

        void testReplacers() {

        expect: "Regular expression replacer replaces newlines"
        unfold_regexp(input) == output

        and: "Groovy unfold replaces newlines"
        unfold_groovy(input) == output

        where:
        input      | output
        null       | null
        ''         | ''
        'a'        | 'a'
        'xxx'      | 'xxx'
        'xxx\n '   | 'xxx'
        'xxx\r\n ' | 'xxx'
        'xxx\r\r ' | 'xxx\r'
        'xxx xxx'  | 'xxx xxx'
    }

}

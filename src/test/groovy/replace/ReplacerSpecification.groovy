package replace

import spock.lang.Specification
import spock.lang.Unroll

import java.lang.reflect.Method
import java.lang.reflect.Modifier

import static replace.RandomStringGenerator.randomAlphanumericString as rs
import static replace.ReplaceGroovy.unfold_groovy
import static replace.Replacer.unfold_regexp

@Unroll
class ReplacerSpecification extends Specification {

    private static Random RANDOM = new Random()

    private static List<Method> METHODS_TO_TEST = [Replacer, ReplaceGroovy]*.declaredMethods.flatten().findAll { Method it ->
        it.parameterTypes.length == 1 && (it.modifiers & Modifier.STATIC) == Modifier.STATIC
    }

    private static List<String> RANDOM_STRINGS = (0..1000).collect {
        (1..RANDOM.nextInt(10)).collect { rs(it) }.join('')
    }.unique().sort { it.length() }

    private static String prettify(String str) {
        str.replace('\n', 'N').replace('\r', 'R').replace(' ', '_')
    }

    void testReplacers() {

        expect: "Regular expression replacer replaces newlines"
        unfold_regexp(input) == output

        and: "Groovy unfold replaces newlines"
        unfold_groovy(input) == output

        where:
        input            | output
        null             | null
        ''               | ''
        'a'              | 'a'
        'ab'             | 'ab'
        'xxx'            | 'xxx'
        'xxx\n '         | 'xxx'
        'xxx\r\n '       | 'xxx'
        'xxx\r\r '       | 'xxx\r'
        'xxx xxx'        | 'xxx xxx'
        'xxx\n xxx\r\n ' | 'xxxxxx'
    }

    @Unroll("Method '#prettyMethod' for string '#prettyStr' has output '#prettyRef'")
    void testWithRandomStrings() {
        given:
        def output = method.invoke(null, str)

        expect:
        output == reference

        where:
        [method,str] << [METHODS_TO_TEST,RANDOM_STRINGS].combinations()
        reference = unfold_regexp(str)
        prettyStr = prettify(str)
        prettyRef = prettify(reference)
        prettyMethod = method.name
    }
}

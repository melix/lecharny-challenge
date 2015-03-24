import groovy.transform.CompilationUnitAware
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.control.CompilationUnit
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformationClass

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

// a compiler configuration that works around a Gradle issue with classloading
withConfig(configuration) {
    ast(Spoofed)
}

@Retention(RetentionPolicy.RUNTIME)
@GroovyASTTransformationClass("ClassLoaderSpoofer")
@interface Spoofed {}

@GroovyASTTransformation(phase = CompilePhase.CONVERSION)
class ClassLoaderSpoofer extends AbstractASTTransformation implements CompilationUnitAware {

    @Override
    void visit(final ASTNode[] nodes, final SourceUnit source) {
    }

    @Override
    void setCompilationUnit(final CompilationUnit unit) {
        unit.transformLoader.parent.allowPackage('groovyjarjarasm')
    }
}
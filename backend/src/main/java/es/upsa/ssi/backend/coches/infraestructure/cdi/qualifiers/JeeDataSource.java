package es.upsa.ssi.backend.coches.infraestructure.cdi.qualifiers;

import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.enterprise.util.Nonbinding;
import jakarta.inject.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Qualifier
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
public @interface JeeDataSource
{

    //La direccion de este string hay que adaptarla
    public static final String DEFAULT_VALUE = "es.upsa.ssi.coches.infraestructure.cdi.qualifiers.JeeDataSource.value.DEFAULT";

    @Nonbinding
    public String value() default DEFAULT_VALUE;

    public static class Literal extends AnnotationLiteral<JeeDataSource> implements JeeDataSource
    {
        public static final JeeDataSource DEFAULT = of(DEFAULT_VALUE);
        public static JeeDataSource of(String value)
        {
            return new Literal(value);
        }

        private String value;

        private Literal(String value) {
            this.value = value;
        }

        @Override
        public String value() {
            return value;
        }
    }
}

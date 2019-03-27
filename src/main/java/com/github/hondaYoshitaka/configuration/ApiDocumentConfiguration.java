package com.github.hondaYoshitaka.configuration;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import springfox.documentation.builders.AlternateTypeBuilder;
import springfox.documentation.builders.AlternateTypePropertyBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

@Configuration
@EnableSwagger2
public class ApiDocumentConfiguration {

    @Bean
    public Docket swaggerSpringMvcPlugin(final TypeResolver typeResolver) {
        return new Docket(DocumentationType.SWAGGER_2)
                .alternateTypeRules(newRule(typeResolver.resolve(Pageable.class),
                                            pageableMixin(),
                                            Ordered.HIGHEST_PRECEDENCE))
                .groupName("xxx-api")
                .select()
                .paths(paths())
                .build()
                .apiInfo(apiInfo());
    }

    private Predicate<String> paths() {
        return Predicates.or(Predicates.containsPattern("/api/*"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "xxx API",
                "",
                "",
                "",
                new Contact("", "", ""),
                "",
                "",
                new ArrayList<>()
        );
    }

    private Type pageableMixin() {
        return new AlternateTypeBuilder()
                .fullyQualifiedClassName(
                        String.format("%s.generated.%s",
                                      Pageable.class.getPackage().getName(),
                                      Pageable.class.getSimpleName()))
                .withProperties(Arrays.asList(
                        property(Integer.class, "size"),
                        property(Integer.class, "limit"),
                        property(String.class, "sort")
                ))
                .build();
    }

    private AlternateTypePropertyBuilder property(Class<?> type, String name) {
        return new AlternateTypePropertyBuilder()
                .withName(name)
                .withType(type)
                .withCanRead(true)
                .withCanWrite(true);
    }
}

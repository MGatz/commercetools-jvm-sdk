package introspection;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static introspection.IntrospectionUtils.readClassNames;
import static java.util.stream.Collectors.toList;

//test:runMain introspection.OptionalInspection
public class OptionalInspection {
    static final Predicate<Class<?>> isOptionalClass = type -> type.getName().equals("java.util.Optional");
    public static final Predicate<MethodInfo> METHOD_CONTAINS_OPTIONAL_STUFF = m -> m.containsOptionalParameter() || m.containsOptionalReturnType();


    public static void main(String[] args) throws Exception {
        final Stream<String> classNameStream = readClassNames();
        final List<ClassInfo> classesWithAnyOptional = classNameStream.map(className -> {
            try {
                final Class<?> clazz = Class.forName(className);
                return new ClassInfo(clazz);
            } catch (ClassNotFoundException e) {
                return null;
            }
        })
        .filter(i -> i.getMethods().stream().anyMatch(m -> m.containsOptionalParameter() || m.containsOptionalReturnType()) || i.containsOptionalField())
        .collect(toList());

        classesWithAnyOptional
//TODO, search for optional fields

                .forEach(clazzInfo -> {
                    System.err.println(clazzInfo);
                    final List<MethodInfo> methodWithOptionalParameters = clazzInfo.getMethodInfoStream().filter(m -> m.containsOptionalParameter()).collect(toList());
                    if (!methodWithOptionalParameters.isEmpty()) {
                        System.err.println("methods with Optional parameters:");
                        methodWithOptionalParameters.forEach(m -> System.err.println(m));
                        System.err.println("");
                    }

                    final List<MethodInfo> methodsWithOptionalReturnType = clazzInfo.getMethodInfoStream().filter(m -> m.containsOptionalReturnType()).collect(toList());
                    if (!methodsWithOptionalReturnType.isEmpty()) {
                        System.err.println("methods with Optional return type:");
                        methodsWithOptionalReturnType.forEach(m -> System.err.println(m));
                        System.err.println("");
                    }

                    final List<Field> fields = clazzInfo.fieldsWithOptionalTypeStream().collect(toList());
                    if (!fields.isEmpty()) {
                        System.err.println("fields of Optional type:");
                        fields.forEach(field -> System.err.println(field.getName()));
                        System.err.println("");
                    }
                    System.err.println("-----------------------------------------------------------------------------------------");
                });

        System.err.println("classes affected: " + classesWithAnyOptional.size());
        System.err.println("methods affected: " + classesWithAnyOptional.stream().mapToLong(c -> {
            final long count = c.getMethodInfoStream().filter(METHOD_CONTAINS_OPTIONAL_STUFF).count();
            return count;
        }).sum());
    }


}

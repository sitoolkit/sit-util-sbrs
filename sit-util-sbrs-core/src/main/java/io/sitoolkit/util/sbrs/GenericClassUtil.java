package io.sitoolkit.util.sbrs;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.ResolvableType;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenericClassUtil {

  public static List<Class<?>> getAllGenericClassesFromImpl(
      Class<?> parentClass, Class<?> interfaceClass) {
    ResolvableType parentResolvableType =
        (Objects.isNull(interfaceClass))
            ? ResolvableType.forClass(parentClass)
            : ResolvableType.forClass(parentClass).as(interfaceClass);

    return Stream.of(parentResolvableType.getGenerics())
        .map(ResolvableType::resolve)
        .collect(Collectors.toList());
  }

  public static Class<?> getGenericClassFromImpl(
      Class<?> parentClass, Class<?> interfaceClass, int num) {
    return getAllGenericClassesFromImpl(parentClass, interfaceClass).get(num);
  }

  public static List<Class<?>> getAllGenericClasses(Class<?> parentClass) {
    return getAllGenericClassesFromImpl(parentClass, null);
  }

  public static Class<?> getGenericClass(Class<?> parentClass, int num) {
    return getAllGenericClasses(parentClass).get(num);
  }
}

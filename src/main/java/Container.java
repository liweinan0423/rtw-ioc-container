import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;

public class Container {

  private Set<Object> beans = new HashSet<>();

  public <T> void register(T instanceOrType) {
    if (isAnInstance(instanceOrType)) {
      registerInstance(instanceOrType);
    } else if (isAType(instanceOrType)) {
      registerByType((Class) instanceOrType);
    }
  }

  private <T> boolean isAType(T instanceOrType) {
    return instanceOrType instanceof Class;
  }

  private <T> void registerByType(Class type) {
    try {
      final Object instance;
      Constructor constructor = findConstructorWithLongestParamList(type);
      Parameter[] parameters = constructor.getParameters();
      Object[] paramValues = new Object[parameters.length];
      for (int i = 0, parametersLength = parameters.length; i < parametersLength; i++) {
        Parameter parameter = parameters[i];
        Class<?> paramType = parameter.getType();
        Object paramValue = this.get(paramType);
        paramValues[i] = paramValue;
      }

      instance = constructor.newInstance(paramValues);
      registerInstance(instance);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private <T> boolean registerInstance(T instance) {
    return beans.add(instance);
  }

  private <T> boolean isAnInstance(T instanceOrType) {
    return !(instanceOrType instanceof Class);
  }

  private Constructor findConstructorWithLongestParamList(Class type) {
    return Arrays.stream(type.getConstructors()).max(Comparator.comparingInt(Constructor::getParameterCount)).get();
  }

  public <T> T get(Class<T> type) {
    return (T) beans.stream().filter(bean -> bean.getClass() == type).findFirst().orElse(null);
  }
}

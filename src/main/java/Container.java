import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;

@SuppressWarnings("rawtypes")
public class Container {

  private Map<Class, Object> beanRegistry = new HashMap<>();

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

  private <T> boolean isAnInstance(T instanceOrType) {
    return !(instanceOrType instanceof Class);
  }

  private void registerInstance(Object instance) {
    beanRegistry.put(instance.getClass(), instance);
  }

  private void registerByType(Class type) {
    try {
      Constructor constructor = findConstructorWithLongestParamList(type);
      Parameter[] parameters = constructor.getParameters();
      Object[] paramValues = new Object[parameters.length];
      for (int i = 0, parametersLength = parameters.length; i < parametersLength; i++) {
        Parameter parameter = parameters[i];
        Class<?> paramType = parameter.getType();
        Object paramValue = this.get(paramType);
        paramValues[i] = paramValue;
      }

      registerInstance(constructor.newInstance(paramValues));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private Constructor findConstructorWithLongestParamList(Class type) {
    return Arrays.stream(type.getConstructors()).max(Comparator.comparingInt(Constructor::getParameterCount)).orElseThrow(RuntimeException::new);
  }

  public <T> T get(Class<T> type) {
    return (T) beanRegistry.get(type);
  }
}

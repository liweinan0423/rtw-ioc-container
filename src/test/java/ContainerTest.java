import fixtures.CompositeTypeWithSingleCtor;
import fixtures.CompositeTypeWithMultipleCtors;
import fixtures.Klass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertThat;

public class ContainerTest {

  @Test
  public void should_be_able_to_register_a_string() {
    Container container = new Container();

    container.register("hello");

    String str = container.get(String.class);

    assertThat(str, is("hello"));
  }

  @Test
  public void should_be_able_to_register_an_integer() {
    Container container = new Container();

    container.register(1);

    Integer bean = container.get(Integer.class);

    assertThat(bean, is(1));
  }

  @Test
  public void should_be_able_to_register_a_bean_by_type() {
    Container container = new Container();
    container.register(Klass.class);
    assertThat(container.get(Klass.class), isA(Klass.class));
  }

  @Test
  public void should_be_able_to_register_a_composite_type_with_dependencies_by_constructor() {
    Container container = new Container();
    container.register("foo");
    container.register(CompositeTypeWithSingleCtor.class);
    CompositeTypeWithSingleCtor instance = container.get(CompositeTypeWithSingleCtor.class);
    assertThat(instance, isA(CompositeTypeWithSingleCtor.class));
    assertThat(instance.name, is("foo"));
  }

  @Test
  public void should_be_able_to_inject_dependencies_from_constructor_with_longest_param_list() {
    Container container = new Container();
    container.register("foo");
    container.register(CompositeTypeWithMultipleCtors.class);
    CompositeTypeWithMultipleCtors instance = container.get(CompositeTypeWithMultipleCtors.class);
    assertThat(instance, isA(CompositeTypeWithMultipleCtors.class));
    assertThat(instance.name, is("foo"));
  }
}

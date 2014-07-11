package al.jdi.dao.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Collection;

public class CollectionTest {
  private final Collection<?> collection;

  public CollectionTest(Collection<?> collection) {
    this.collection = collection;
  }

  public void assertNotNullAndEmpty() {
    assertThat(collection, is(not(nullValue(Collection.class))));
    assertThat(collection.isEmpty(), is(true));
  }
}

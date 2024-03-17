package com.exatask.platform.utilities.unit;

import com.exatask.platform.utilities.CollectionUtility;
import org.instancio.Gen;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionUtilityTest {

  @Test
  public void shouldReturnCollection_nullSafe() {

    Integer size = Gen.ints().range(10, 25).get();

    Assertions.assertAll(
        () -> Assertions.assertEquals(0, CollectionUtility.nullSafe((Set) null).size()),
        () -> Assertions.assertEquals(0, CollectionUtility.nullSafe((List) null).size()),
        () -> Assertions.assertEquals(0, CollectionUtility.nullSafe((Map) null).size()),
        () -> Assertions.assertEquals(size, CollectionUtility.nullSafe(Instancio.ofSet(Integer.class).size(size).create()).size()),
        () -> Assertions.assertEquals(size, CollectionUtility.nullSafe(Instancio.ofList(Integer.class).size(size).create()).size()),
        () -> Assertions.assertEquals(size, CollectionUtility.nullSafe(Instancio.ofMap(Integer.class, Integer.class).size(size).create()).entrySet().size())
    );
  }

  @Test
  public void shouldReturnCollection_defaultIfNull() {

    Set<Integer> defaultSet = Instancio.ofSet(Integer.class).size(1).create();
    List<Integer> defaultList = Instancio.ofList(Integer.class).size(1).create();
    Map<Integer, Integer> defaultMap = Instancio.ofMap(Integer.class, Integer.class).size(1).create();

    Integer size = Gen.ints().range(10, 25).get();
    Set<Integer> set = Instancio.ofSet(Integer.class).size(size).create();
    List<Integer> list = Instancio.ofList(Integer.class).size(size).create();
    Map<Integer, Integer> map = Instancio.ofMap(Integer.class, Integer.class).size(size).create();

    Assertions.assertAll(
        () -> Assertions.assertEquals(null, CollectionUtility.defaultIfNull(null, (Set) null)),
        () -> Assertions.assertEquals(null, CollectionUtility.defaultIfNull(null, (List) null)),
        () -> Assertions.assertEquals(null, CollectionUtility.defaultIfNull(null, (Map) null)),
        () -> Assertions.assertEquals(defaultSet, CollectionUtility.defaultIfNull(null, defaultSet)),
        () -> Assertions.assertEquals(defaultList, CollectionUtility.defaultIfNull(null, defaultList)),
        () -> Assertions.assertEquals(defaultMap, CollectionUtility.defaultIfNull(null, defaultMap)),
        () -> Assertions.assertEquals(set, CollectionUtility.defaultIfNull(set, defaultSet)),
        () -> Assertions.assertEquals(list, CollectionUtility.defaultIfNull(list, defaultList)),
        () -> Assertions.assertEquals(map, CollectionUtility.defaultIfNull(map, defaultMap))
    );
  }

  @Test
  public void shouldReturnCollection_defaultIfEmpty() {

    Set<Integer> defaultSet = Instancio.ofSet(Integer.class).size(1).create();
    List<Integer> defaultList = Instancio.ofList(Integer.class).size(1).create();
    Map<Integer, Integer> defaultMap = Instancio.ofMap(Integer.class, Integer.class).size(1).create();

    Integer size = Gen.ints().range(10, 25).get();
    Set<Integer> set = Instancio.ofSet(Integer.class).size(size).create();
    List<Integer> list = Instancio.ofList(Integer.class).size(size).create();
    Map<Integer, Integer> map = Instancio.ofMap(Integer.class, Integer.class).size(size).create();

    Assertions.assertAll(
        () -> Assertions.assertEquals(null, CollectionUtility.defaultIfEmpty(null, (Set) null)),
        () -> Assertions.assertEquals(null, CollectionUtility.defaultIfEmpty(null, (List) null)),
        () -> Assertions.assertEquals(null, CollectionUtility.defaultIfEmpty(null, (Map) null)),
        () -> Assertions.assertEquals(defaultSet, CollectionUtility.defaultIfEmpty(null, defaultSet)),
        () -> Assertions.assertEquals(defaultList, CollectionUtility.defaultIfEmpty(null, defaultList)),
        () -> Assertions.assertEquals(defaultMap, CollectionUtility.defaultIfEmpty(null, defaultMap)),
        () -> Assertions.assertEquals(null, CollectionUtility.defaultIfEmpty(Collections.emptySet(), null)),
        () -> Assertions.assertEquals(null, CollectionUtility.defaultIfEmpty(Collections.emptyList(), null)),
        () -> Assertions.assertEquals(null, CollectionUtility.defaultIfEmpty(Collections.emptyMap(), null)),
        () -> Assertions.assertEquals(defaultSet, CollectionUtility.defaultIfEmpty(Collections.emptySet(), defaultSet)),
        () -> Assertions.assertEquals(defaultList, CollectionUtility.defaultIfEmpty(Collections.emptyList(), defaultList)),
        () -> Assertions.assertEquals(defaultMap, CollectionUtility.defaultIfEmpty(Collections.emptyMap(), defaultMap)),
        () -> Assertions.assertEquals(set, CollectionUtility.defaultIfEmpty(set, defaultSet)),
        () -> Assertions.assertEquals(list, CollectionUtility.defaultIfEmpty(list, defaultList)),
        () -> Assertions.assertEquals(map, CollectionUtility.defaultIfEmpty(map, defaultMap))
    );
  }
}

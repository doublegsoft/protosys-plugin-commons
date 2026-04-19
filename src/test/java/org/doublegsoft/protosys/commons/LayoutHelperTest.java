package org.doublegsoft.protosys.commons;

import com.doublegsoft.jcommons.lang.HashObject;
import com.doublegsoft.jcommons.metaui.layout.Cell;
import com.doublegsoft.jcommons.metaui.layout.Grid;
import com.doublegsoft.jcommons.metaui.layout.Position;
import com.doublegsoft.jcommons.metaui.layout.Row;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO: ADD DESCRIPTION
 *
 * @author <a href="mailto:guo.guo.gan@gmail.com">Christian Gann</a>
 * @since 1.0
 */
public class LayoutHelperTest {

  /**
   * <pre>
   * ---------- ----------
   * |        | |   1,2  |
   * |        | ----------
   * | 1-2,1  | ----------
   * |        | |   2,2  |
   * ---------- ----------
   * </pre>
   */
  @Ignore
  public void test1() {
    System.out.println("##################### test1 ######################");
    HashObject w_1$2_1 = new HashObject();
    HashObject w_1_2 = new HashObject();
    HashObject w_2_2 = new HashObject();

    w_1$2_1.set("id", "w_1$2_1");
    w_1$2_1.set("pos", Position.at("(1*2, 1, 100%, 100%)"));

    w_1_2.set("id", "w_1_2");
    w_1_2.set("pos", Position.at("(1, 2, 100%, 100%)"));

    w_2_2.set("id", "w_2_2");
    w_2_2.set("pos", Position.at("(2, 2, 100%, 100%)"));

    List<HashObject> widgets = new ArrayList<>();
    widgets.add(w_1$2_1);
    widgets.add(w_1_2);
    widgets.add(w_2_2);

    LayoutHelper layout = new LayoutHelper();
    Grid<HashObject> grid = layout.layout(widgets);

    Assert.assertEquals(1, grid.getRows().size());
    Assert.assertEquals(2, grid.getLastRow().getCells().size());

    Assert.assertEquals(2, grid.getLastRow().getCells().get(1).getRows().size());

    for (Object row : grid.getLastRow().getCells().get(1).getRows()) {
      ((Row) row).getCells().forEach(cell -> {
        System.out.println(((Cell) cell).getValue());
      });
    }
  }

  /**
   * <pre>
   * ---------- ---------------
   * |   1,1  | |             |
   * ---------- |             |
   * ---------- |             |
   * |   2,1  | |    1-3,2    |
   * ---------- |             |
   * ---------- |             |
   * |   3,1  | |             |
   * ---------- ---------------
   * </pre>
   */
  @Ignore
  public void test2() {
    System.out.println("##################### test2 ######################");
    HashObject w_1_1 = new HashObject();
    HashObject w_2_1 = new HashObject();
    HashObject w_3_1 = new HashObject();
    HashObject w_1$3_2 = new HashObject();

    w_1_1.set("id", "w_1_1");
    w_1_1.set("pos", Position.at("(1, 1, 100%, 100%)"));

    w_2_1.set("id", "w_2_1");
    w_2_1.set("pos", Position.at("(2, 1, 100%, 100%)"));

    w_3_1.set("id", "w_3_1");
    w_3_1.set("pos", Position.at("(3, 1, 100%, 100%)"));

    w_1$3_2.set("id", "w_1$3_2");
    w_1$3_2.set("pos", Position.at("(1*3, 2, 100%, 100%)"));

    List<HashObject> widgets = new ArrayList<>();
    widgets.add(w_1_1);
    widgets.add(w_2_1);
    widgets.add(w_3_1);
    widgets.add(w_1$3_2);

    LayoutHelper layout = new LayoutHelper();
    Grid<HashObject> grid = layout.layout(widgets);

    Assert.assertEquals(1, grid.getRows().size());
    Assert.assertEquals(2, grid.getLastRow().getCells().size());
    Assert.assertEquals(3, grid.getLastRow().getCells().get(0).getRows().size());
  }

  /**
   * <pre>
   * --------------------------
   * |         1,1            |
   * --------------------------
   * ---------- ---------------
   * |   2,1  | |             |
   * ---------- |     2-2,2   |
   * ---------- |             |
   * |   3,1  | |             |
   * ---------- ---------------
   * </pre>
   */
  @Ignore
  public void test3() {
    System.out.println("##################### test3 ######################");
    HashObject w_1_1 = new HashObject();
    HashObject w_2_1 = new HashObject();
    HashObject w_3_1 = new HashObject();
    HashObject w_2$2_2 = new HashObject();

    w_2$2_2.set("id", "w_2$2_2");
    w_2$2_2.set("pos", Position.at("(2*2, 2, 100%, 100%)"));

    w_1_1.set("id", "w_1_1");
    w_1_1.set("pos", Position.at("(1, 1, 100%, 100%)"));

    w_2_1.set("id", "w_2_1");
    w_2_1.set("pos", Position.at("(2, 1, 100%, 100%)"));

    w_3_1.set("id", "w_3_1");
    w_3_1.set("pos", Position.at("(3, 1, 100%, 100%)"));


    List<HashObject> widgets = new ArrayList<>();
    widgets.add(w_1_1);
    widgets.add(w_2_1);
    widgets.add(w_3_1);
    widgets.add(w_2$2_2);

    LayoutHelper layout = new LayoutHelper();
    Grid<HashObject> grid = layout.layout(widgets);

    Assert.assertEquals(2, grid.getRows().size());
    Assert.assertEquals(2, grid.getLastRow().getCells().size());
    Assert.assertEquals(2, grid.getLastRow().getCells().get(0).getRows().size());

    Row<HashObject> row = grid.getLastRow();
    Cell<HashObject> cell = row.getCells().get(1);
    // Assert.assertEquals("w_2$2_2", cell.getValue().get("id"));
  }

}

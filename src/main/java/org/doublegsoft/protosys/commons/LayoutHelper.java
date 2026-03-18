package org.doublegsoft.protosys.commons;

import com.doublegsoft.jcommons.lang.HashObject;
import com.doublegsoft.jcommons.metaui.layout.Cell;
import com.doublegsoft.jcommons.metaui.layout.Grid;
import com.doublegsoft.jcommons.metaui.layout.Position;
import com.doublegsoft.jcommons.metaui.layout.Row;

import java.util.*;

/**
 * TODO: ADD DESCRIPTION
 *
 * @author <a href="mailto:guo.guo.gan@gmail.com">Christian Gann</a>
 * @since 1.0
 */
public class LayoutHelper {

  public Grid<HashObject> layout(List<HashObject> widgets) {
    Grid<HashObject> retVal = new Grid<>();
    Set<Object> processed = new HashSet<>();
    countRows(retVal, widgets, processed);
    return retVal;
  }

  private void countRows(Grid<HashObject> container, List<HashObject> widgets, Set<Object> processed) {
    for (HashObject widget : widgets) {
      // easy way to process
      if (processed.contains(widget)) {
        continue;
      }
      List<HashObject> widgetsInRow = widgetsInRow(widget, widgets, processed);
      Set<Object> processedInRow = new HashSet<>();
      Row<HashObject> row = new Row<>();
      countColsInRow(row, widgetsInRow, processedInRow);
      container.addRow(row);
    }
  }

  private List<HashObject> widgetsInRow(HashObject reference, List<HashObject> widgets, Set<Object> processed) {
    Position refPos = reference.get("pos");
    String refId = reference.get("id");
    List<HashObject> retVal = new ArrayList<>();
    retVal.add(reference);
    processed.add(reference);
    int maxRowSpan = refPos.getRowSpan();
    for (HashObject widget : widgets) {
      String widgetId = widget.get("id");
      if (processed.contains(widget) || refId.equals(widgetId)) {
        continue;
      }
      Position widgetPos = widget.get("pos");
      if (widgetPos.getRowIndex() == refPos.getRowIndex()) {
        maxRowSpan = Math.max(widgetPos.getRowSpan(), refPos.getRowSpan());
      }
    }
    for (HashObject widget : widgets) {
      String widgetId = widget.get("id");
      if (processed.contains(widget) || refId.equals(widgetId)) {
        continue;
      }
      Position widgetPos = widget.get("pos");
      if (widgetPos.getRowIndex() == refPos.getRowIndex() ||
          (widgetPos.getRowIndex() > refPos.getRowIndex() &&
              widgetPos.getRowIndex() <= (refPos.getRowIndex() + maxRowSpan - 1))) {
        retVal.add(widget);
        processed.add(widget);
      }
    }
    return retVal;
  }

  private void countColsInRow(Row<HashObject> row, List<HashObject> widgetsInRow, Set<Object> processedInRow) {
    for (HashObject widget : widgetsInRow) {
      // easy way to process
      if (processedInRow.contains(widget)) {
        continue;
      }
      List<HashObject> widgetsInCol = widgetsInCol(widget, widgetsInRow, processedInRow);
      Cell<HashObject> col = new Cell<>();
      if (widgetsInCol.size() == 1) {
        col.addValue(widgetsInCol.get(0));
      } else {
        Set<Object> processedInCol = new HashSet<>();
        countRows(col, widgetsInCol, processedInCol);
      }
      row.addCell(col);
    }
  }

  private List<HashObject> widgetsInCol(HashObject reference, List<HashObject> widgetsInRow, Set<Object> processedInRow) {
    Position refPos = reference.get("pos");
    String refId = reference.get("id");
    List<HashObject> retVal = new ArrayList<>();
    retVal.add(reference);
    processedInRow.add(reference);

    for (HashObject widget : widgetsInRow) {
      String widgetId = widget.get("id");
      if (processedInRow.contains(widget) || refId.equals(widgetId)) {
        continue;
      }
      Position widgetPos = widget.get("pos");
      if (widgetPos.getCellIndex() == refPos.getCellIndex()) {
        retVal.add(widget);
        processedInRow.add(widget);
      }
    }
    return retVal;
  }

}

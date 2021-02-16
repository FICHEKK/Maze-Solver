package views;

import models.Cell;
import search.BreadthFirstSearch;
import search.DepthFirstSearch;
import search.SearchAlgorithm;

import javax.swing.*;
import java.util.List;

public class SearchAlgorithmPickerView extends JComboBox<SearchAlgorithm<Cell>> {
    private static final List<SearchAlgorithm<Cell>> SEARCH_ALGORITHMS = List.of(
            new DepthFirstSearch<>(),
            new BreadthFirstSearch<>()
    );

    public SearchAlgorithmPickerView() {
        for (var algorithm : SEARCH_ALGORITHMS) {
            addItem(algorithm);
        }
    }

    public SearchAlgorithm<Cell> getSearchAlgorithm() {
        //noinspection unchecked
        return (SearchAlgorithm<Cell>) getSelectedItem();
    }
}

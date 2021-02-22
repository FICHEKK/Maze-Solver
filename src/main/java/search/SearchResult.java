package search;

import java.util.Locale;
import java.util.Map;

public class SearchResult<S> {
    private final SearchNode<S> pathHead;
    private final Map<S, SearchNode<S>> visited;

    public SearchResult(SearchNode<S> pathHead, Map<S, SearchNode<S>> visited) {
        this.pathHead = pathHead;
        this.visited = visited;
    }

    public SearchNode<S> getPathHead() {
        return pathHead;
    }

    public Map<S, SearchNode<S>> getVisited() {
        return visited;
    }

    @Override
    public String toString() {
        final var pathCost = pathHead != null ? "Cost: " + String.format(Locale.ROOT, "%.2f", pathHead.getCost()) : "No path found.";
        return pathCost + " | Visited: " + visited.size();
    }
}

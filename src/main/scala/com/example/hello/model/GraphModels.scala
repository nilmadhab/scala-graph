package com.example.hello.model

import io.swagger.v3.oas.annotations.media.Schema
import scala.beans.BeanProperty

@Schema(description = "Request to create a graph")
class CreateGraphRequest {
  @BeanProperty
  @Schema(description = "List of nodes in the graph", example = "[\"A\", \"B\", \"C\", \"D\"]")
  var nodes: java.util.List[String] = _

  @BeanProperty
  @Schema(description = "List of edges as [source, target] pairs", example = "[[\"A\", \"B\"], [\"B\", \"C\"], [\"C\", \"D\"], [\"A\", \"D\"]]")
  var edges: java.util.List[java.util.List[String]] = _
}

@Schema(description = "Graph information response")
class GraphInfoResponse {
  @BeanProperty
  @Schema(description = "Graph ID", example = "graph-1")
  var graphId: String = _

  @BeanProperty
  @Schema(description = "Number of nodes", example = "4")
  var nodeCount: Int = _

  @BeanProperty
  @Schema(description = "Number of edges", example = "4")
  var edgeCount: Int = _

  @BeanProperty
  @Schema(description = "List of nodes")
  var nodes: java.util.List[String] = _

  @BeanProperty
  @Schema(description = "List of edges")
  var edges: java.util.List[String] = _

  @BeanProperty
  @Schema(description = "Whether the graph is connected", example = "true")
  var isConnected: Boolean = _

  @BeanProperty
  @Schema(description = "Whether the graph is acyclic", example = "false")
  var isAcyclic: Boolean = _
}

object GraphInfoResponse {
  def apply(graphId: String, nodeCount: Int, edgeCount: Int, nodes: java.util.List[String],
            edges: java.util.List[String], isConnected: Boolean, isAcyclic: Boolean): GraphInfoResponse = {
    val response = new GraphInfoResponse
    response.graphId = graphId
    response.nodeCount = nodeCount
    response.edgeCount = edgeCount
    response.nodes = nodes
    response.edges = edges
    response.isConnected = isConnected
    response.isAcyclic = isAcyclic
    response
  }
}

@Schema(description = "Shortest path response")
class ShortestPathResponse {
  @BeanProperty
  @Schema(description = "Source node", example = "A")
  var source: String = _

  @BeanProperty
  @Schema(description = "Target node", example = "D")
  var target: String = _

  @BeanProperty
  @Schema(description = "Path from source to target")
  var path: java.util.List[String] = _

  @BeanProperty
  @Schema(description = "Path length", example = "2")
  var length: Int = _

  @BeanProperty
  @Schema(description = "Whether a path exists", example = "true")
  var pathExists: Boolean = _
}

object ShortestPathResponse {
  def apply(source: String, target: String, path: java.util.List[String], length: Int, pathExists: Boolean): ShortestPathResponse = {
    val response = new ShortestPathResponse
    response.source = source
    response.target = target
    response.path = path
    response.length = length
    response.pathExists = pathExists
    response
  }
}

@Schema(description = "Neighbors response")
class NeighborsResponse {
  @BeanProperty
  @Schema(description = "The node", example = "A")
  var node: String = _

  @BeanProperty
  @Schema(description = "Direct neighbors of the node")
  var neighbors: java.util.List[String] = _

  @BeanProperty
  @Schema(description = "Number of neighbors", example = "2")
  var degree: Int = _
}

object NeighborsResponse {
  def apply(node: String, neighbors: java.util.List[String], degree: Int): NeighborsResponse = {
    val response = new NeighborsResponse
    response.node = node
    response.neighbors = neighbors
    response.degree = degree
    response
  }
}

@Schema(description = "Traversal response")
class TraversalResponse {
  @BeanProperty
  @Schema(description = "Starting node", example = "A")
  var startNode: String = _

  @BeanProperty
  @Schema(description = "Traversal type (BFS or DFS)", example = "BFS")
  var traversalType: String = _

  @BeanProperty
  @Schema(description = "Nodes in traversal order")
  var traversalOrder: java.util.List[String] = _
}

object TraversalResponse {
  def apply(startNode: String, traversalType: String, traversalOrder: java.util.List[String]): TraversalResponse = {
    val response = new TraversalResponse
    response.startNode = startNode
    response.traversalType = traversalType
    response.traversalOrder = traversalOrder
    response
  }
}

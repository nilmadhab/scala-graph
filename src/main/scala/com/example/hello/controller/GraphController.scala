package com.example.hello.controller

import com.example.hello.model._
import com.example.hello.service.GraphService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.{HttpStatus, MediaType, ResponseEntity}
import org.springframework.web.bind.annotation._

import scala.jdk.CollectionConverters._

@RestController
@RequestMapping(Array("/api/graphs"))
@Tag(name = "Graph API", description = "REST API for graph operations using scala-graph library")
class GraphController @Autowired()(graphService: GraphService) {

  @PostMapping(consumes = Array(MediaType.APPLICATION_JSON_VALUE),
               produces = Array(MediaType.APPLICATION_JSON_VALUE))
  @Operation(
    summary = "Create a new graph",
    description = "Creates a new undirected graph with the specified nodes and edges"
  )
  @ApiResponses(Array(
    new ApiResponse(responseCode = "201", description = "Graph created successfully"),
    new ApiResponse(responseCode = "400", description = "Invalid request")
  ))
  def createGraph(@RequestBody request: CreateGraphRequest): ResponseEntity[GraphInfoResponse] = {
    val nodes = request.nodes.asScala.toList
    val edges = request.edges.asScala.map { edge =>
      val edgeList = edge.asScala.toList
      (edgeList.head, edgeList(1))
    }.toList

    val graphId = graphService.createGraph(nodes, edges)

    graphService.getGraphInfo(graphId) match {
      case Some((nodeCount, edgeCount, nodeList, edgeList, isConnected, isAcyclic)) =>
        val response = GraphInfoResponse(
          graphId, nodeCount, edgeCount,
          nodeList.asJava, edgeList.asJava,
          isConnected, isAcyclic
        )
        new ResponseEntity(response, HttpStatus.CREATED)
      case None =>
        new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
    }
  }

  @GetMapping(value = Array("/{graphId}"), produces = Array(MediaType.APPLICATION_JSON_VALUE))
  @Operation(
    summary = "Get graph information",
    description = "Returns detailed information about a graph"
  )
  @ApiResponses(Array(
    new ApiResponse(responseCode = "200", description = "Graph found"),
    new ApiResponse(responseCode = "404", description = "Graph not found")
  ))
  def getGraph(
    @Parameter(description = "Graph ID", required = true)
    @PathVariable graphId: String
  ): ResponseEntity[GraphInfoResponse] = {
    graphService.getGraphInfo(graphId) match {
      case Some((nodeCount, edgeCount, nodeList, edgeList, isConnected, isAcyclic)) =>
        val response = GraphInfoResponse(
          graphId, nodeCount, edgeCount,
          nodeList.asJava, edgeList.asJava,
          isConnected, isAcyclic
        )
        ResponseEntity.ok(response)
      case None =>
        new ResponseEntity(HttpStatus.NOT_FOUND)
    }
  }

  @GetMapping(produces = Array(MediaType.APPLICATION_JSON_VALUE))
  @Operation(
    summary = "List all graphs",
    description = "Returns a list of all graph IDs"
  )
  def listGraphs(): java.util.List[String] = {
    graphService.listGraphs().asJava
  }

  @GetMapping(value = Array("/{graphId}/shortest-path"),
              produces = Array(MediaType.APPLICATION_JSON_VALUE))
  @Operation(
    summary = "Find shortest path",
    description = "Finds the shortest path between two nodes in the graph"
  )
  @ApiResponses(Array(
    new ApiResponse(responseCode = "200", description = "Path computed"),
    new ApiResponse(responseCode = "404", description = "Graph not found")
  ))
  def findShortestPath(
    @Parameter(description = "Graph ID", required = true)
    @PathVariable graphId: String,
    @Parameter(description = "Source node", required = true, example = "A")
    @RequestParam source: String,
    @Parameter(description = "Target node", required = true, example = "D")
    @RequestParam target: String
  ): ResponseEntity[ShortestPathResponse] = {
    graphService.findShortestPath(graphId, source, target) match {
      case Some((path, length, exists)) =>
        val response = ShortestPathResponse(source, target, path.asJava, length, exists)
        ResponseEntity.ok(response)
      case None =>
        new ResponseEntity(HttpStatus.NOT_FOUND)
    }
  }

  @GetMapping(value = Array("/{graphId}/neighbors/{node}"),
              produces = Array(MediaType.APPLICATION_JSON_VALUE))
  @Operation(
    summary = "Get node neighbors",
    description = "Returns all neighbors of a specific node"
  )
  @ApiResponses(Array(
    new ApiResponse(responseCode = "200", description = "Neighbors found"),
    new ApiResponse(responseCode = "404", description = "Graph or node not found")
  ))
  def getNeighbors(
    @Parameter(description = "Graph ID", required = true)
    @PathVariable graphId: String,
    @Parameter(description = "Node name", required = true, example = "A")
    @PathVariable node: String
  ): ResponseEntity[NeighborsResponse] = {
    graphService.getNeighbors(graphId, node) match {
      case Some((neighbors, degree)) =>
        val response = NeighborsResponse(node, neighbors.asJava, degree)
        ResponseEntity.ok(response)
      case None =>
        new ResponseEntity(HttpStatus.NOT_FOUND)
    }
  }

  @GetMapping(value = Array("/{graphId}/traverse/bfs/{startNode}"),
              produces = Array(MediaType.APPLICATION_JSON_VALUE))
  @Operation(
    summary = "BFS traversal",
    description = "Performs a breadth-first search traversal starting from the given node"
  )
  @ApiResponses(Array(
    new ApiResponse(responseCode = "200", description = "Traversal completed"),
    new ApiResponse(responseCode = "404", description = "Graph or node not found")
  ))
  def bfsTraversal(
    @Parameter(description = "Graph ID", required = true)
    @PathVariable graphId: String,
    @Parameter(description = "Starting node", required = true, example = "A")
    @PathVariable startNode: String
  ): ResponseEntity[TraversalResponse] = {
    graphService.bfsTraversal(graphId, startNode) match {
      case Some(order) =>
        val response = TraversalResponse(startNode, "BFS", order.asJava)
        ResponseEntity.ok(response)
      case None =>
        new ResponseEntity(HttpStatus.NOT_FOUND)
    }
  }

  @GetMapping(value = Array("/{graphId}/traverse/dfs/{startNode}"),
              produces = Array(MediaType.APPLICATION_JSON_VALUE))
  @Operation(
    summary = "DFS traversal",
    description = "Performs a depth-first search traversal starting from the given node"
  )
  @ApiResponses(Array(
    new ApiResponse(responseCode = "200", description = "Traversal completed"),
    new ApiResponse(responseCode = "404", description = "Graph or node not found")
  ))
  def dfsTraversal(
    @Parameter(description = "Graph ID", required = true)
    @PathVariable graphId: String,
    @Parameter(description = "Starting node", required = true, example = "A")
    @PathVariable startNode: String
  ): ResponseEntity[TraversalResponse] = {
    graphService.dfsTraversal(graphId, startNode) match {
      case Some(order) =>
        val response = TraversalResponse(startNode, "DFS", order.asJava)
        ResponseEntity.ok(response)
      case None =>
        new ResponseEntity(HttpStatus.NOT_FOUND)
    }
  }

  @DeleteMapping(value = Array("/{graphId}"))
  @Operation(
    summary = "Delete a graph",
    description = "Deletes a graph by its ID"
  )
  @ApiResponses(Array(
    new ApiResponse(responseCode = "204", description = "Graph deleted"),
    new ApiResponse(responseCode = "404", description = "Graph not found")
  ))
  def deleteGraph(
    @Parameter(description = "Graph ID", required = true)
    @PathVariable graphId: String
  ): ResponseEntity[Void] = {
    if (graphService.deleteGraph(graphId)) {
      new ResponseEntity(HttpStatus.NO_CONTENT)
    } else {
      new ResponseEntity(HttpStatus.NOT_FOUND)
    }
  }
}

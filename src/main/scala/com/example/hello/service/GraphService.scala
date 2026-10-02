package com.example.hello.service

import org.springframework.stereotype.Service
import scalax.collection.edges.UnDiEdge
import scalax.collection.immutable.Graph

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import scala.jdk.CollectionConverters._

@Service
class GraphService {

  // Use Graph[String, UnDiEdge[String]] with the proper type parameter
  private val graphs = new ConcurrentHashMap[String, Graph[String, UnDiEdge[String]]]()

  def createGraph(nodes: List[String], edges: List[(String, String)]): String = {
    val graphId = s"graph-${UUID.randomUUID().toString.take(8)}"

    val edgeSet: Set[UnDiEdge[String]] = edges.map { case (from, to) =>
      UnDiEdge(from, to)
    }.toSet

    val graph: Graph[String, UnDiEdge[String]] = Graph.from(nodes, edgeSet)
    graphs.put(graphId, graph)
    graphId
  }

  def getGraph(graphId: String): Option[Graph[String, UnDiEdge[String]]] = {
    Option(graphs.get(graphId))
  }

  def getGraphInfo(graphId: String): Option[(Int, Int, List[String], List[String], Boolean, Boolean)] = {
    getGraph(graphId).map { graph =>
      val nodeCount = graph.nodes.size
      val edgeCount = graph.edges.size
      val nodes = graph.nodes.map(_.outer).toList.sorted
      val edges = graph.edges.map(e => s"${e.node1}--${e.node2}").toList.sorted
      val isConnected = graph.isConnected
      val isAcyclic = graph.isAcyclic
      (nodeCount, edgeCount, nodes, edges, isConnected, isAcyclic)
    }
  }

  def findShortestPath(graphId: String, source: String, target: String): Option[(List[String], Int, Boolean)] = {
    getGraph(graphId).map { graph =>
      val sourceNode = graph.find(source)
      val targetNode = graph.find(target)

      (sourceNode, targetNode) match {
        case (Some(s), Some(t)) =>
          s.shortestPathTo(t) match {
            case Some(path) =>
              val pathNodes = path.nodes.map(_.outer).toList
              (pathNodes, pathNodes.length - 1, true)
            case None =>
              (List.empty[String], 0, false)
          }
        case _ =>
          (List.empty[String], 0, false)
      }
    }
  }

  def getNeighbors(graphId: String, node: String): Option[(List[String], Int)] = {
    getGraph(graphId).flatMap { graph =>
      graph.find(node).map { n =>
        val neighbors = n.neighbors.map(_.outer).toList.sorted
        (neighbors, neighbors.size)
      }
    }
  }

  def bfsTraversal(graphId: String, startNode: String): Option[List[String]] = {
    getGraph(graphId).flatMap { graph =>
      graph.find(startNode).map { start =>
        start.outerNodeTraverser.toList
      }
    }
  }

  def dfsTraversal(graphId: String, startNode: String): Option[List[String]] = {
    getGraph(graphId).flatMap { graph =>
      graph.find(startNode).map { start =>
        start.outerNodeTraverser.withKind(scalax.collection.GraphTraversal.DepthFirst).toList
      }
    }
  }

  def deleteGraph(graphId: String): Boolean = {
    graphs.remove(graphId) != null
  }

  def listGraphs(): List[String] = {
    graphs.keys().asScala.toList
  }
}

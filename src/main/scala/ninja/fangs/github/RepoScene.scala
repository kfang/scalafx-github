package ninja.fangs.github

import org.eclipse.egit.github.core.Repository
import org.eclipse.egit.github.core.client.GitHubClient
import org.eclipse.egit.github.core.service.{IssueService, RepositoryService}
import scalafx.collections.ObservableBuffer
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.{VBox, Priority}
import scala.collection.JavaConversions._
import scalafx.Includes._
import scalafx.util.StringConverter

class RepoScene(client: GitHubClient, repos: RepositoryService) extends Scene {

  stylesheets = Seq("caspian.css")

  val issuesList = new VBox {
    content = Seq()
  }

  val reposList = ObservableBuffer[Repository](repos.getRepositories)
  val repoDropDown = new ChoiceBox[Repository](reposList){
    alignmentInParent = Pos.TOP_LEFT
    converter = StringConverter.toStringConverter[Repository](repo => repo.getName)
    selectionModel().selectedItem.onChange((value, _, _) => {
      val issueClient = new IssueService(client)
      val issues = issueClient.getIssues(value.value, Map[String, String]())
      issuesList.content = issues.map(issue => new Label(issue.getTitle))
    })
  }

  root = new VBox {
    content = Seq(repoDropDown, issuesList)
    spacing = 10
    padding = Insets(20)
    vgrow = Priority.ALWAYS
    hgrow = Priority.ALWAYS
  }

}

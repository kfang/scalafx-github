package ninja.fangs.github

import org.eclipse.egit.github.core.client.GitHubClient
import org.eclipse.egit.github.core.service.{IssueService, RepositoryService}
import scalafx.scene.Scene
import scalafx.scene.control.TabPane.TabClosingPolicy
import scalafx.scene.control.{TabPane, Tab}
import scalafx.scene.layout.{VBox, Priority}
import scala.collection.JavaConversions._

class RepoScene(client: GitHubClient, repos: RepositoryService) extends Scene {

  IssuesConsole.stage.width = 800
  IssuesConsole.stage.height = 800
  stylesheets = Seq("caspian.css")

  val repoTabs = repos.getRepositories.map(r => {
    new Tab {
      text = r.getName
      content = new VBox()
    }
  })

  root = new TabPane {
    tabs = repoTabs
    tabClosingPolicy = TabClosingPolicy.UNAVAILABLE
    vgrow = Priority.ALWAYS
    hgrow = Priority.ALWAYS
  }

}

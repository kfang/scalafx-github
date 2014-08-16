package ninja.fangs.github

import java.util

import org.eclipse.egit.github.core.{Repository, User}
import org.eclipse.egit.github.core.client.GitHubClient
import org.eclipse.egit.github.core.service.{IssueService, OrganizationService, RepositoryService}

import scala.util.Try
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.input.{MouseButton, MouseEvent}
import scalafx.scene.layout.{HBox, Priority, VBox}
import scala.collection.JavaConversions._
import scalafx.Includes._

class HomeScene(client: GitHubClient, repos: RepositoryService) extends Scene {
  case class RepoTreeItem(name: String, user: Option[String] = None, repo: Option[Repository] = None){
    override def toString = name
  }

  case class IssuePane(title: String, body: String) extends TitledPane {
    text = title
    content = {
      new ScrollPane(){
        content = new Label(body)
      }
    }
    collapsible = true
  }

  val mainPanel = new VBox {
    content = Seq(new Label("main panel stuff"))
    minHeight = 300
    minWidth = 500
    vgrow = Priority.ALWAYS
    hgrow = Priority.ALWAYS
  }

  val sidePanel = new VBox {

    val root = new TreeItem[RepoTreeItem](RepoTreeItem("repo-root"))

    val orgRepos = new OrganizationService(client).getOrganizations.map(org => {
      val parent = new TreeItem[RepoTreeItem](RepoTreeItem(org.getLogin))
      val orgrepos = new RepositoryService(client).getOrgRepositories(org.getLogin)
      val labels = orgrepos.sortBy(_.getName).map(r => {
        new TreeItem[RepoTreeItem](RepoTreeItem(r.getName, Some(org.getLogin), Some(r)))
      })
      parent.children = labels
      parent
    })

    val selfRepos = {
      val parent = new TreeItem[RepoTreeItem](RepoTreeItem(client.getUser))
      val selfrepos = new RepositoryService(client).getRepositories.map(repo => {
        new TreeItem[RepoTreeItem](RepoTreeItem(repo.getName, Some(client.getUser), Some(repo)))
      })
      parent.children = selfrepos
      parent
    }

    root.children = orgRepos.:+(selfRepos)
    val rootView = new TreeView[RepoTreeItem](root)
    rootView.selectionModel().setSelectionMode(SelectionMode.SINGLE)
    rootView.onMouseClicked = (e: MouseEvent) => e.button match {
      case MouseButton.PRIMARY   =>
        val sel = rootView.selectionModel().getSelectedItem.getValue
        (sel.user, sel.repo) match {
          case (Some(user), Some(repo)) => Try {
            val issues = new IssueService(client).getIssues(repo, new util.HashMap())
            val issuePanes = issues.map(i => IssuePane(i.getTitle, i.getBody))
            val acc = new Accordion()
            acc.panes = issuePanes
            mainPanel.content = acc
          }
          case _ =>
        }
      case MouseButton.SECONDARY => println("secondary")
      case MouseButton.MIDDLE    => println("middle")
      case MouseButton.NONE      => println("none")
    }
    rootView.showRoot = false

    content = Seq(rootView)
    minHeight = 300
    minWidth = 300
    padding = Insets(20)
    vgrow = Priority.ALWAYS
    hgrow = Priority.ALWAYS
    style = "-fx-background-color: #336699;"
  }

  root = new HBox {
    content = Seq(sidePanel, mainPanel)
//    spacing = 10
//    padding = Insets(20)
    vgrow = Priority.ALWAYS
    hgrow = Priority.ALWAYS
  }

}

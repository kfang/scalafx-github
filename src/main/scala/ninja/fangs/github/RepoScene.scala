package ninja.fangs.github

import org.eclipse.egit.github.core.Repository
import org.eclipse.egit.github.core.client.GitHubClient
import org.eclipse.egit.github.core.service.{OrganizationService, IssueService, RepositoryService}
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, VBox, Priority}
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

  val userRepoSearchBox = new HBox {

    val userTextField = new TextField {
      promptText = "user"
      text = client.getUser
    }
    val typeDropDown = new ChoiceBox[String](ObservableBuffer[String](Seq("user", "organization"))){
      selectionModel().selectFirst()
    }

    val userSubmitBtn = new Button {
      text = "submit"
      defaultButton = true
      onAction = (e: ActionEvent) => typeDropDown.selectionModel().getSelectedItem match {
        case "user"         =>
          val userRepos = repos.getRepositories(userTextField.text.value)
          reposList.clear()
          reposList.++=(userRepos)
          repoDropDown.selectionModel().selectFirst()
        case "organization" =>
          val orgRepos = repos.getOrgRepositories(userTextField.text.value)
          reposList.clear()
          reposList.++=(orgRepos)
          repoDropDown.selectionModel().selectFirst()
      }
    }

    content = Seq(userTextField, typeDropDown, userSubmitBtn)
    spacing = 10
  }

  root = new VBox {
    content = Seq(userRepoSearchBox, repoDropDown, issuesList)
    spacing = 10
    padding = Insets(20)
    vgrow = Priority.ALWAYS
    hgrow = Priority.ALWAYS
  }

}



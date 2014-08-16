package ninja.fangs.github

import org.eclipse.egit.github.core.client.GitHubClient
import org.eclipse.egit.github.core.service.RepositoryService
import scalafx.event.ActionEvent
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.{Button, PasswordField, TextField}
import scalafx.scene.layout.{Priority, HBox}
import scalafx.Includes._

class LoginScene extends Scene {
  stylesheets = Seq("caspian.css")

  root = new HBox {

    val username = new TextField {
      promptText = "email"
      alignmentInParent = Pos.BASELINE_CENTER
    }
    val password = new PasswordField {
      promptText = "password"
      alignmentInParent = Pos.BASELINE_CENTER
    }
    val submit = new Button {
      text = "Submit"
      alignmentInParent = Pos.BASELINE_CENTER
      defaultButton = true
      onAction = (e: ActionEvent) => {
        val client = new GitHubClient()
        client.setCredentials(username.text.value, password.text.value)
        val repos = new RepositoryService(client)
        val repoScene = new HomeScene(client, repos)
        IssuesConsole.stage.scene = repoScene
      }
    }

    content = Seq(username, password, submit)
    spacing = 10
    padding = Insets(20)
    vgrow = Priority.ALWAYS
    hgrow = Priority.ALWAYS
  }
}

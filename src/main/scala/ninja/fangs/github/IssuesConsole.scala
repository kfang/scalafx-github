package ninja.fangs.github

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage

object IssuesConsole extends JFXApp {

  stage = new PrimaryStage {
    title = "GitHub Issues Console"
    scene = new LoginScene()
  }

}

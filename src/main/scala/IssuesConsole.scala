import org.eclipse.egit.github.core.service.{RepositoryService, IssueService}
import scala.collection.JavaConversions._

object IssuesConsole {

  def main (args: Array[String]) {
    val repos = new RepositoryService()
    repos.getRepositories.map(r => println(r.getName))
    val issues = new IssueService()
    issues.getIssues.map(ri => println(ri.getTitle))
  }

}

package akkaseed

import org.scalatest.{Matchers, WordSpec}
import spray.json._
import Protocol._


class ProtocolSpec extends WordSpec with Matchers with TaskTypeAnalyzer {

  import TaskType._

  val parallel = UnitOfWork(taskType = Parallel, payload = new JsString("testParallel"))
  val sequential = UnitOfWork(taskType = Sequential, payload = new JsString("testSequential"))

  val parallelGroup = Set(parallel)
  val sequentialGroup = Set(sequential)
  val mixedGroup = Set(parallel, sequential)
  val emptyGroup = Set.empty

  "TaskTypeAnalyzer" should {

    "return Parallel task type if all tasks in the set are parallel" in {
      getTaskType(parallelGroup) shouldBe Parallel
    }

    "return Sequential task type if all tasks in the set are sequential" in {
      getTaskType(sequentialGroup) shouldBe Sequential
    }

    "return Mixed task type if tasks in the set are both parallel and sequential" in {
      getTaskType(mixedGroup) shouldBe Mixed
    }

    "return Mixed task type if set is empty" in {
      getTaskType(emptyGroup) shouldBe Mixed
    }

  }
}

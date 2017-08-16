package akkaseed


import spray.json.JsValue

object Protocol {

  object TaskType {
    trait TaskType
    case object Parallel extends TaskType
    case object Sequential extends TaskType
    case object Mixed extends TaskType
  }

  import TaskType._

  trait Task {
    def taskType: TaskType
  }

  trait TaskTypeAnalyzer {
    def getTaskType[T <: Task](work: Set[T]): TaskType = {
      val isParallel = work.forall(_.taskType == Parallel)
      val isSequential = work.forall(_.taskType == Sequential)
      if (isParallel && !isSequential){
        Parallel
      } else if (!isParallel && isSequential) {
        Sequential
      } else {
        Mixed
      }
    }
  }

  final case class UnitOfWork(taskType: TaskType, payload: JsValue) extends Task

  final case class TaskGroup(units: Set[UnitOfWork]) extends Task with TaskTypeAnalyzer{
    val taskType: TaskType = getTaskType(units)
  }

  final case class Batch(groups: Set[TaskGroup]) extends Task with TaskTypeAnalyzer {
    val taskType: TaskType = getTaskType(groups)
  }


  case object EmptyTask extends Task { val taskType = Sequential }
  final case class SampleTask(text: String) extends Task { val taskType = Mixed}
  //final case class Batch(tasks: Set[Task]) extends Task

  final case class TaskResult(text: String)
  final case class TaskFailed(task: Task, reason: String)
  //TODO: add batch result and failure


  case object WorkerRegistration
}

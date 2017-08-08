package akkaseed

object Protocol {

  trait Task

  /*
  case class UnitOfWork extends Task
  case class TaskGroup(work: Set[UnitOfWork]) extends Task
  case class Batch(groups: Set[TaskGroup]) extends Task
   */
  case object EmptyTask extends Task
  final case class SampleTask(text: String) extends Task
  final case class Batch(tasks: Set[Task]) extends Task

  final case class TaskResult(text: String)
  final case class TaskFailed(task: Task, reason: String)
  //TODO: add batch result and failure


  case object WorkerRegistration
}

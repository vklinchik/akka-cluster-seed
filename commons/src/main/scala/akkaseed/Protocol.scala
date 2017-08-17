package akkaseed


import spray.json.JsValue

object Protocol {

  object TaskType {
    trait TaskType
    case object Parallel extends TaskType
    case object Sequential extends TaskType
    case object Mixed extends TaskType
  }

  object ResultType {
    trait ResultType
    case object Success extends ResultType
    case object Failure extends ResultType
    case object Warning extends ResultType
  }

  import TaskType._
  import ResultType._

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


  trait Result {
    def resultType: ResultType
  }


  trait ResultTypeAnalyzer {

    def isSuccess[T <: Result](results: Set[T]): Boolean = {
      results.forall(result => result.resultType == Success && result.resultType != Failure && result.resultType != Warning)
    }

    def isSuccessWithWarning[T <: Result](results: Set[T]): Boolean = {
      results.forall(result => result.resultType == Success || result.resultType != Warning && result.resultType != Failure)
    }

    def containsFailure[T <: Result](results: Set[T]): Boolean = !isSuccessWithWarning(results)

    def getResultType[T <: Result](results: Set[T]): ResultType = {
      if (isSuccess(results)) {
        ResultType.Success
      } else if (isSuccessWithWarning(results)) {
        ResultType.Warning
      } else {
        ResultType.Failure
      }
    }
  }

  final case class UnitOfWork(taskType: TaskType, payload: JsValue) extends Task

  final case class TaskGroup(units: Set[UnitOfWork]) extends Task with TaskTypeAnalyzer {
    val taskType: TaskType = getTaskType(units)
  }

  final case class Batch(groups: Set[TaskGroup]) extends Task with TaskTypeAnalyzer {
    val taskType: TaskType = getTaskType(groups)
  }

  final case class UnitOfWorkResult(unit: UnitOfWork, resultType: ResultType) extends Result

  final case class TaskGroupResult(group: TaskGroup) extends Result with ResultTypeAnalyzer {
    lazy val resultType: ResultType = getResultType(group.units)
  }

  final case class BatchResult(batch: Batch) extends Result with ResultTypeAnalyzer {
    lazy val resultType: ResultType = getResultType(batch.groups)

    def markAllAsFailure(): BatchResult = {
      val groups = batch.groups

      groups.map{ group =>
        val units = group.units
        units.map{ unit =>

        }
      }
    }
  }



  case object WorkerRegistration
}

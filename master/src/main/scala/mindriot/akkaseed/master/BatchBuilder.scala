package mindriot.akkaseed.master

import akkaseed.Protocol.TaskType.Parallel
import akkaseed.Protocol.{Batch, TaskGroup, UnitOfWork}
import akkaseed.RandomString
import spray.json.JsString

object BatchBuilder extends RandomString {

  def generateBatch(groupCount: Int, unitsPerGroup: Int) = {
    val groups =
      (0 to groupCount).map { _ =>
        val units = (0 to unitsPerGroup).map{ _ => UnitOfWork(taskType = Parallel, payload = JsString(generate)) }.toSet
        TaskGroup(units)
      }
      .toSet

    Batch(groups)
  }
}

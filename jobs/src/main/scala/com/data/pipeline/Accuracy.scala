package com.data.pipeline

import org.apache.spark.ml.PipelineModel
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.{DataFrame, SaveMode}
import org.apache.spark.sql.functions.{abs, avg, max, not, round, when}

object Accuracy {
  def main(args: Array[String]): Unit = {
    val branch = args(0)
    val accuracy = new Accuracy(branch)
    accuracy.write()
  }
}

class Accuracy(branch: String) extends PipelinePoint[(DataFrame, DataFrame)] {

  import spark.implicits._

  override def write(): Unit = {
    val (predicted, accuracy) = this.transform()

    accuracy.write.mode(SaveMode.Overwrite).parquet(s"s3a://experiments/$branch/accuracy.parquet")
    predicted.write.mode(SaveMode.Overwrite).parquet(s"s3a://experiments/$branch/comparison.parquet")
  }

  override def transform(): (DataFrame, DataFrame) = {
    val estimationMethod = PipelineModel.load(s"s3a://experiments/$branch/estimation.model")
    val validationSet = spark.read.parquet(s"s3a://experiments/$branch/validation_set.parquet")

    val predicted = estimationMethod.transform(validationSet)
      .transform(roundMethodology)
      .transform(unknowReplacement)

    val accuracy = predicted.transform(accuracyCalculation)

    accuracy.show()

    (predicted, accuracy)
  }

  protected def roundMethodology(df: DataFrame): DataFrame = {
    df.withColumn("prediction", round($"prediction"))
  }

  protected def unknowReplacement(df: DataFrame): DataFrame = {
    df.withColumn("prediction", when($"prediction" < 0, 0)
      .otherwise($"prediction"))
  }

  protected def accuracyCalculation(df: DataFrame): DataFrame = {
    df.withColumn("delta", abs($"units" - $"prediction"))
      .agg(avg("delta") as "MAE")
  }
}

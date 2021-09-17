package com.data.pipeline

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.ml.util.MLWritable

object EstimationMethod {
  def main(args: Array[String]): Unit = {
    val branch = args(0)
    val model = new EstimationMethod(branch)
    model.write()
  }
}
class EstimationMethod(val branch: String) extends PipelinePoint[MLWritable] {
  override def write(): Unit = {
    val model = this.transform()
    model
      .write.overwrite()
      .save(s"s3a://experiments/$branch/estimation.model")
  }

  override def transform(): MLWritable = {
    val trainSet = spark.read.parquet(s"s3a://experiments/$branch/train_set.parquet")
    val lr = new LinearRegression()
      .setLabelCol("units")

    val chartTraining = new VectorAssembler()
      .setInputCols(Array("rank"))
      .setOutputCol("features")

    val pipeline = new Pipeline()
      .setStages(Array(chartTraining, lr))

    pipeline.fit(trainSet)
  }
}
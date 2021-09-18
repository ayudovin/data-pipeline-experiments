# The DAG object; we'll need this to instantiate a DAG

# Operators; we need this to operate!
from airflow import DAG
from airflow.providers.apache.spark.operators.spark_submit import SparkSubmitOperator
from dateutil.utils import today
from airflow.models import Variable

# These args will get passed on to each operator
# You can override them on a per-task basis during operator initialization
default_args = {
    'owner': 'Artsiom Yudovin',
    'depends_on_past': False,
    'start_date': today(),
}

with DAG(
        'data-pipeline-experiments',
        default_args=default_args,
        description='Conduct experiments in data pipelines',
        tags=['data', 'pipeline', 'experiments'],
) as dag:

    branch = Variable.get("branch")
    bsr_preparation = SparkSubmitOperator(application="spark-job.jar",
                                          java_class="com.data.pipeline.RankPreparation", task_id="bsr_preparation",
                                          spark_binary="/spark/bin/spark-submit", application_args=[branch],
                                          driver_memory="4g")

    units_preparation = SparkSubmitOperator(application="spark-job.jar",
                                            java_class="com.data.pipeline.UnitsPreparation",
                                            task_id="units_preparation",
                                            spark_binary="/spark/bin/spark-submit", application_args=[branch],
                                            driver_memory="4g")

    rank_unit_accord = SparkSubmitOperator(application="spark-job.jar",
                                           java_class="com.data.pipeline.RankUnitAccord",
                                           task_id="rank_units_accord",
                                           spark_binary="/spark/bin/spark-submit", application_args=[branch],
                                           driver_memory="4g")

    split = SparkSubmitOperator(application="spark-job.jar",
                                java_class="com.data.pipeline.Split",
                                task_id="split",
                                spark_binary="/spark/bin/spark-submit", application_args=[branch],
                                driver_memory="4g")

    estimation_method = SparkSubmitOperator(application="spark-job.jar",
                                            java_class="com.data.pipeline.EstimationMethod",
                                            task_id="estimation_method",
                                            spark_binary="/spark/bin/spark-submit", application_args=[branch],
                                            driver_memory="4g")

    accuracy = SparkSubmitOperator(application="spark-job.jar",
                                   java_class="com.data.pipeline.Accuracy",
                                   task_id="accuracy",
                                   spark_binary="/spark/bin/spark-submit", application_args=[branch],
                                   driver_memory="4g")

    sold_units = SparkSubmitOperator(application="spark-job.jar",
                                     java_class="com.data.pipeline.ApplySoldUnits",
                                     task_id="sold_units",
                                     spark_binary="/spark/bin/spark-submit", application_args=[branch],
                                     driver_memory="4g")

    [bsr_preparation, units_preparation] >> rank_unit_accord >> split >> estimation_method >> [accuracy, sold_units]

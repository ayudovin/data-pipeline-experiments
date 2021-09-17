FROM apache/airflow:2.1.3
ARG DEBIAN_FRONTEND=noninteractive
ARG SPARK_VERSION=3.1.2

USER root

RUN pip install 'apache-airflow-providers-apache-spark'
RUN apt-get update
RUN apt-get install openjdk-11-jdk -y

RUN apt-get install -y curl wget \
      && wget http://archive.apache.org/dist/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop3.2.tgz \
      && tar -xvzf spark-${SPARK_VERSION}-bin-hadoop3.2.tgz \
      && mv spark-${SPARK_VERSION}-bin-hadoop3.2 /spark \
      && rm spark-${SPARK_VERSION}-bin-hadoop3.2.tgz \
      && cd /
RUN wget https://repo1.maven.org/maven2/org/apache/hadoop/hadoop-aws/3.2.0/hadoop-aws-3.2.0.jar
RUN mv hadoop-aws-3.2.0.jar /spark/jars
RUN wget https://repo1.maven.org/maven2/com/amazonaws/aws-java-sdk-bundle/1.11.375/aws-java-sdk-bundle-1.11.375.jar
RUN mv aws-java-sdk-bundle-1.11.375.jar /spark/jars


#CMD airflow db init
#CMD airflow users create --username admin --password admin --role Admin --firstname Artsiom --lastname Yudovin --email admin@example.org
#CMD airflow webserver -D
#CMD airflow scheduler -D
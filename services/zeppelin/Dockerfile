FROM apache/zeppelin:0.10.0

ENV SPARK_VERSION=3.1.2
ENV HADOOP_VERSION=3.2

USER root

# support Kerberos certification
RUN export DEBIAN_FRONTEND=noninteractive && apt-get update && apt-get install -yq krb5-user libpam-krb5 && apt-get clean

RUN apt-get update && apt-get install -y curl unzip wget grep sed vim tzdata && apt-get clean

# auto upload zeppelin interpreter lib
RUN rm -rf /zeppelin

RUN rm -rf /spark
RUN wget https://dlcdn.apache.org/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz
RUN tar -xvzf spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz
RUN mv spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION} spark
RUN rm spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz
RUN wget https://repo1.maven.org/maven2/org/apache/hadoop/hadoop-aws/3.2.0/hadoop-aws-3.2.0.jar
RUN mv hadoop-aws-3.2.0.jar spark/jars
RUN wget https://repo1.maven.org/maven2/com/amazonaws/aws-java-sdk-bundle/1.11.375/aws-java-sdk-bundle-1.11.375.jar
RUN mv aws-java-sdk-bundle-1.11.375.jar spark/jars
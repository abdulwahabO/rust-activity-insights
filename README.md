# Overview: RustLang Github Insights.

This is a simple data pipeline project. The dashbord shows................
The raw data is...................... Not meant for real-world use. just me sharpening my skills.


-- project layout.

> Media: GIF of Dashboard.

# Architecture

> Media: Architecture Diagram.

The pipeline kicks off with an extractor program running on AWS Lambda... 

# Tech Stack

* Java 11
* Spring Boot - Web Framework for serving the dashboard UI and feeding it data. 
* Bulma - CSS library for styling the UI of the dashboard. 
* Apache Spark - For distributed batch processing of the raw data from Github's API.
* AWS: 
  - Lambda - Runs the extractor program that populates an S3 bucket with raw Github data.
  - S3 - Storage for raw Github data before the Spark job processes them.
  - DynamoDB - Persistence for the final output of the Spark job.
  - Elastic Beanstalk - Provides a way to conveniently deploy dashboard on EC2.


## Planned Improvement: Spark Kubernetes Cluster


## Planned Improvement: Pipeline Orchestration

        - K8s cluster.
        - Pipeline orchestration - STEP FUNCTIONS
# AWS LOCALSTACK PROJECT

## Table of Contents
1. [Introduction](#introduction)
2. [Technologies Used](#technologies-used)
3. [Prerequisites](#prerequisites)
4. [Configuration and Deployment](#configuration-and-deployment)
5. [Usage and Examples](#usage-and-examples)
6. [Scalability and Architecture](#scalability-and-architecture)
7. [Common Issues and Troubleshooting](#common-issues-and-troubleshooting)
8. [Contribution and Support](#contribution-and-support)
9. [License](#license)
10. [References](#references)

## Introduction

This project is designed to bridge the gap between theoretical knowledge and practical application in cloud services. It provides a sandbox environment where both beginners and experienced cloud practitioners can learn, experiment, and develop without the costs and risks associated with an actual AWS cloud environment.

Using LocalStack, the project emulates essential AWS services such as S3, Lambda, DynamoDB, and API Gateway. These services are integral to many cloud-based applications, and mastering them is critical for modern cloud computing practices. The project's scalability and adaptability make it a perfect learning platform, offering transferable skills for real-world AWS environments. Additionally, it serves as a practical resource for educators in cloud computing, enhancing both learning and teaching experiences in this rapidly evolving field.

## Technologies Used

### LocalStack
LocalStack is a comprehensive tool that simulates the cloud environment of Amazon Web Services (AWS) locally on your machine. It provides a testing and development platform that supports a wide range of AWS services, enabling developers to run cloud applications offline and without incurring any costs. LocalStack is particularly useful for debugging, rapid prototyping, and continuous integration testing, allowing users to ensure their applications function correctly before deployment to the actual AWS cloud.

### AWS Services
The project utilizes the following AWS services, simulated through LocalStack:

  - S3 (Simple Storage Service): Used for storing and retrieving data. This project uses S3 to store various files and artifacts, demonstrating how to interact with cloud-based storage in a local environment.

  - Lambda: Employs serverless computing capabilities, enabling users to run code in response to events without provisioning or managing servers. In this project, Lambda functions are used for various purposes, such as processing data or integrating with other AWS services.

  - DynamoDB: A fast and flexible NoSQL database service. In this project, DynamoDB is employed for database management, reflecting its potential for future large-scale horizontal expansion. While the current implementation may not demonstrate handling massive datasets, the choice of DynamoDB is strategic, considering its scalability and performance in larger, more complex applications.

  - API Gateway: Manages and facilitates the creation, deployment, and maintenance of APIs. This project uses API Gateway to establish RESTful APIs. The inclusion of API Gateway is based on its scalability and ability to handle a high number of requests, making it a suitable choice for future expansion and more extensive application integration.

## Prerequisites

### LocalStack Installation

LocalStack can be easily set up on a local development machine using Docker, which simplifies the process of configuring and running AWS-like services. Here's how you can get started:

1. **Install Docker**: Ensure Docker is installed on your machine. Docker provides the necessary environment to run LocalStack. You can download and install Docker from [Docker's official website](https://www.docker.com/get-started).

2. **Pull LocalStack Docker Image**: Once Docker is installed and running, you can pull the LocalStack Docker image. Open a terminal or command prompt and run the following command:
    ```bash
    docker pull localstack/localstack
    ```
    This command downloads the latest LocalStack image which contains the services you need.

  ## Configuration and Deployment

1. **Run LocalStack**:
   To start LocalStack, use the following Docker command:
    ```bash
    docker run -d -p 4566:4566 -p 4571:4571 localstack/localstack
    ```
    This command runs LocalStack in a Docker container and exposes the default ports. Port `4566` is the main entry point for all service requests. Port `4571` is used for the legacy S3 service.

3. **Environment Configuration**:
   Optionally, you can set environment variables to configure LocalStack. For example, setting `SERVICES=s3,lambda,dynamodb,apigateway` will launch only these specified services. To do this, add `-e` followed by the environment variable in the Docker run command:
    ```bash
    docker run -d -e SERVICES=s3,lambda,dynamodb,apigateway -p 4566:4566 -p 4571:4571 localstack/localstack
    ```

5. **Verify Installation**:
   To verify that LocalStack is running correctly, you can use the following command:
    ```bash
    docker logs [container_id]
    ```
    Replace `[container_id]` with the ID of your LocalStack Docker container. This command displays the logs of the LocalStack container and should show the services starting up.

7. **Access LocalStack Services**:
   Once LocalStack is running, you can access the services using the AWS CLI or SDKs. Remember to configure these tools to point to your local endpoint (typically `http://localhost:4566`) instead of the real AWS endpoints.

9. **Stopping LocalStack**:
    To stop LocalStack, use the Docker stop command followed by your container ID:
    ```bash
    docker stop [container_id]
    ```

This setup provides a convenient and efficient way to mimic AWS cloud services locally, facilitating development and testing without the need to connect to actual AWS services.

## Usage and Examples

### Running the Main Script

To initiate the project setup and configure AWS services using LocalStack, follow these steps:

1. **Stop and Start LocalStack**:
   
   Ensure LocalStack is not already running. If it is, stop and restart it to ensure a clean state.
   ```bash
   docker stop milocalstack
   docker start milocalstack
   ```
2. **Navigate to the Project Directory**:
   
   Change the directory to the location of your AWS setup script.
   ```bash
   cd $args[0]
   ```
3. **Create S3 Buckets:**
   
   Execute the commands to create S3 buckets for storing artifacts, data lake files, and lambda logs.
   ```bash
   aws s3 mb s3://gitradar-datalake --endpoint-url=http://localhost:4566
   aws s3 mb s3://gitradar-artifacts --endpoint-url=http://localhost:4566
   aws s3 mb s3://gitradar-lambda-logs --endpoint-url=http://localhost:4566
   ```
   
4. **Upload Artifacts to S3**
   
   Upload necessary artifacts to the gitradar-artifacts bucket.
   ```bash
   aws s3 cp .\artifacts s3://gitradar-artifacts --recursive --endpoint-url=http://localhost:4566
   ```
5. **Create DynamoDB Tables:**
    
   Set up DynamoDB tables for storing tokenized suggestion data and metrics.
   ```bash
   aws dynamodb create-table `
    --table-name FilesTokens `
    --attribute-definitions AttributeName=FileName,AttributeType=S `
    --key-schema AttributeName=FileName,KeyType=HASH `
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 `
    --endpoint-url http://localhost:4566

   aws dynamodb create-table `
    --table-name FilesMetrics `
    --attribute-definitions AttributeName=FileName,AttributeType=S `
    --key-schema AttributeName=FileName,KeyType=HASH `
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 `
    --endpoint-url http://localhost:4566
   ```
6. **Deploy Lambda Functions:**
   
   Deploy Lambda functions for metrics analysis and tokenization. These functions are defined in packages stored in the `gitradar-artifacts` S3 bucket.
   - For creating the gitradar-code-metrics Lambda function:
   ```bash
   aws --endpoint-url=http://localhost:4566 lambda create-function \
    --function-name gitradar-code-metrics \
    --runtime python3.8 \
    --handler gitradar-code-metrics.lambda_handler \
    --code S3Bucket=gitradar-artifacts,S3Key=lambda_metrics_package.zip \
    --role arn:aws:iam::000000000000:role/lambda-role
   ```
   - Add permissions and update configuration for 'gitradar-code-metrics'
   ```bash
   aws --endpoint-url=http://localhost:4566 lambda add-permission \
    --function-name gitradar-code-metrics \
    --statement-id s3invoke \
    --action "lambda:InvokeFunction" \
    --principal s3.amazonaws.com \
    --source-arn arn:aws:s3:::gitradar-datalake

    aws lambda update-function-configuration \
     --function-name gitradar-code-metrics \
     --timeout 200 \
     --memory-size 2048 \
     --endpoint-url=http://localhost:4566

   ```
   - For creating the gitradar-code-tokenizer Lambda function:
   ```bash
   aws --endpoint-url=http://localhost:4566 lambda create-function `
    --function-name gitradar-code-tokenizer `
    --runtime python3.8 `
    --handler gitradar-code-tokenizer.lambda_handler `
    --code S3Bucket=gitradar-artifacts,S3Key=lambda_tokenizer_package.zip `
    --role arn:aws:iam::000000000000:role/lambda-role
   ```
   - Add permissions and update configuration for 'gitradar-code-tokenizer'
   ```bash
   aws --endpoint-url=http://localhost:4566 lambda add-permission --function-name gitradar-code-tokenizer --statement-id s3invoke --action "lambda:InvokeFunction" principal s3.amazonaws.com --source-arn arn:aws:s3:::gitradar-datalake
   ```
8. **Set Up API Gateway:**
   
   Create and configure a REST API using API Gateway to interact with the deployed Lambda functions.
   ```bash
   # Command to create a new REST API and define resources, methods, and integrations
   ```
   Deploy the API to make it accessible:
   ```bash
   # Command to deploy the API and generate accessible endpoints
   ```
   The API endpoints will be echoed for easy access:
   ```bash
   echo "API Endpoints:"
   echo "Metrics API: http://localhost:4566/restapis/$apiID/test/_user_request_/metrics"
   echo "Suggestions API: http://localhost:4566/restapis/$apiID/test/_user_request_/suggestions"
   ```
9. **Upload Data to S3 Bucket:**

   Upload files to the gitradar-datalake bucket. This triggers the Lambda functions as per the bucket notification configuration.
   ```bash
   $sourceDir = ".\gitradar-datalake\"
   $s3Bucket = "s3://gitradar-datalake"
   $endpointUrl = "http://localhost:4566"

   foreach ($file in Get-ChildItem -Path $sourceDir) {
    aws s3 cp "$sourceDir$file" "$s3Bucket" --endpoint-url="$endpointUrl"
    Start-Sleep -Seconds 2
   }

   ```
   
### Clone the Repository
```bash
git clone [repository URL]

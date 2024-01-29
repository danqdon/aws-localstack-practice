docker stop milocalstack

docker start milocalstack

cd $args[0]

sleep 2

aws s3 mb s3://gitradar-datalake --endpoint-url=http://localhost:4566

aws s3 mb s3://gitradar-artifacts --endpoint-url=http://localhost:4566

aws s3 mb s3://gitradar-lambda-logs --endpoint-url=http://localhost:4566

aws s3 ls

aws s3 cp .\artifacts s3://gitradar-artifacts --recursive --endpoint-url=http://localhost:4566

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



aws --endpoint-url=http://localhost:4566 lambda create-function `
    --function-name gitradar-code-metrics `
    --runtime python3.8 `
    --handler gitradar-code-metrics.lambda_handler `
    --code S3Bucket=gitradar-artifacts,S3Key=lambda_metrics_package.zip `
    --role arn:aws:iam::000000000000:role/lambda-role

sleep 3

aws --endpoint-url=http://localhost:4566 lambda add-permission --function-name gitradar-code-metrics --statement-id s3invoke --action "lambda:InvokeFunction" --principal s3.amazonaws.com --source-arn arn:aws:s3:::gitradar-datalake

sleep 1

aws lambda update-function-configuration --function-name gitradar-code-metrics --timeout 200
aws lambda update-function-configuration --function-name gitradar-code-metrics --memory-size 2048

aws --endpoint-url=http://localhost:4566 lambda create-function `
    --function-name gitradar-code-tokenizer `
    --runtime python3.8 `
    --handler gitradar-code-tokenizer.lambda_handler `
    --code S3Bucket=gitradar-artifacts,S3Key=lambda_tokenizer_package.zip `
    --role arn:aws:iam::000000000000:role/lambda-role

sleep 3
    
aws --endpoint-url=http://localhost:4566 lambda add-permission --function-name gitradar-code-tokenizer --statement-id s3invoke --action "lambda:InvokeFunction" --principal s3.amazonaws.com --source-arn arn:aws:s3:::gitradar-datalake

sleep 1

aws lambda update-function-configuration --function-name gitradar-code-tokenizer --timeout 200
aws lambda update-function-configuration --function-name gitradar-code-tokenizer --memory-size 2048

aws --endpoint-url=http://localhost:4566 s3api put-bucket-notification-configuration --bucket gitradar-datalake --notification-configuration '{\"LambdaFunctionConfigurations\": [{\"Id\": \"lambda-function-1\", \"LambdaFunctionArn\": \"arn:aws:lambda:us-east-1:000000000000:function:gitradar-code-metrics\", \"Events\": [\"s3:ObjectCreated:*\"]}, {\"Id\": \"lambda-function-2\", \"LambdaFunctionArn\": \"arn:aws:lambda:us-east-1:000000000000:function:gitradar-code-tokenizer\", \"Events\": [\"s3:ObjectCreated:*\"]}]}'

aws --endpoint-url=http://localhost:4566 s3api get-bucket-notification-configuration --bucket gitradar-datalake

aws --endpoint-url=http://localhost:4566 lambda create-function --function-name apigw-lambda --runtime python3.8 --handler ApiLambda1.lambda_handler --memory-size 128 --code S3Bucket=gitradar-artifacts,S3Key=lambda_api1_package.zip --role arn:aws:iam::111111111111:role/apigw

aws --endpoint-url=http://localhost:4566 lambda create-function --function-name apigw-lambda2 --runtime python3.8 --handler ApiLambda2.lambda_handler --memory-size 128 --code S3Bucket=gitradar-artifacts,S3Key=lambda_api2_package.zip --role arn:aws:iam::111111111111:role/apigw

$response = aws --endpoint-url=http://localhost:4566 apigateway create-rest-api --name "API try2"

$apiID = $response.Split('\"')[4]

$response = aws --endpoint-url=http://localhost:4566 apigateway get-resources --rest-api-id $apiID

$parentID = $response.Split('\"')[8]

$response = aws --endpoint-url=http://localhost:4566 apigateway create-resource --rest-api-id $apiID --parent-id $parentID --path-part "{somethingId}"

$resourceID = $response.Split('\"')[4]

aws --endpoint-url=http://localhost:4566 apigateway put-method --rest-api-id $apiID --resource-id $resourceID --http-method GET --request-parameters "method.request.path.somethingId=true" --authorization-type "NONE"

aws --endpoint-url=http://localhost:4566 apigateway put-integration --rest-api-id $apiID --resource-id $resourceID --http-method GET --type AWS_PROXY --integration-http-method POST --uri arn:aws:apigateway:us-east-1:lambda:path/2015-03-31/functions/arn:aws:lambda:us-east-1:000000000000:function:apigw-lambda/invocations --passthrough-behavior WHEN_NO_MATCH

aws --endpoint-url=http://localhost:4566 apigateway create-deployment --rest-api-id $apiID --stage-name metrics

$response = aws --endpoint-url=http://localhost:4566 apigateway create-resource --rest-api-id $apiID --parent-id $parentID --path-part "suggestions"

$resourceID = $response.Split('\"')[4]

aws --endpoint-url=http://localhost:4566 apigateway put-method --rest-api-id $apiID --resource-id $resourceID --http-method GET --request-parameters "method.request.path.somethingId=true" --authorization-type "NONE"

aws --endpoint-url=http://localhost:4566 apigateway put-integration --rest-api-id $apiID --resource-id $resourceID --http-method GET --type AWS_PROXY --integration-http-method POST --uri arn:aws:apigateway:us-east-1:lambda:path/2015-03-31/functions/arn:aws:lambda:us-east-1:000000000000:function:apigw-lambda2/invocations --passthrough-behavior WHEN_NO_MATCH

aws --endpoint-url=http://localhost:4566 apigateway create-deployment --rest-api-id $apiID --stage-name suggestions

echo "http://localhost:4566/restapis/$apiID/test/_user_request_/metrics"
echo "http://localhost:4566/restapis/$apiID/test/_user_request_/suggestions"

$sourceDir = ".\gitradar-datalake\"
$s3Bucket = "s3://gitradar-datalake"
$endpointUrl = "http://localhost:4566"

foreach ($file in Get-ChildItem -Path $sourceDir) {
    aws s3 cp "$sourceDir$file" "$s3Bucket" --endpoint-url="$endpointUrl"
    Start-Sleep -Seconds 2
}

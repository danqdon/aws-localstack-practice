import json
import boto3

s3_client = boto3.client('s3', region_name="us-east-1")
dynamodb = boto3.resource('dynamodb')
table_name = 'FilesMetrics'
table = dynamodb.Table(table_name)
desired_key_order = ['FileName', 'Number_of_imports', 'Lines_of_code', 'Lines_of_comments', 'Percentage_comment',
                     'Number_of_variables', 'Number_of_types', 'Number_of_classes', 'Number_of_interfaces', 'Number_of_methods',
                     'Number_of_enumerate', 'Number_of_abstract_types', 'Abstractness', 'Imported_libraries', 'Class_names',
                     'Interface_names', 'Variable_names', 'Enumerate_names']


def upload_log(event, context):
    log_data = {
        'event': event,
        'context': str(context)
    }
    log_file_name = f"log-{context.aws_request_id}.json"
    s3_client.put_object(
        Bucket='gitradar-lambda-logs',
        Key=log_file_name,
        Body=json.dumps(log_data)
    )

def lambda_handler(event, context):
    upload_log(event, context)
    response = table.scan()
    items = response.get('Items', [])
    sorted_data_list = [reorder_keys(entry, desired_key_order) for entry in items]
    return {
        'statusCode': 200,
        'body': json.dumps(sorted_data_list)
    }
def reorder_keys(dictionary, key_order):
    return {key: dictionary[key] for key in key_order if key in dictionary}






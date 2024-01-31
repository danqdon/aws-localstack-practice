import logging
import json
import boto3
import javalang

logger = logging.getLogger()
logger.setLevel(logging.INFO)

s3_client = boto3.client('s3')

dynamodb = boto3.resource('dynamodb')
table_name = 'FilesMetrics'
table = dynamodb.Table(table_name)

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

    s3_event = event['Records'][0]['s3']

    bucket_name = s3_event['bucket']['name']
    file_key = s3_event['object']['key']

    file_contents = read_file_from_s3(bucket_name, file_key)

    features = extract_features(file_contents)
    features["FileName"] = file_key
    put_item_response = store_data_in_dynamodb(features)
    return {
        'statusCode': 200,
        'body': features,
        'put_item_response': put_item_response
    }

def extract_features(java_code):
    tree = javalang.parse.parse(java_code)

    lines_of_code = len(java_code.split('\n'))
    lines_of_comments = sum(1 for line in java_code.splitlines() if line.strip().startswith('//') or line.strip().startswith('/*') or line.strip().startswith('*') or line.strip().startswith('*/'))
    num_imports = 0
    num_types = 0
    num_methods = 0
    num_variables = 0
    num_classes = 0
    num_interfaces = 0
    num_enum = 0
    num_abstract_types = 0
    class_names = []
    interface_names = []
    variable_names = []
    enum_names = []
    imported_libraries = []

    for path, node in tree:

        if isinstance(node, javalang.tree.Import):
            imported_libraries.append(node.path)
            num_imports += 1

        if isinstance(node, javalang.tree.TypeDeclaration):
            num_types += 1
            if 'abstract' in node.modifiers:
                num_abstract_types += 1
            if 'classdeclaration' in str(node).split("(")[0].lower():
                class_names.append(node.name)
                num_classes += 1
            if 'interfacedeclaration' in str(node).split("(")[0].lower():
                interface_names.append(node.name)
                num_interfaces += 1
            if 'enumdeclaration' in str(node).split("(")[0].lower():
                enum_names.append(node.name)
                num_enum += 1

        if isinstance(node, javalang.tree.VariableDeclarator):
            num_variables += 1
            variable_names.append(node.name)
        if isinstance(node, javalang.tree.MethodDeclaration):
            num_methods += 1

    percentage_comment = (lines_of_comments / lines_of_code) * 100 if lines_of_code > 0 else 0

    abstractness = num_abstract_types / num_types if num_types > 0 else 0

    return {
        'Number_of_imports': str(num_imports),
        'Lines_of_code': str(lines_of_code),
        'Lines_of_comments': str(lines_of_comments),
        'Percentage_comment': str(percentage_comment),
        'Number_of_types': str(num_types),
        'Number_of_methods': str(num_methods),
        'Number_of_variables': str(num_variables),
        'Number_of_classes': str(num_classes),
        'Number_of_interfaces': str(num_interfaces),
        'Number_of_enumerates': str(num_enum),
        'Number_of_abstract_types': str(num_abstract_types),
        'Abstractness': str(abstractness),
        'Class_names': ", ".join(class_names) if class_names else "",
        'Interface_names': ", ".join(interface_names) if interface_names else "",
        'Variable_names': ", ".join(variable_names) if variable_names else "",
        'Enumerate_names': ", ".join(enum_names) if enum_names else "",
        'Imported_libraries': ", ".join(imported_libraries) if imported_libraries else ""
    }
def read_file_from_s3(bucket_name, file_key):
    try:
        response = s3_client.get_object(Bucket=bucket_name, Key=file_key)
        file_contents = response['Body'].read().decode('utf-8')
        return file_contents
    except Exception as e:
        logging.error(f"Error reading file from S3: {e}")
        raise

def store_data_in_dynamodb(output_data):
    try:
        response = table.put_item(Item=output_data)
        return response
    except Exception as e:
        print(f"Error storing data in DynamoDB: {e}")
        raise



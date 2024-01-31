import logging
import string
import boto3
import json

logger = logging.getLogger()
logger.setLevel(logging.INFO)

s3_client = boto3.client('s3', region_name="us-east-1")

dynamodb = boto3.resource('dynamodb', region_name="us-east-1")
table_name = 'FilesTokens'
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
    logging.info(f"I am here {file_key}")
    file_contents = read_file_from_s3(bucket_name, file_key)

    tokens = Tokenize(file_contents)

    output_data = {"FileName": file_key, "Tokens": ",".join(tokens)}

    put_item_response = store_data_in_dynamodb(output_data)
    return {
        'statusCode': 200,
        'body': tokens,
        'put_item_response': put_item_response
    }

def read_file_from_s3(bucket_name, file_key):
    try:
        response = s3_client.get_object(Bucket=bucket_name, Key=file_key)
        file_contents = response['Body'].read().decode('utf-8')
        return file_contents
    except Exception as e:
        logging.error(f"Error reading file from S3: {e}")
        raise

def Tokenize(file_contents):
    java_keywords = {"abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
                     "continue", "default", "do", "double", "else", "enum", "extends", "false", "final", "finally",
                     "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native",
                     "new", "null", "package", "private", "protected", "public", "return", "short", "static", "strictfp",
                     "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try", "void",
                     "volatile", "while", "superclass", "method", "overrides", "string", "sets", "systemoutprintln"}

    with open("stopwords.txt", 'r', encoding='utf-8') as file:
        stopwords = set(file.read().splitlines())

    words = file_contents.split()
    table = str.maketrans(string.punctuation, " " * len(string.punctuation))
    cleaned_words = [word.translate(table).strip() for word in words]
    tokens = [word.lower() for word in cleaned_words if word.isalpha() and word.lower() not in java_keywords and word.lower() not in stopwords]
    tokens = list(set(tokens))
    return tokens

def store_data_in_dynamodb(output_data):
    try:
        response = table.put_item(Item=output_data)
        return response
    except Exception as e:
        print(f"Error storing data in DynamoDB: {e}")
        raise

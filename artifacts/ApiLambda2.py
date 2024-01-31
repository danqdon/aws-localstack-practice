import json
import random
from collections import defaultdict
import boto3

s3_client = boto3.client('s3', region_name="us-east-1")
dynamodb = boto3.resource('dynamodb')
table_name = 'FilesTokens'
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

    api_response = [generate_words(items[e]["Tokens"].split(","), items[e]["FileName"]) for e in range(len(items))]

    all_tokens = [element for sublist in (items[e]["Tokens"].split(",") for e in range(len(items))) for element in
                  sublist]
    ngram_model = build_ngram_model(all_tokens, n=10)
    new_words = [generate_word(ngram_model, n=3, max_length=10) for _ in range(5)]
    generated_words = {"Filename": "All_documents", "Generated_words": ", ".join(new_words)}
    api_response.append(generated_words)
    return {
        'statusCode': 200,
        'body': json.dumps(api_response)
    }


def build_ngram_model(words, n=2):
    model = defaultdict(list)

    for word in words:
        for i in range(len(word) - n + 1):
            ngram = word[i:i + n]
            next_char = word[i + n] if i + n < len(word) else ''
            model[ngram].append(next_char)

    return model

def generate_word(model, n=2, max_length=10):
    current_ngram = random.choice(list(model.keys()))
    generated_word = current_ngram

    while len(generated_word) < max_length:
        next_char = random.choice(model.get(current_ngram, ['']))
        if not next_char:
            break
        generated_word += next_char
        current_ngram = generated_word[-n:]

    return generated_word

def generate_words(tokens, filename):
    ngram_size = min(10, len(tokens))
    ngram_model = build_ngram_model(tokens, n=ngram_size)
    new_words = [generate_word(ngram_model, n=3, max_length=10) for _ in range(5)]

    return {"Filename": filename, "Generated_words": ", ".join(new_words)}





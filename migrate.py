import boto3
import argparse

parser = argparse.ArgumentParser()
parser.add_argument("--stage")

args = parser.parse_args()

lambda_client = boto3.client('lambda')

lambda_client.invoke(FunctionName='dp-core-db-migrator-' + args.stage + '-migrate', InvocationType='Event')

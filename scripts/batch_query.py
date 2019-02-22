import argparse
import csv
import json
from typing import Dict, Any
from urllib import request

import xlrd


def main():
    # add the arguments for the command line interface
    parser = argparse.ArgumentParser(description='Cellosaurus STR Similarity Search Tool')
    parser.add_argument(dest='Input file path', type=str, help='[str]')
    parser.add_argument(dest='Output file path', type=str, help='[str]')
    parser.add_argument('--algorithm', dest='Algorithm', type=int, help='[1|2|3]')
    parser.add_argument('--scoringMode', dest='Scoring mode', type=int, help='[1|2|3]')
    parser.add_argument('--includeAmelogenin', dest='Include Amelogenin', type=bool, help='[bool]')
    parser.add_argument('--scoreFilter', dest='Score filter', type=int, help='[int]')
    parser.add_argument('--maxResults', dest='Max results', type=int, help='[int]')
    parser.add_argument('--outputFormat', dest='Output format', type=str, help='[json|csv]')

    # parse the provided arguments
    args: Dict[str, Any] = vars(parser.parse_args())

    input_path = args.get('Input file path')
    output_path = args.get('Output file path')

    # format the output path so that the output file has the correct extension
    if args.get('Output format') == 'csv' and not output_path.endswith('.zip'):
        output_path += '.zip'
    if not output_path.endswith('.zip') and not output_path.endswith('.json'):
        output_path += '.json'

    # read the values contained in the input file
    table_list = read_table(input_path)

    # construct the queries by adding them in a list as dictionaries
    queries = []
    for i in range(len(table_list)):
        query = {}

        # add all the values read from the input file to the query
        for table_tuple in table_list[i].items():
            query[table_tuple[0]] = table_tuple[1]

        if args.get('Algorithm'):
            query['algorithm'] = args.get('Algorithm')
        if args.get('Scoring mode'):
            query['scoringMode'] = args.get('Scoring mode')
        if args.get('Score filter'):
            query['scoreFilter'] = args.get('Score filter')
        if args.get('Max results'):
            query['maxResults'] = args.get('Max results')
        if args.get('Include Amelogenin'):
            query['includeAmelogenin'] = args.get('Include Amelogenin')
        if args.get('Output format'):
            query['outputFormat'] = args.get('Output format')

        queries.append(query)

    call_api(output_path, queries)


def read_table(path):
    queries = []

    # read the file with xlrd if it is an Excel file
    if path.endswith('.xls') or path.endswith('.xlsx'):
        with xlrd.open_workbook(path) as book:
            sheet = book.sheet_by_index(0)

            for x in range(1, sheet.nrows):
                # consider the first column as the sample name
                query = {"description": sheet.cell(x, 0).value}

                # add the key/value pairs in a dictionary
                for y in range(1, sheet.ncols):
                    key = sheet.cell(0, y).value
                    value = sheet.cell(x, y).value
                    query[key] = value

                queries.append(query)

    # read the file the file as csv if it is a csv file
    elif path.endswith('.csv'):
        rows = []

        with open(path, 'r') as infile:
            rd = csv.reader(infile, delimiter=',', quotechar='"')

            for row in rd:
                rows.append(row)

        for x in range(1, len(rows)):
            # consider the first column as the sample name
            query = {"description": rows[x][0]}

            # add the key/value pairs in a dictionary
            for y in range(1, len(rows[0])):
                key = rows[0][y]
                value = rows[x][y]
                query[key] = value

            queries.append(query)

    # raise an exception if the input file is not in a correct format
    else:
        raise ValueError('Unknown input file extension: .' + path.split('.')[1])

    return queries


def call_api(output_path, queries):
    # format the queries as a json string
    json_string = json.dumps(queries)
    # encode the json string as bytes
    json_bytes = json_string.encode('utf-8')

    # call the API as a POST request
    req = request.Request('http://129.194.71.205:8080/str-sst/api/batch')
    req.add_header('Content-Type', 'application/json; charset=utf-8')
    req.add_header('Content-Length', len(json_bytes))

    # read the API response and write it in the output file
    response = request.urlopen(req, json_bytes)
    with open(output_path, "wb") as outfile:
        outfile.write(response.read())


if __name__ == '__main__':
    main()

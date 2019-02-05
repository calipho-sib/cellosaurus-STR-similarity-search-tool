import argparse
import csv
import os
import tempfile
from typing import Dict, Any

import xlrd
import zipfile

from urllib import request

try:
    import zlib

    compression = zipfile.ZIP_DEFLATED
except (ImportError, AttributeError):
    compression = zipfile.ZIP_STORED


def main():
    parser = argparse.ArgumentParser(description='Cellosaurus STR Similarity Search Tool batch queries')
    parser.add_argument(dest='Input path', type=str, help='Path of the input table file [str]')
    parser.add_argument(dest='Output path', type=str, help='Path of the output zip file [str]')
    parser.add_argument('--scoring', dest='Scoring', type=int, help='Scoring algorithm [1|2|3]')
    parser.add_argument('--mode', dest='Mode', type=int, help='Computation mode [1|2]')
    parser.add_argument('--score', dest='Score filter', type=int, help='Score filter [int]')
    parser.add_argument('--size', dest='Size filter', type=int, help='Size filter [int]')
    parser.add_argument('--amel', dest='Include Amelogenin', type=bool, help='Include Amelognin [bool]')

    args: Dict[str, Any] = vars(parser.parse_args())
    queries = read_table(args.get('Input path'))

    opt_args = ['format=csv']
    if args.get('Scoring'):
        opt_args.append('scoring=' + str(args.get('Scoring')))
    if args.get('Mode'):
        opt_args.append('mode=' + str(args.get('Mode')))
    if args.get('Score filter'):
        opt_args.append('filter=' + str(args.get('Score filter')))
    if args.get('Size filter'):
        opt_args.append('scoring=' + str(args.get('Size filter')))
    if args.get('Include Amelogenin'):
        opt_args.append('includeAmelogenin=' + str(args.get('Include Amelogenin')).lower())

    full_queries = []
    for query in queries:
        full_queries.append(query + '&' + '&'.join(opt_args))

    call_api(args.get('Output path'), full_queries)


def read_table(path):
    queries = []

    if path.endswith('.xls') or path.endswith('.xlsx'):
        with xlrd.open_workbook(path) as book:
            sheet = book.sheet_by_index(0)
            for x in range(1, sheet.nrows):
                query = ["description=" + sheet.cell(x, 0).value.replace(' ', '_')]

                for y in range(3, sheet.ncols):
                    key = sheet.cell(0, y).value.replace(' ', '_')
                    value = sheet.cell(x, y).value.replace(' ', '')

                    if key.lower() == 'amel' or key.lower() == 'amelogenin':
                        key = 'Amelogenin'

                    query.append(key + '=' + value.upper())

                queries.append('&'.join(query))

    elif path.endswith('.csv'):
        matrix = []

        with open(path, 'r') as infile:
            rd = csv.reader(infile, delimiter=',', quotechar='"')

            for row in rd:
                matrix.append(row)

        for x in range(1, len(matrix)):
            query = ["description=" + matrix[x][0].replace(' ', '_')]

            for y in range(3, len(matrix[0])):
                key = matrix[0][y].replace(' ', '_')
                value = matrix[x][y].replace(' ', '')

                if key.lower() == 'amel' or key.lower() == 'amelogenin':
                    key = 'Amelogenin'

                query.append(key + '=' + value.upper())

            queries.append('&'.join(query))

    else:
        raise ValueError('Unknown input file extension: .' + path.split('.')[1])

    return queries


def call_api(output_path, queries):
    if os.path.isdir(output_path):
        output_path = os.path.join(output_path, 'Cellosaurus_STR_Results.zip')

    with tempfile.TemporaryDirectory() as tempfolder:
        with zipfile.ZipFile(output_path, 'w') as outfile:
            i = 0
            for query in queries:
                i += 1

                filepath = os.path.join(tempfolder, 'Cellosaurus_STR_Results_' + str(i) + '.csv')

                with request.urlopen('http://localhost:8080/str-sst/api/query?' + query) as response:
                    with open(filepath, 'wb') as temp:
                        temp.write(response.read())

                outfile.write(filepath, os.path.basename(filepath), compress_type=compression)


if __name__ == '__main__':
    main()

from urllib import request
from urllib.parse import urlencode

API_URL = 'http://129.194.71.205:8080/str-sst/api/'


def main():
    output_format = "csv"
    output_path = "example.csv"

    # format the output path so that the output file has the correct extension
    if output_format == 'csv' and not output_path.endswith('.csv'):
        output_path += '.csv'
    elif output_format == 'json' and not output_path.endswith('.json'):
        output_path += '.json'

    # construct the query by adding the parameters in a tuple of key/values
    query = ('Amelogenin', 'X'),\
            ('CSF1PO', '13,14'),\
            ('D5S818', '13'),\
            ('D7S820', '8,9'),\
            ('D13S317', '12'),\
            ('FGA', '24'),\
            ('TH01', '8'),\
            ('TPOX', '11'),\
            ('vWA', '16'),\
            ('algorithm', 1),\
            ('scoringMode', 1),\
            ('scoreFilter', 40),\
            ('maxResults', 10),\
            ('outputFormat', output_format),\
            ('includeAmelogenin', False)

    call_api(output_path, query)


def call_api(output_path, query):
    # format the query as url parameters
    url_parameters = '?' + urlencode(query)

    # call the API as a GET request
    req = request.Request(API_URL + 'query' + url_parameters)

    # read the API response and write it in the output file
    response = request.urlopen(req)
    with open(output_path, 'wb') as outfile:
        outfile.write(response.read())


if __name__ == '__main__':
    main()

# EPAM Gen AI Bootcamp Coursework - Module 4

This module focuses on project organization. We use [httpyac](https://www.npmjs.com/package/httpyac) to test our API.

## Installation

Before running the tests, you need to install the project dependencies. You can do this by running the following command in your terminal:

```bash
npm install
```

## API Tests

We have a collection of API tests in the `/api/tests` directory. These tests cover various scenarios for our API endpoints.

### Running the tests

You can run the tests using the following npm scripts:

- `npm run test`: This will run all the tests and print the results in the console.
- `npm run api-test`: This will run all the tests and generate a `report.xml` file with the results in the JUnit format.

### Test Cases

We have test cases for the following API endpoints:

- Calculate age from date
- Get Bing Search URL
- Get Wikipedia URL

Each test case sends a request to the corresponding API endpoint and checks the response status code, headers, and body.

## Dependencies

- [httpyac](https://www.npmjs.com/package/httpyac): HTTP client for Visual Studio Code with a focus on testing RESTful APIs.

## Author

[Jegors Čemisovs](mailto:jegors_cemisovs@epam.com)

## License

This project is licensed under the MIT License.

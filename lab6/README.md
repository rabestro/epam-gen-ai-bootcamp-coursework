# GenAI Bootcamp - Lab 6: Retrieval-Augmented Generation

## Prerequisites
* Requires JDK17
* Requires Docker

## Application Setup

`docker-compose up`
* test with opening pgadmin `http://localhost:5050. Login is `pgadmin4@pgadmin.org` and password is `admin`.

* text-embedding-ada-002 is for vectors.
* LLM is for generation reports.

## API
### POST /chat

This endpoint is used to send a chat message and receive a response.

#### Request Body

- input (string, required): The input message for the chat.


#### Response

The response body is a JSON object with the following schema:

``` json
{
  "response": {
    "type": "string"
  }
}

 ```

The response contains the chatbot's response to the input message.

### POST /chat

This endpoint is used to send a chat message and receive a response.

#### Request Body

- input (string, required): The input message for the chat.


#### Response

The response body is a JSON object with the following schema:

``` json
{
  "response": {
    "type": "string"
  }
}

 ```

The response contains the chatbot's response to the input message.

### POST /admin/documents/upload
endpoint is used to upload documents to the admin system. The request should be sent as a form-data with a key "file" containing the document to be uploaded.

### Request Body

- `file` (file): The document file to be uploaded.


### Response

The response to this request is in JSON format with a status code of 201 (Created). Since the user wants to document the response as a JSON schema, it's important to note that the response is not in JSON format. If the response is expected to be in JSON format, the API should be updated accordingly.



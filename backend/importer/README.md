Importer Micro services
==

## Example Work flow 
1. The User selects a Story (e.g. New Contract)
2. The User enters example customer data in a table or import the data from a CSV File
3. The User clicks "Export" and enters a name for the test. 
4. The API takes this data in a form of a table and formats it according to the JSON Schema
5. The API makes A POST Request to the Importer with the JSON (POST http://localhost:8082/exporter)
6. The Importer takes the JSON, checks if it's formatted correctly and if all the necessary fields are available (storyType and testData)
7. The Importer creates individual JSON chunks from the given JSON in order to export one entry (one row from the CSV) at a time to the Database
8. The Importer makes a POST Request to the Database in order to save the JSON chunks (POST http://localhost:8082/database/newContract)

## Example Data for the given Work flow: 

Front End Table/CSV: 

| ID |  NAME 		   | AGE  | ADDRESS  |
|----|:----------------|:----:|---------:|
|1   | Max Mustermann  | 20   | Aachen   |
|2   | John Smith 	   | 30   | Berlin   |

Note: If the data is imported from a CSV File, and there are no IDs for the rows, the Front End or the API have to add one

API Formats to: 

```json
{
	"storyType": "newContract",
	"testNameExport": "newContractTest",
	"testData": [
		{
			"exampleCustomerID": "1",
			"customerData": {"name": "Max Mustermann", "age": "20", "address": "Aachen"}
		},

		{
			"exampleCustomerID": "2",
			"customerData": {"name": "John Smith", "age": "30", "address": "Berlin"}
		}
	
	]
}
```

Importer receives that and creates the following two JSON chunks: 
```json
{
	"storyType": "newContract",
	"testNameExport": "newContractTest",
	"exampleCustomerID": "1",
	"name": "Max Mustermann",
	"age": "20", 
	"address": "Aachen"
}
```

```json
{
	"storyType": "newContract",
	"testNameExport": "newContractTest",
	"exampleCustomerID": "1",
	"name": "John Smith",
	"age": "30", 
	"address": "Berlin"
}
```

Importer exports the given chunks one by one to the Database
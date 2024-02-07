# Mini Rest Project 
The application was built using Java 17, Spring Boot, JPA, an H2 in-memory database and OpenCSV used for reading the CSV file.

## Loading Customers from CSV
The `CustomerDataLoader` class implements the `CommandLineRunner` interface so runs on startup after the application context is loaded. 
This does 2 things:
* Read a list of customers from a CSV.
* Call a POST API to save each customer to the database.

#### CSV Format & Validation
The csv filepath can be supplied as a commandline argument. If none is supplied, the
default is `customers.csv` which needs to be present in the root of the application.
Each CSV record must the following headers: `Customer Ref, Customer Name, Address Line 1, Address Line 2, Town, County, Country, Postcode` and each row much contain 8 fields.    
Customer reference and customer name are required fields, the 6 address fields are all optional. Further address validation could be added if an address was deemed required.
The customer reference field only accepts alphanumeric characters. As the format of the customer reference wasn't known, no max or min lengths were specified. 

#### Saving Customers to Database 
Spring Webclient was used to invoke the POST API for each valid customer record asynchronously. If the CSV had a large number of records, this would be more efficient than calling the API for each record on the same thread.

#### Error Handling
When parsing the CSV, if a customer fails validation the error is logged and the next customer is processed, allowing all valid records to be processed.
Similarly, any errors when calling the POST API are logged and the next customer is processed.
The error handling could be enhanced to add error notifications rather than just log the errors.

## REST APIs
The `CustomerController` class contains the REST endpoints for the customer resource.
#### POST: /customers
Saves a customer to the database. If the customer is saved successfully, a 201 response is returned.
If the customer reference already exists in the database, a 400 response is returned.
#### GET: /customers/{ref}    
Retrieves a customer by their customer ref. If the customer is not found, a 404 response is returned.



# Sludgate Brass Band

Team 20 Group Project

## Table of Contents
- [Run Locally](#run-locally)
- [Running Tests](#running-tests)
- [Setting Up](#setting-up)
- [Support](#support)
- [Authors](#authors)

## Run Locally

Go to the project directory

```bash
  cd <the-project>
```

Start the server

```bash
  gradle bootRun
```

Open the application in the browser at [http://localhost:8080/login](http://localhost:8080/login)


To terminate the application, use the following command in the terminal
```
  Ctrl + c 
```
When prompted for confirmation, enter `Y`.
## Running Tests

Run tests with the following command

```bash
  gradle clean test -info
```

After running the tests, navigate to the test result directory

```
  cd build/result/tests/test
```

Open the `index.html` file to view the test results.
## Setting Up

### Adding the Director to the Database ###
- Start the server.
- Register as a normal user.
- Open the MySQL console and execute the following command to assign the "DIRECTOR" role to the user you just registered.
  
  ```mysql
    INSERT INTO user_roles (user_id, roles) VALUES (1, DIRECTOR);
  ```

### Adding Bands to the Database ###
- Open the MySQL console.
- Execute the following commands to add bands to the database.

  ```mysql
    INSERT INTO bands (id, name) VALUES (1, 'Senior');
    INSERT INTO bands (id, name) VALUES (2, 'Training');
  ```
## Support

***Please use the existing database if the new database doesn't work.***

Credentials:

- Datasource URL : jdbc:mysql://stusql.dcs.shef.ac.uk/team020
- Username : team020
- Password : Yoh1nae2e

The database is pre-configured with the director account and the two bands.

Pre-Configured Director: 

- Email : director@director.com
- Password : director

## Authors

- Michael Udo


# Library Management System Test Documentation

## Table of Contents
1. [Introduction](#introduction)
2. [Test Coverage](#test-coverage)
3. [Unit Tests](#unit-tests)
    - [BookTest](#booktest)
    - [BookParameterizedTest](#bookparameterizedtest)
    - [BookManagementExceptionTest](#bookmanagementexceptiontest)
    - [TransactionManagementTest](#transactionmanagementtest)
4. [Integration Tests](#integration-tests)
    - [TransactionIntegrationTest](#transactionintegrationtest)
5. [Regression Tests](#regression-tests)
    - [LibrarySystemRegressionTest](#librarysystemregressiontest)
6. [Test Setup and Teardown](#test-setup-and-teardown)
7. [Mocking and Stubbing](#mocking-and-stubbing)
8. [Exception Handling Tests](#exception-handling-tests)
9. [Parameterized Testing](#parameterized-testing)
10. [Running the Tests](#running-the-tests)
11. [Continuous Integration](#continuous-integration)

## Introduction

This document provides a comprehensive overview of the test suite for the Library Management System. The test suite is designed to ensure the reliability, correctness, and robustness of the system through a combination of unit tests, integration tests, parameterized tests, and regression tests.

## Test Coverage

The current test suite achieves 80% code coverage as measured by Jacoco. This coverage encompasses critical paths and functionalities of the Library Management System, including book management, patron management, and transaction processing.

## Unit Tests

### BookTest

Tests the basic functionality of the `Book` class.

Test methods:
- `testBookConstructorAndGetters()`:
    - Creates a book with ID 1, title "Test Book", author "Test Author", ISBN "1234567890", availability true, and count 5.
    - Validates that all getter methods return the correct values.

- `testSetters()`:
    - Creates a book and uses setters to modify its properties.
    - Verifies that the changes are reflected correctly.

- `testCheckOut()`:
    - Creates a book with 2 copies available.
    - Checks out the book twice and verifies the count and availability.
    - Attempts to check out when no copies are available and verifies it fails.

- `testReturnBook()`:
    - Creates a book with 0 copies available and not available.
    - Returns the book and verifies the count increases and it becomes available.

### BookParameterizedTest

Utilizes parameterized testing to efficiently test the `Book` class with multiple scenarios.

Test methods:
- `testBookConstructorWithDifferentInputs(int id, String title, String author, String isbn, boolean available, int count)`:
    - Tests book creation with various inputs:
        1. ID 1, "Test Book 1", "Author 1", "ISBN1", true, 5
        2. ID 2, "Test Book 2", "Author 2", "ISBN2", false, 0
        3. ID 3, "Test Book 3", "Author 3", "ISBN3", true, 10
    - Verifies that all properties are set correctly for each case.

- `testCheckOutWithDifferentScenarios(int initialCount, boolean initialAvailability, boolean expectedCheckOutResult, int expectedFinalCount, boolean expectedFinalAvailability)`:
    - Tests the check-out process under different conditions:
        1. 5 copies, available -> successful checkout
        2. 1 copy, available -> successful checkout, becomes unavailable
        3. 0 copies, unavailable -> failed checkout

### BookManagementExceptionTest

Tests exception handling in the `BookManagement` class.

Test method:
- `testGetBookByIdNotFound()`:
    - Mocks a database query that returns no results.
    - Verifies that `getBookById()` returns null when a book is not found.

### TransactionManagementTest

Unit tests for the `TransactionManagement` class using mocking to isolate the class from its dependencies.

Test methods:
- `testSearchTransactions()`:
    - Mocks database query returning two transactions.
    - Verifies that the search method returns the correct number of transactions and that the query is executed with the correct parameters.

- `testSearchTransactionsNoResults()`:
    - Mocks an empty result set.
    - Verifies that the search method returns an empty list.

- `testGetTransactionsByPatronId()`:
    - Mocks database query returning two transactions for a specific patron.
    - Verifies that the method returns the correct number of transactions and that the query is executed with the correct patron ID.

- `testGetTransactionsByPatronIdNoResults()`:
    - Mocks an empty result set for a patron.
    - Verifies that the method returns an empty list.

- `testSearchTransactionsSQLException()`:
    - Mocks a SQLException during query execution.
    - Verifies that the exception is propagated correctly.

- `testGetTransactionsByPatronIdSQLException()`:
    - Mocks a SQLException during query execution for patron transactions.
    - Verifies that the exception is propagated correctly.

## Integration Tests

### TransactionIntegrationTest

Tests the integration of various components in the transaction process.

Test method:
- `testBorrowAndReturnBook()`:
    - Adds a new book and patron to the system.
    - Borrows the book for the patron.
    - Verifies the transaction is recorded correctly.
    - Returns the book.
    - Verifies the book becomes available again.
    - Cleans up by deleting all created entities.

## Regression Tests

### LibrarySystemRegressionTest

Provides a comprehensive test of the entire library system workflow to catch any regressions.

Test method:
- `testFullLibraryProcess()`:
    1. Adds a new book "Regression Test Book" to the system.
    2. Verifies the book was added correctly.
    3. Adds a new patron "Regression Test Patron".
    4. Verifies the patron was added correctly.
    5. Borrows the book for the patron.
    6. Returns the book.
    7. Verifies the book is available again.
    8. Updates the book's author information.
    9. Verifies the update was successful.
    10. Cleans up by deleting all transactions, the book, and the patron.
    11. Verifies all deletions were successful.

## Test Setup and Teardown

- `@BeforeAll`: Establishes a database connection for integration tests.
- `@BeforeEach`: Initializes mock objects and sets up test fixtures.
- `@AfterAll`: Releases the database connection after all tests are complete.

## Mocking and Stubbing

- Mockito is used extensively in `TransactionManagementTest` to mock `Connection`, `PreparedStatement`, and `ResultSet` objects.
- This allows for testing the `TransactionManagement` class in isolation from the actual database.

## Exception Handling Tests

- `testSearchTransactionsSQLException()` and `testGetTransactionsByPatronIdSQLException()` in `TransactionManagementTest` specifically test the handling of SQLExceptions.
- `LibrarySystemRegressionTest` includes try-catch blocks to handle and log potential SQLExceptions during the test process.

## Parameterized Testing

`BookParameterizedTest` demonstrates the use of JUnit 5's `@ParameterizedTest` and `@CsvSource` to test multiple scenarios efficiently.

## Running the Tests

To run the tests, use the following command:
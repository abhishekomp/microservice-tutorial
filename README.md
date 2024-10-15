# Microservice Based Bookstore application

This project demonstrates microservice applications interacting with one another including a thymeleaf based frontend application.

## Project Overview

This Spring Boot Microservice application showcases the following features:

- Setting up individual Spring Boot application that talk to each other for Bootstore application.
- Dockerizing the applications using GitHub Actions workflow.
- Running the application in Docker container.
- Usage of RestClient for interaction between different microservice applications.
- Publishing of events to RabbitMQ using Outbox Pattern.
- Implementation of email notification via mailhog.
- Frontend application based on Thymeleaf rendering and usage of AlpineJS for adding functionality on web page.
- Cart functionality for Order placement.

## Prerequisites

Before you begin, ensure you have the following installed:

- Java Development Kit (JDK) 17 or later
- Apache Maven
- Docker client

## Project Structure

The project (multi-module maven project) consists of the following applications:

- `catalog-service`: A Spring boot application that manages the books for the bookstore application.
- `order-service`: A Spring boot application that is used to create and manage Order from the user.
- `notification-service`: A Spring boot application that interacts with RabbitMQ to fetch the events and sends out the email notification to the user.
- `api-gateway`: A Spring boot application that serves as a single point of entry for api usage.
- `bookstore-webapp`: (End User facing application) A Spring boot web mvc application using Thymeleaf for html rendering and AlpineJS for adding functionality to the html page

## Usage

Once the application is running, you can interact access the application using following endpoints:

- Bookstore web application: `http://localhost:8080`

The above endpoint will show the main page for the Bookstore web application along with Cart in the upper right side.

Happy coding!
<p align="center">
  <img src="./documents/bucket_logo.svg" width="600" />
</p>
<p align="center">  
  PLACEHOLDER.<br>
  <a href="#introduction">Introduction</a> | <a href="https://github.com/mahgoh/palletpals-client">Frontend Development</a>
</p>

# PalletPals Webapp

The PalletPals webapp was developed as a part of the IT-Project module of the Business Information Technology study programme at FHNW by Daniel Locher ([@dantheman625](https://github.com/dantheman625)) Tibor Haller ([@972C8](https://github.com/972C8)), and Marco Kaufmann ([@mahgoh](https://github.com/mahgoh)). The app XXXX

The project was developed in two repositories: [palletpals-webapp](https://github.com/972C8/palletpals-webapp) and [palletpals-client](https://github.com/mahgoh/palletpals-client). `palletpals-webapp` contains the main application and the final web application. The frontend was developed in the repository `palletpals-client` - more information can be found in the repository.
The api endpoints were designed with the help of Postman. The API design can be found at: https://documenter.getpostman.com/view/17679206/Uz5AseV9.

## Introduction
TEXT?

The content is structured based on the project milestones, more concretely the three main phases of "defining and documenting the requirements", "technical design", and "implementation".

#### Contents:

- [Requirements](#requirements)
  - [Use Case](#use-case)
  - [Non-Functional Requirements](#non-functional-requirements)
  - [Mockups](#mockups)
- [Design](#design)
  - [Class Diagram](#class-diagram)
  - [Database Design](#database-design)
  - [Sequence Diagram](#sequence-diagram)
  - [Endpoint Design](#endpoint-design)
- [Implementation](#implementation)
  - [Backend Technology](#backend-technology)
  - [Frontend Technology](#frontend-technology)
- [User Guide](#user-guide)
- [Project Management](#project-management)
  - [Roles](#roles)
  - [Milestones](#milestones)

## Requirements

### Use Case

The detailed requirements are found in PalletPals_Phase1_Requirements.pdf at XY.

ADD THE USE CASE OVERVIEWS HERE, BUT NOT THE DETAILED USE CASES TABLES

### Non-Functional Requirements

### Mockups
Mockups was part of phase 1 requirements

## Design

### Class Diagram

![Class Diagram](documents/PalletPals_ClassDiagram_FINAL.drawio.png)

The Class Diagram shall give the reader of this document an overall perspective about the class structure of our application. Mostly, this diagram aims to be self-explanatory, nevertheless we like to point out a few things.

As our goal is to exceed the basic requirements, we plan to develop a more holistic online shop experience. One key issue was to ensure that orders reflect a snapshot of the relevant data, which is important when for example the user changes his address after an order was finalized, or that for example historic order data are preserved correctly. Wehave roughly split our classes into three overall data groups:

- Static Data

  This group includes all regular data classes, such as classes with product and user related data. A user should be able to change his personal data, as well as product data can change over time.

- Session Data

  We pursue to offer a real online shop feeling. With this in mind, we introduced session data to preserve a user’s shopping basket over a certain period. Even if the website will be left, the shopping cart of a user is still saved in our database. Therefore, if this user revisits our website, he or she can flawlessly continue his or her purchase. Session data includes the classes ShoppingSession and CartItem. Once a user submits his order, the session data is used to create an order with the correct information, such as quantities or shipping address.

- Processed Data

  After a user has confirmed the order, the data gathered in his or hers shopping session will be changed to processed data, using the order class. According to the user’s shopping session new objects of the classes Order, AddressItem, ShippingItem, and ProductItem will be created and referenced by the new order object. AddressItem is part of Order as it is only relevant in that context and only used to store address attributes as part of the class. The logic behind this process is, that the user shall have an history of his or her orders. These objects are important to ensure the data integrity as simply working with references incurs the risk of faulty order when some data is updated at a later point. One example that must be handled is that the user updates his shipping address after finalizing an order. Furthermore, the ShoppingSession object with its related CartItems get deleted as soon as the order has been saved to the database, meaning that the user’s shopping cart is emptied.

### Database Design

### Sequence Diagram

### Endpoint Design

The Postman API Platform was used for the endpoint design and during the implementation of the backend. Using Postman allowed us to create the API collaboratively and efficiently thanks to a synchronized workflow. Furthermore, Postman also allowed us to create the API documentation through an out-of-the-box, user-friendly, web-view of the API.

Please check out our endpoint design at https://documenter.getpostman.com/view/17679206/Uz5AseV9 for a user-friendly web-view of the API.

In addition, the overview of the available api requests is found in the [documents](https://github.com/972C8/palletpals-webapp/tree/main/documents) folder to ensure availability of the resource in the future.

## Implementation

### Backend Technology

The backend was initially based on a fork of https://github.com/DigiPR/acrm-webapp. The following excerpt is copied from acrm-webapp and explains the main project dependencies:

#### Dependencies according to the ACRM fork:

This Web application is relying on [Spring Boot](https://projects.spring.io/spring-boot) and the following dependencies:

- [Spring Boot](https://projects.spring.io/spring-boot)
- [Spring Web](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html)
- [Spring Data](https://projects.spring.io/spring-data)
- [Java Persistence API (JPA)](http://www.oracle.com/technetwork/java/javaee/tech/persistence-jsp-140049.html)
- [H2 Database Engine](https://www.h2database.com)
- [PostgreSQL](https://www.postgresql.org)

To bootstrap the application, the [Spring Initializr](https://start.spring.io/) has been used.

### Backend Implementation

The following documentation shall highlight certain key functionalities implemented in the backend.

#### Support of Product Images (using Inheritance)

To improve the user's experience, product images can be created and added to products. One product can have zero or multiple product images.

The class ProductImage extends AbstractImage, which holds the main attributes relevant to images (such as fileName, fileType and fileUrl). Using this approach, future extension of the webapp to support other image types is easily supported.

##### Single table inheritance and discriminator
The image implementation uses single table inheritance and a discriminator column.
This effectively means that only a single table is created in the database (although there are more classes) and that the discriminator is used to determine which class the particular row belongs to.
More information is found at https://en.wikibooks.org/wiki/Java_Persistence/Inheritance#Single_Table_Inheritance

##### Storing of Images
Images are stored in the directory "/uploads" and a database entry is created for the ProductImage, which holds the relevant information (fileName, fileType, fileUrl). When a GET request is sent, the entry is retrieved from the database and the image is taken from the file system using this information.

In a real world, the service could be further improved by storing images directly in an external cloud storage instead of in the project directory.

#### ShoppingSession

#### UserOrder

#### DAN -> Warehouse/serviceprovider/calculation?

### Frontend

## User Guide
Give an overview of the webapp

## Project Management

### Roles

- Backend: Daniel Locher & Tibor Haller
- Frontend: Marco Kaufmann
- Conceptual: All

Note that some overlap between frontend and backend responsibilities existed.

### Milestones

- **Phase 1**: Define and document requirements
- **Phase 2**: Technical Design
- **Phase 3**: Implementation

#### License

- [Apache License, Version 2.0](blob/master/LICENSE)\*\*\*\*

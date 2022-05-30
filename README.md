<p align="center">
  <img src="./documents/bucket_logo.svg" width="600" />
</p>
<p align="center">  
  PLACEHOLDER.<br>
  <a href="#introduction">Introduction</a> | <a href="https://github.com/mahgoh/palletpals-client">Frontend Development</a>
</p>

# PalletPals Webapp

The PalletPals webapp was developed as a part of the IT-Project module of the Business Information Technology study programme at FHNW by Daniel Locher ([@dantheman625](https://github.com/dantheman625)) Tibor Haller ([@972C8](https://github.com/972C8)), and Marco Kaufmann ([@mahgoh](https://github.com/mahgoh)). The app XXXX

The project was developed in two repositories: [palletpals-webapp](https://github.com/972C8/palletpals-webapp) and [palletpals-client](https://github.com/mahgoh/palletpals-client). `bucket-webapp` contains the main application and the final web application. The frontend was developed in the repository `palletpals-client` - more information can be found in the repository.

## Introduction
TEXT?

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
  
  After a user has confirmed the order, the data gathered in his or hers shopping session will be changed to processed data, using the order class. According to the user’s shopping session new objects of the classes Order, AddressItem, ShippingItem, and ProductItem will be created and referenced by the new order object.AddressItem is an innerclass of Order as it is only relevant in that context and only used to store address attributes as part of the class. The logic behind this process is, that the user shall have an history of his or her orders. These objects are important to ensure the data integrity as simply working with references incurs the risk of faulty order when some data is updated at a later point.One example that must be handled is that the user updates his shipping address after finalizingan order.Furthermore, the ShoppingSession object with its related CartItems get deleted as soon as the order has been saved to the database, meaning that the user’s shopping cart is emptied.

### Database Design

### Sequence Diagram

### Endpoint Design

The Postman API Platform was used for the endpoint design and during the implementation of the backend. Using Postman allowed us to create the API collaboratively and efficiently thanks to a synchronized workflow. Furthermore, Postman also allowed us to create the API documentation through an out-of-the-box, user-friendly, web-view of the API.

Please check out our endpoint design at https://documenter.getpostman.com/view/17679206/Uz5AseV9 for a user-friendly web-view of the API.

In addition, the overview of the available api requests is attached below as an image to ensure availability of the resource in the future.

![Postman Endpoint Design](documents/PalletPals_Postman_RequestsOverview.png)

## Implementation

### Backend

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

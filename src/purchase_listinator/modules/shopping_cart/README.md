# Shopping Cart

## Purpose
The Shopping Cart Module is designed to efficiently handle cart events that impact one or more shopping sessions within a purchase list. It effortlessly manages a wide array of events, including adding new items or categories, ensuring a seamless and fluid cart management experience throughout the entire life cycle of the global cart event.

## Abstraction
This module simplifies the management of shopping cart events, which may originate from various sources, including customer interactions such as changes to the purchase list and shopping activity. Additionally, it handles price suggestions generated internally by the shopping cart module.

## Features
- Seamless Cart Management: The module ensures seamless shopping cart events management by efficiently handling and holding cart events for the purchase list and the shopping sessions linked with it.
- Global Cart Event Management: The global cart can manage all the events efficiently, ensuring accurate tracking and coordination of its impacts on the shopping sessions.
- Shopping Responsiveness: The module reacts to shopping orders. When a shopping session is opened, then this module creates a shopping cart if needed, and when the shopping session is closed and there is no other shopping linked to the purchase list, the cart is closed.
- Price Suggestion integration: The module communicates with the price suggestion module, requesting the item prices when the first shopping session the created for the purchase list.
- Cart Provision: Can provide the purchase list at the moment the first shopping session opened and all the subsequent events that happened.
- Shopping Cart Closed Events: Every time a shopping session is closed, this module provides all the events that happened while the session was opened in an event published to the message queue.

## Architecture Overview

### Components
- Redis:
  - The system efficiently retains the purchase list state from the initial shopping session's opening until the conclusion of the last shopping session, ensuring comprehensive data retention throughout the entire shopping process.
  - Stores active shopping sessions associated with the purchase list, facilitating easy access and tracking of ongoing sessions.
  - Stores all the events occurring within the global cart, enabling seamless storage and retrieval of event data for all the shopping session interactions.
- Message Queue:
  - Publish an event every time a shopping session is closed with the state of the purchase list and all the events that happened while the session was opened
  - Listen to the shopping session closed event, unlink the session with the list, and publish the event mentioned above. Also, close the cart when there is no other shopping session active.
  - Listen to the events provided by the purchase list module, adding to the global cart when there is some shopping session opened for the list
- HTTP Requests
  - The items' prices suggestions to the price suggestion module
  - The purchase list when the first shopping session is linked with the list
- HTTP Endpoints
  - To initiate a shopping cart
  - To receive shopping cart events
  - To return the shopping cart state (the list with the events)
 
### Shopping cart C4 Model (Containers)
The C4 Model provides a structured and hierarchical overview of the Shopping Cart module architecture, helping to better understand its components, relationships, and interactions. The model below aims to communicate the high-level design of this module clearly and concisely through the separation of concerns and identify potential dependencies. 

The C4 model presented succinctly depicts the vision of the Shopping Cart module, emphasizing only the relevant modules that directly interact with it. Non-impacted modules have been omitted to streamline clarity. Similarly, interactions with other services are excluded, ensuring a focused representation of the Shopping Cart module's architecture. This refined approach provides a clear and concise overview of the system's essential components, enhancing the understanding of its design and functionalities.

![Miro with the C4 model of the Shopping cart, considering the containers part](https://github.com/gumberss/PurchaseListinator/assets/38296002/01e92e0a-0c07-4481-956d-27f50eaf56ec)

### Shopping Cart Startup / Events Reception Sequence Diagram
The Sequence Diagram below provides a clear and detailed representation of the step-by-step interactions between different components, modules, and users involved with the Shopping Cart module as well as how the shopping cart will behave when a shopping session is started and events are received.

![image](https://github.com/gumberss/PurchaseListinator/assets/38296002/cc3454f5-b0d8-485c-ab31-717bb60a7d5b)
![image](https://github.com/gumberss/PurchaseListinator/assets/38296002/685545e0-4e35-4d55-bfba-93641631c8ea)

### Shopping Cart Clojure Sequence Diagram
The Sequence Diagram below provides a clear and detailed representation of the step-by-step interactions between different components, modules, and users involved with the Shopping Cart module as well as how the shopping cart will behave when a shopping session is closed.

![image](https://github.com/gumberss/PurchaseListinator/assets/38296002/de2f3b9e-8688-40b4-90b1-847ecfe4a02f)
![image](https://github.com/gumberss/PurchaseListinator/assets/38296002/26395f98-b7c1-4d03-8eb3-50143ce1129a)


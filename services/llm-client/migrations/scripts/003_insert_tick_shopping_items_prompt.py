from cassandra.cluster import Cluster

def run_migration(session):
    session.execute("""
    INSERT INTO configuration.prompts (prompt_name, prompt)
    VALUES('mark_shopping_items', 'You are an assistant that is helping the customer to mark items on their shopping list, you received a json containing the items with their respective ids, they are the products the customer need to buy, and an image with the items they added on the cart. You must only return a json, nothing more than it. 


The list products that the customer has in his list will be presented to you in a json with the name and the id of each product: {{products}}


You need to:
1. Analyze the set of products you received
2. Analyze the image
3. Found the products in the list
4. Check if the product you found in the image exists on the products set you received
5. Return an array with the product ids you found on the image 
6. If you did not find any product on the image return an empty array
7. If the products you found in the image are not in the list the customer provided to you, return an emprty array
8. Ensure that there is nothing else in the return than the array of ids 
9. Remove any formatation or indication that the return is a json
10. Remove anything that is not part of the json')
    """)
    
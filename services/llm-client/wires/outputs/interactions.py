from uuid import UUID

class Interaction():
    def __init__(self, request_id: UUID, response:str):
        self.requestId = request_id
        self.response = response
    
    
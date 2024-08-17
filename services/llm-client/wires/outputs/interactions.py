from uuid import UUID

class Interaction():
    def __init__(self, request_id: UUID, status:str, response:str, details:str):
        self.requestId = request_id
        self.status = status
        self.response = response
        self.details = details
    
    
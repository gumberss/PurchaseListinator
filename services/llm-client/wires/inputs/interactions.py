from pydantic import BaseModel
from typing import Dict, Any
from uuid import UUID

class Interaction(BaseModel):
    promptName: str
    variables: Dict[str, Any] = {}
    images: Dict[str, Any] = {}
    requestId: UUID 
from pydantic import BaseModel
from typing import Dict, Any, List
from uuid import UUID

class Interaction(BaseModel):
    promptName: str
    variables: Dict[str, Any] = {}
    images: List[str] = {}
    requestId: UUID 
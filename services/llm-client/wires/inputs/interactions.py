from pydantic import BaseModel
from typing import Dict, Any

class Interaction(BaseModel):
    promptName: str
    variables: Dict[str, Any] = {}
    images: Dict[str, Any] = {}
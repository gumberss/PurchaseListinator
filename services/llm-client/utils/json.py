import json
from uuid import UUID
from typing import Type, Dict, Any

def serialize(obj):
    data = _create_key_value_pairs(obj)
    return json.dumps(data, cls=_UUIDEncoder)

def deserialize(obj: str, type: Type):
    data = _json_to_dict(obj)
    return _dict_to_class(data, type)

class _UUIDEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, UUID):
            return str(obj)
        return super().default(obj)
    
def _create_key_value_pairs(obj):
    if hasattr(obj, '__dict__'):
        return vars(obj)
    else:
        raise TypeError("Object does not have a __dict__ attribute")

def _json_to_dict(json_string: str) -> Dict[str, Any]:
    return json.loads(json_string)

def _dict_to_class(d: Dict[str, Any], cls: Type) -> Any:
    annotations = getattr(cls, '__annotations__', {})
    
    for key, value in d.items():
        if annotations.get(key) == UUID and isinstance(value, str):
            d[key] = UUID(value)
    
    return cls(**d)

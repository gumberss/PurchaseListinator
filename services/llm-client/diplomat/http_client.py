
from openai import OpenAI
from models.prompts import RenderedPrompt
import os;

client = OpenAI(
  api_key=os.getenv('OPENAI_API_KEY').strip()
)

def complete(rendered_prompt : RenderedPrompt):
    images = [{
        "type": "image_url",
        "image_url": {
            "url": f"data:image/jpeg;base64,{image}"
        }
    } for image in rendered_prompt.images]
   
    completion = client.chat.completions.create(
            model="gpt-4o-mini",
            messages=[
                {
                    "role": "user",
                    "content": [
                        {
                            "type": "text",
                            "text": rendered_prompt.prompt
                        },
                        *images
                    ]
                }
            ],
        )
    return completion.choices[0].message.content
    
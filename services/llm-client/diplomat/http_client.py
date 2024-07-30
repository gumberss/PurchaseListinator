from openai import OpenAI

client = OpenAI(
  api_key=""
  #os.environ['OPENAI_API_KEY'],  # this is also the default, it can be omitted
)

def complete():
    completion = client.chat.completions.create(
        model="gpt-4o-mini",
        messages=[
            {
                "role": "user",
                "content": "Hello, how are you?",
            },
        ],
    )
    print(completion.choices[0].message.content)
    return completion.choices[0].message.content
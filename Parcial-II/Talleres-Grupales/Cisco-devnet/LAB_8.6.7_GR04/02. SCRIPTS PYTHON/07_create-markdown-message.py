import json
import requests
from config import ACCESS_TOKEN, ROOM_ID, MARKDOWN_MESSAGE

url = 'https://webexapis.com/v1/messages'
headers = {
    'Authorization': f'Bearer {ACCESS_TOKEN}',
    'Content-Type': 'application/json'
}
body = {
    'roomId': ROOM_ID,
    'markdown': MARKDOWN_MESSAGE
}

res = requests.post(url, headers=headers, json=body)
print(f'Status: {res.status_code}')
print(json.dumps(res.json(), indent=4))

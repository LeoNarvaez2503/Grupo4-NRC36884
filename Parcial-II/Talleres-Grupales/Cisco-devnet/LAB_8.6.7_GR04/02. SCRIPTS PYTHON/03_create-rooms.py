import json
import requests
from config import ACCESS_TOKEN, ROOM_TITLE

url = 'https://webexapis.com/v1/rooms'
headers = {
    'Authorization': f'Bearer {ACCESS_TOKEN}',
    'Content-Type': 'application/json'
}
body = {
    'title': ROOM_TITLE
}

res = requests.post(url, headers=headers, json=body)
data = res.json()
print(f'Status: {res.status_code}')
print(json.dumps(data, indent=4))

if 'id' in data:
    print(f"\n--> Copia este ROOM_ID en config.py:\n{data['id']}")

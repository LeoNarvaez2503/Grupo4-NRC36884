import json
import requests
from config import ACCESS_TOKEN, ROOM_ID, PERSON_EMAIL

url = 'https://webexapis.com/v1/memberships'
headers = {
    'Authorization': f'Bearer {ACCESS_TOKEN}',
    'Content-Type': 'application/json'
}
body = {
    'roomId': ROOM_ID,
    'personEmail': "acflores12@gmail.com"
}

res = requests.post(url, headers=headers, json=body)
print(f'Status: {res.status_code}')
print(json.dumps(res.json(), indent=4))

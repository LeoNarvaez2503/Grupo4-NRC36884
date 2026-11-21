import requests
import json

access_token = 'ZmIwOWNhMTItYjhjNy00OThjLWE4MjUtYzc2ZDNhNDliMDU3MGNhMTA2MWEtNzA2_P0A1_4324cf20-3dc3-4497-af07-1b996464c625'
url = 'https://webexapis.com/v1/rooms'
headers = {
    'Authorization': 'Bearer {}'.format(access_token),
    'Content-Type': 'application/json'
}
params= {'title': 'DevNet Associate Training! '}
res = requests.post(url, headers=headers, json=params)
print (res.json ())
import requests
import json

access_token = 'ZmIwOWNhMTItYjhjNy00OThjLWE4MjUtYzc2ZDNhNDliMDU3MGNhMTA2MWEtNzA2_P0A1_4324cf20-3dc3-4497-af07-1b996464c625'
room_id = 'Y2lzY29zcGFyazovL3VybjpURUFNOnVzLXdlc3QtMl9yL1JPT00vMTNjNTYwYjAtN2M1Yy0xMWViLTlmNzYtNWJhYmMxOGIyYmMy'
person_email = 'anthonybastidas48@gmail.com'
url = 'https://webexapis.com/v1/memberships'
headers = {
    'Authorization': 'Bearer {}'.format(access_token),
    'Content-Type': 'application/json'
}
params = {'roomId': room_id, 'personEmail': person_email}
res = requests.post(url, headers=headers, json=params)
print (res.json ()) 
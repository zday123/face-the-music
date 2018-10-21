import requests
import pathlib
import os

import sys
import spotipy
import spotipy.util as util

def main():
    path = pathlib.Path(os.path.abspath("data/success.jpg")).as_uri()
    # print(path)
    # path = 'https://www.google.com'
    # payload = {'client_id': '037fc929d3694a2a8b32fe9c32a4af28', 'response_type': 'token', 'redirect_uri': path}
    # r = requests.get('https://accounts.spotify.com/authorize', params=payload)
    # print(r.status_code, r.access_token, r.token_type)

    scope = 'user-read-currently-playing'

    if len(sys.argv) > 1:
        username = sys.argv[1]
    else:
        print("Please type your Spotify username in command line to allow access to your account!")
        sys.exit()
    token = util.prompt_for_user_token(username, scope, client_id='037fc929d3694a2a8b32fe9c32a4af28',client_secret='90ece485815946c28246292ec73dfe7d',redirect_uri='https://www.google.com/callback')
    if token:
        spotify = spotipy.Spotify(auth=token)
        r = spotify.currently_playing('US')
        # r = spotify.current_user_playlists(limit=50, offset=0)
        print(r.track.name)
        # for item in results['items']:
        #     track = item['track']
        #     print track['name'] + ' - ' + track['artists'][0]['name']


if __name__ == "__main__":
    main()
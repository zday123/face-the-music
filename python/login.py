import requests
import pathlib
import os

import sys
import spotipy
import spotipy.util as util

import time
import facethemusic
import webcamdriver
import timeit

def main():
    scopes = 'user-read-currently-playing playlist-modify-private playlist-read-private '

    if len(sys.argv) > 1:
        username = sys.argv[1]
    else:
        print("Please type your Spotify username in command line to allow access to your account!")
        sys.exit()
    token = util.prompt_for_user_token(username, scopes, client_id='037fc929d3694a2a8b32fe9c32a4af28', client_secret='90ece485815946c28246292ec73dfe7d', redirect_uri='https://www.google.com/callback')
    if token:
        spotify = spotipy.Spotify(auth=token)
        #Set-up playlists
        ul = spotify.user_playlists(username, limit=50, offset=0)
        for pl in [x for x in ['normal', 'joy', 'anger', 'sorrow'] if x + '-facethemusic' not in [l['name'] for l in ul['items'] if l['owner']['id'] == username]]:
            spotify.user_playlist_create(username, pl + '-facethemusic', public=False, description='Songs that make me feel ' + pl)
        songs = {}
        cam = webcamdriver.Webcamdriver()
        cam.begin_webcam_image()
        tic = timeit.default_timer()
        try:
            count = 0
            while True:
                count += 1
                cam.begin_webcam_image()
                toc = timeit.default_timer()
                if count >= 300:
                    try:
                        count = 0
                        r = spotify.currently_playing('US')
                        track = r['item']['uri']
                        name = r['item']['name']
                        filename = cam.capture()
                        emotion = facethemusic.fetch_emotion(filename)
                        os.remove(pathlib.Path(os.path.abspath(filename)))
                        if (track, name) in songs.keys():
                            songs[(track, name)][emotion] += 1
                        else:
                            songs[(track, name)] = {'joy': 0, 'sorrow': 0, 'anger': 0, 'normal': 0}
                        print("Track:", name, "Emotion detected:", emotion)
                    except:
                        break
        except KeyboardInterrupt:
            pass
        cam.release_image()
        print("\nAdding songs to Spotify playlists...\n")
        playlists = {'normal': [], 'joy': [], 'sorrow': [], 'anger': []}
        for key, options in songs.items():
            argmax = max(options, key=lambda k: options[k])
            print("Adding \"{} to \"{}-facethemusic\" playlist".format(key[1], argmax))
            playlists[argmax] += [key[0]]
        print("\n")
        ul = spotify.user_playlists(username, limit=50, offset=0)
        for pl in playlists:
            if len(playlists[pl]) > 0:
                for expl in range(len(ul['items'])):
                    if ul['items'][expl]['name'] == pl + "-facethemusic":
                        spotify.user_playlist_remove_all_occurrences_of_tracks(username, ul['items'][expl]['uri'],
                                                                               playlists[pl], snapshot_id=None)
                        spotify.user_playlist_add_tracks(username, ul['items'][expl]['uri'], playlists[pl], position=None)
                print("\nAdded {} items to {}-facethemusic playlist".format(len(playlists[pl]), pl))


if __name__ == "__main__":
    main()
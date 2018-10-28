curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"albumTitle":"album-1", "songTitle": "song-title-1", "artist": "artist-name-1"}' \
  http://localhost:9000/v1/music

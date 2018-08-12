curl -v -X POST https://content.dropboxapi.com/2/files/download \
    --header "Authorization: Bearer oUS03qam98AAAAAAAAAAx9CoKf3qpFWLtzxu6kq7Eei7OJ30RfWg7SIl6MGbSLy3" \
    --header "Dropbox-API-Arg: {\"path\": \"/assets/Prog Noir Image.jpeg\"}" \
    --output ./out.jpg


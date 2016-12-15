curl -XPOST 'http://localhost:8090/token.connect?user=yskim' -d 'this is yskim'
echo 
curl -XPOST 'http://localhost:8090/token.connect?user=young' -d 'this is young'
echo 
curl -XGET 'http://localhost:8090/token.userlist'
echo 

curl -XPOST 'http://localhost:8090/token.addchat?user=yskim' -d 'hello'
echo 
curl -XPOST 'http://localhost:8090/token.addchat?user=young' -d 'hello'
echo 

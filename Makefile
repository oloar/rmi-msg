.PHONY: all clean build

build:
	mkdir -p out/server out/client
	javac -cp src src/ChatServer.java -d out/server/
	javac -cp src src/ChatClient.java -d out/client/

clean:
	rm -f out/client/*.class out/server/*.class out/server/history.txt

.PHONY: all clean build

build:
	javac -cp src src/ChatServer.java -d out/server/
	javac -cp src src/ChatClient.java -d out/client/

clean:
	rm out/client/*.class out/server/*.class out/server/history.txt

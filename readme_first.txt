
Classes explanation:

	mainServer			: our main thread for the server
	clientThread 		: worker thread that takes the required data and sends the response
	statThread 			: statistics thread
	xmlParser			: XML Parser for the configuration
	reqOb				: constructs an object with the required data for the clientThread
	
Program explanation:
	mainServer runs a Web-Server continously in its main thread.
	It can support 2 threads-workers(clientThread class) which handle the response on port 8000.
	Also, on another thread, we have port 8001 that shows the Statistics about the server.
	
	About XML Parsing:
		The Server is configured to run at /root/ directory.
		(In Windows, it's C:/root/ ) 
		
		"config.xml" file is in a different format than the example that you provide us.
		(As said in the lecture, we could make our own XML file from the beginning)
		You do not have to place the configuration file as an argument when running the program.
		You just need to have it under the same directory with the executable.
		If you want to change a directory or a port for the server to use, just edit the config.xml.
		It's in a simpler format for the XML DOM Parser libraries to use, without node-kids searching.
		If you want it in an alternative form, contact us.
	
	About the logs:
		access_log.txt and error_log.txt are created under /root/.
	
	About concurrency-threads :
		Our program can support 2 workers that send responses.
		We use a BlockingQueue where we load there the required data for the request.
		The workers take the objects from the queue and work for them sending the response.
		This is a technique to solve concurrency problems since when a worker tries to take an object
		from the BlockingQueue and it's null, the worker blocks until an object gets put into the queue for 
		him to work!
		Also, we added "synchronized" prefix on some methods in clientThread (worker class) so only one
		thread runs the method at the time and doesn't interrupt the other.
		Just like the 'locks' example we saw in the lectures.
		
		Last but not least, uncomment line 99-100 in mainServer to test the second worker.

Problems/Caution! :
		1)According to your computer and your browser, you might see some problems in concurrency as the program might try to send 
		data to a closed(by false) socket.
		This corresponds to line 356 in clientThread class.
		This error can occur when the local network system aborts a connection, 
		such as when WinSock closes an established connection after data retransmission fails 
		(receiver never acknowledges data sent on a datastream socket)
	
		2)Given "mime-types.txt" had a double entry for .mp4 , which blocked our browsers from playing the video.
		We deleted the one entry, so please use our mime-types.txt file for testing.
		
		3)As said previously, there is no need to put the .xml config file as an argument anymore.
		We do this automatically!
		
Contact:
		Eleftherios Panagiotis Loukas : eloukas@uth.gr
		Katerina Kanellopoulou 		  : akanellop@uth.gr
		
	
	
	

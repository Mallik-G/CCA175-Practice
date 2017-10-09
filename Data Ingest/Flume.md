#### Setup for Basic Example 
flume-ng agent --name a1 --conf /home/cloudera/Desktop/flumePrac -f example.conf

telnet 127.0.0.1 44444
// Enter the log messages and they will be captured in the sink

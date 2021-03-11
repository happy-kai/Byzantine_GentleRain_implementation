# Byzantine_GentleRain_implementation
The implementation of ByzantinegGentleRain

### Abstract
Byzantine GentleRain is a protocol which satisfies causal consistency and is able to tolarate Byzantine failures. This protocol is based on GentleRain and PBFT protocols. It is a three phase protocol; one phase takes place between the client and servers, which is similar to GentleRain, while the others take place between the servers in order to tolarate Byzantine failures.  

We know that GentleRain uses pysical time to sign each operation, so that each operation can safely give the response which will not avolite causal consistency in just one round of communication. But every coin has two sides, to check the causal dependency, each server needs to maintain the physical time of the last operation seen by all the servers. So GentleRain protocol uses two vector to store the LST and GST on each server, which is a very skillful design. Our Byzantine GentleRain takes the same strategy; however, the client needs to communicate with all the servers this time, since every server may be a byzantine server.

### Implementation

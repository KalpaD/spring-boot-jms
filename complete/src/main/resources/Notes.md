When configured with default settings like below there is one listner thread 
which does all the work

```Java
        @JmsListener(destination = "mailbox", containerFactory = "myFactory")
        public void receiveMessage(Email email) {
            LOGGER.info("Received {} on thread {}", email, Thread.currentThread().getName());
        }
```

And the output you see from above is like this
```
 enerContainer-1] hello.Receiver: Received Email{to=info@example.com, body=Hello -0} on thread DefaultMessageListenerContainer-1
[enerContainer-1] hello.Receiver: Received Email{to=info@example.com, body=Hello -1} on thread DefaultMessageListenerContainer-1
```

when we adjust the concurrency level using the following configuration
there are multiple listener thread start tp appear and do the work

```Java
        @JmsListener(destination = "mailbox", containerFactory = "myFactory", concurrency = "1-5")
        public void receiveMessage(Email email) {
            LOGGER.info("Received {} on thread {}", email, Thread.currentThread().getName());
        }
```

```
[enerContainer-2] hello.Receiver Received Email{to=info@example.com, body=Hello -2} on thread DefaultMessageListenerContainer-2
[enerContainer-3] hello.Receiver Received Email{to=info@example.com, body=Hello -7} on thread DefaultMessageListenerContainer-3
[enerContainer-5] hello.Receiver Received Email{to=info@example.com, body=Hello -18} on thread DefaultMessageListenerContainer-5
```


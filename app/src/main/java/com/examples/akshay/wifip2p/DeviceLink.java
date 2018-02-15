package com.examples.akshay.wifip2p;

public class DeviceLink {

    /*

    Now you have three problems to solve:

    1) How can you instantiate just once this class and inject it to SendDataTask and ReceiveDataTask

    I'm betting that Android has a dependency injection container that you can use for that
    I'm also betting that Android has some class/method you can implement that gets called only once
    when the app is launched

    No singletons needed! :D

    2) Implement receive() and send()

    You already had that worked out. Just place the code inside those methods and make them share
    some socket connection.

     */
    
    public String receive() {
        return "";
    }

    public void send(String data) {

    }
}

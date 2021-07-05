# **D.J. - DiscordJava**
**An rudimentary incomplete java library for discord bots!!**

Don't expect it to be much :(

This is a possible starting point for anyone who wants to make their library, but I wouldn't suggest using it to create a bot as it isn't there yet. It is far from complete and would only cause you pain. This library is currently limited to only bots and you won't be able to utilize non-bot discord api pathways.

## Using it

Something you might wanna do ¯\\\_(ツ)_/¯

It is far from usable, but if you really want to punish yourself, start by initializing the `Client` class. _I personally create a subclass of `Client` and intialize that._

Now you must create event listeners to interact with the `DiscordGateway`. To do so, call the `Client.on(String eventName, Client.EventHandler.Listener listener)` method. To construct the `Listener class`
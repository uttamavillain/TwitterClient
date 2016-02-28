# Project 1 - *InstagamViewer*

**InstagamViewer** is an android app that allows a user to check out popular photos from Instagram. The app utilizes Instagram API to display images and basic image information to the user.

Time spent: **15** hours spent in total

## User Stories

The following **required** functionality is completed:

* [X] User can sign in to Twitter using OAuth login (2 points)
* [X] User can view the tweets from their home timeline
  * [X] User should be displayed the username, name, and body for each tweet (2 points)
  * [X] should be displayed the relative timestamp for each tweet "8m", "7h" (1 point)
  * [X] can view more tweets as they scroll with infinite pagination (1 point)
* [X] User can compose a new tweet
  * [X] User can click a “Compose” icon in the Action Bar on the top right (1 point)
  * [X] User can then enter a new tweet and post this to twitter (2 points)
  * [X] User is taken back to home timeline with new tweet visible in timeline (1 point)

The following advanced user stories are optional:

* [X] Advanced: While composing a tweet, user can see a character counter with characters remaining for tweet out of 140 (1 point)
* [X] Advanced: Links in tweets are clickable and will launch the web browser (see autolink) (1 point)
* [X] Advanced: User can refresh tweets timeline by pulling down to refresh (i.e pull-to-refresh) (1 point)
* [X] Advanced: User can open the twitter app offline and see last loaded tweets
  * [X] Tweets are persisted into sqlite and can be displayed from the local DB (2 points)
* [] Advanced: User can tap a tweet to display a "detailed" view of that tweet (2 points)
* [X] Advanced: User can select "reply" from detail view to respond to a tweet (1 point)
* [X] Advanced: Improve the user interface and theme the app to feel "twitter branded" (1 to 5 points)
* [X] Bonus: User can see embedded image media within the tweet detail view (1 point)
* [X] Bonus: User can watch embedded video within the tweet (1 point)
* [X] Bonus: Compose activity is replaced with a modal overlay (2 points)
* [X] Bonus: Use Parcelable instead of Serializable using the popular Parceler library. (1 point)
* [X] Bonus: Apply the popular Butterknife annotation library to reduce view boilerplate. (1 point)
* [] Bonus: Leverage the popular GSON library to streamline the parsing of JSON data. (1 point)
* [] Bonus: Leverage RecyclerView as a replacement for the ListView and ArrayAdapter for all lists of tweets. (2 points)
* [] Bonus: Move the "Compose" action to a FloatingActionButton instead of on the AppBar. (1 point)
* [] Bonus: Replace Picasso with Glide for more efficient image rendering. (1 point)

## Video Walkthrough 

Here's a walkthrough of implemented user stories:

![Video Walkthrough](Demo.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Describe any challenges encountered while building the app.

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android
- [Butterknife] (http://jakewharton.github.io/butterknife/) A annotation library to reduce view boilerplate

## License

    Copyright [2016] [name of copyright owner]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

# ComposeRestGallery

Simple app written in Jetpack Compose showcasing:

* Unidirectional Data Flow
* Simple paging mechanism
* Loading data via REST API using Retrofit
* Custom JSON deserialization using Kotlinx.Serialization
* Unit testing
* UI testing

**Note:**
This app connects to https://unsplash.com/documentation#list-photos
Requests to this API needs to be authenticated. Authentication token is not provided in sources.
You need to specify your own token in the file 
[GalleryService.kt](/app/src/main/java/com/example/composerestgallery/screens/gallery/api/GalleryService.kt)

![Screen capture of the app](/app_capture.gif)

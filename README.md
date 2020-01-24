**YouTubeExtractor**

A Youtube urls extractor for java & android for streaming and downloading purpose.

I made this on my Android Device using AIDE(IDE), So i cant update gradle, so dont ask me

[Test Apk](https://github.com/naveedhassan913/YouTubeExtractor/raw/master/YoutubeExtractor/app/build/bin/app.apk)

## Features 
- Extracts Muxed and Adaptive urls separately
- Extracts Signature Protected Videos(like vevo)
- Extracts Live Videos Urls(hls) 
- Extracts video info(title,author,description,view,etc)
- Extracts Age restricted videos (Uses Cookie from a Google account)
- Extracts YouTube Video Captions

### Usage

`Copy the classes or compile project.`


## Dependencies Used 
- Gson
- Mozilla Rhino
- UniversalVideoView(Used only for video testing)

Usage

```Java
new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner(){
				@Override
				public void onExtractionDone(List<YTMedia> adativeStream, final List<YTMedia> muxedStream,List<YTSubtitles> subtitles, YoutubeMeta meta) {
					//url to get subtitle
					String subUrl=subtitles.get(0).getBaseUrl();
					for (YTMedia media:adativeStream) {
						if(media.isVideo()){
							//is video
						}else{
							//is audio
						}
					}
				}
				@Override
				public void onExtractionGoesWrong(final ExtractorException e) {
					Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}).useDefaultLogin().Extract(URL/YOUTUBE_ID);
             //use .useDefaultLogin() to extract age restricted videos 

```




## For age restricted Videos

For extraction of age restricted videos use `useDefaultLogin()` to Use default cookie.. OR YOU CAN override with your own cookies by method `setHeaders` 

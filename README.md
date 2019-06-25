# YouTubeExtractor

A Youtube urls extractor for java & android for streaming and downloading purpose. 

_I made this on my Android Device using AIDE(IDE),
So i cant update gradle, so dont ask me_ 

# Features

- Extracts Muxed and Adaptive urls separately
- Extracts Signature Protected Videos(like vevo) 
- Extracts Live Videos Urls(hls)
- Extracts video info(title,author,description,view,etc)

 

# Usage

Copy the classes or compile  project.

_.jar will be available  soon_

# Dependencies Used

- Gson
- Mozilla Rhino
- UniversalVideView(Used only for video testing)


# Example


```
new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner(){
           @Override
 public void onExtractionDone(List<YoutubeMedia> adativeStream, final List<YoutubeMedia> muxedStream, YoutubeMeta meta) 
	 
	        {
						

												   
		 }

@Override
public void onExtractionGoesWrong(final ExtractorException e) 

                {

												   

                }
		}).Extract(YOUTUBE_ID/LINK); 
```
							 




	

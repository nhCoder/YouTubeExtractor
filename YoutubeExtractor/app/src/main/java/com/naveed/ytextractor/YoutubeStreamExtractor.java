package com.naveed.ytextractor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.naveed.ytextractor.CipherManager;
import com.naveed.ytextractor.model.PlayerResponse;
import com.naveed.ytextractor.model.Response;
import com.naveed.ytextractor.model.StreamingData;
import com.naveed.ytextractor.model.YTMedia;
import com.naveed.ytextractor.model.YoutubeMeta;
import com.naveed.ytextractor.utils.HTTPUtility;
import com.naveed.ytextractor.utils.LogUtils;
import com.naveed.ytextractor.utils.RegexUtils;
import com.naveed.ytextractor.utils.Utils;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.naveed.ytextractor.model.YTSubtitles;

public class YoutubeStreamExtractor extends AsyncTask<String,Void,Void>
{


	Map<String,String> Headers=new HashMap<>();
	List<YTMedia> adaptiveMedia=new ArrayList<>();
	List<YTMedia> muxedMedia=new ArrayList<>();
	List<YTSubtitles> subtitle=new ArrayList<>();
	String regexUrl=("(?<=url=).*");
	String regexYtshortLink="(http|https)://(www\\.|)youtu.be/.*";
	String regexPageLink = ("(http|https)://(www\\.|m.|)youtube\\.com/watch\\?v=(.+?)( |\\z|&)");
	String regexFindReason="(?<=(class=\"message\">)).*?(?=<)";
	
	String regexPlayerJson1="(?<=ytplayer.config\\s=).*?((\\}(\n|)\\}(\n|))|(\\}))(?=;)"; //original
	String regexPlayerJson2="(?<=ytInitialPlayerResponse\\s=).*?(\\}(\\]|\\})\\})(?=;)";  //11.23.2020 After YouTube changes	
	String PlayerBaseRegex="(?<=PLAYER_JS_URL\":\").*?(?=\")";
	ExtractorListner listener;
	private ExtractorException Ex;
	List<String> reasonUnavialable=Arrays.asList(new String[]{"This video is unavailable on this device.","Content Warning","who has blocked it on copyright grounds."});
	Handler han=new Handler(Looper.getMainLooper());
	private Response response;
	private YoutubeMeta ytmeta;

	PlayerResponse playerResponse;
	private int selectedRegrexPlayerJson = 0; //Flag to check the selected regexPlayerJson variable

	public YoutubeStreamExtractor(ExtractorListner EL)
	{
		this.listener = EL;
		Headers.put("Accept-Language", "en");
	}

	public YoutubeStreamExtractor setHeaders(Map<String, String> headers)
	{
		Headers = headers;
		return this;
	}

	public YoutubeStreamExtractor useDefaultLogin()
	{
		Headers.put("Cookie", Utils.loginCookie);
		return setHeaders(Headers);	
	}

	public Map<String, String> getHeaders()
	{
		return Headers;
	}

	public void Extract(String VideoId)
	{
		this.execute(VideoId);
	}



	@Override
	protected void onPostExecute(Void result)
	{
		if (Ex != null)
		{
			listener.onExtractionGoesWrong(Ex);
		}
		else
		{
			listener.onExtractionDone(adaptiveMedia, muxedMedia, subtitle, ytmeta);
		}
	}

	@Override
	protected void onPreExecute()
	{
		Ex = null;
		adaptiveMedia.clear();
		muxedMedia.clear();

	}

	@Override
	protected void onCancelled()
	{
		if (Ex != null)
		{
			listener.onExtractionGoesWrong(Ex);
		}	
	}



	@Override
	protected Void doInBackground(String[] ids)
	{

		String Videoid=Utils.extractVideoID(ids[0]);
        String jsonBody = null;
        try
		{
			String body = HTTPUtility.downloadPageSource("https://www.youtube.com/watch?v=" + Videoid + "&has_verified=1&bpctr=9999999999", Headers);
			jsonBody = parsePlayerConfig(body);

			if(selectedRegrexPlayerJson == 1){
				playerResponse = parseJson1(jsonBody);
				playerResponse.setPlayerJs(RegexUtils.matchGroup(PlayerBaseRegex, body));
			}else if(selectedRegrexPlayerJson == 2){
				playerResponse = parseJson2(jsonBody);
				playerResponse.setPlayerJs(RegexUtils.matchGroup(PlayerBaseRegex, body));
			}//else - When the situation comes, parsePlayerConfig() already throws an error.

			
			ytmeta = playerResponse.getVideoDetails();
			subtitle = playerResponse.getCaptions() != null ? playerResponse .getCaptions().getPlayerCaptionsTracklistRenderer().getCaptionTracks(): null;
			//Utils.copyToBoard(jsonBody);
			if (playerResponse.getVideoDetails().getisLive())
			{
				parseLiveUrls(playerResponse.getStreamingData());
			}
			else
			{
				StreamingData sd=playerResponse.getStreamingData();
				LogUtils.log("sizea= " + sd.getAdaptiveFormats().length);
				LogUtils.log("sizem= " + sd.getFormats().length);

				adaptiveMedia =	parseUrls(sd.getAdaptiveFormats());
				muxedMedia =	parseUrls(sd.getFormats());
				LogUtils.log("sizeXa= " + adaptiveMedia.size());
				LogUtils.log("sizeXm= " + muxedMedia.size());

			}
		}
		catch (Exception e)
		{
			LogUtils.log(Arrays.toString(e.getStackTrace()));
			Ex = new ExtractorException("Error While getting Youtube Data:" + e.getMessage());
			this.cancel(true);
		}
		return null;
	}

	/*this function creates Json models using Gson*/
	private PlayerResponse parseJson1(String body) throws Exception
	{
		JsonParser parser=new JsonParser();
		response = new GsonBuilder().serializeNulls().create().fromJson(parser.parse(body), Response.class);
		return new GsonBuilder().serializeNulls().create().fromJson(response.getArgs().getPlayerResponse(), PlayerResponse.class);
	}

	private PlayerResponse parseJson2(String body) throws Exception {
		JsonParser parser=new JsonParser();


		return new GsonBuilder().serializeNulls().create().fromJson(parser.parse(body), PlayerResponse.class);
	}
	/*This function is used to check if webpage contain steam data and then gets the Json part of from the page using regex*/
	private String parsePlayerConfig(String body) throws ExtractorException
	{

		if (Utils.isListContain(reasonUnavialable, RegexUtils.matchGroup(regexFindReason, body)))
		{
			throw new ExtractorException(RegexUtils.matchGroup(regexFindReason, body));
		}
		if(RegexUtils.matchGroup(regexPlayerJson1, body) != null){
			selectedRegrexPlayerJson = 1;
			return RegexUtils.matchGroup(regexPlayerJson1, body);
		}else if(RegexUtils.matchGroup(regexPlayerJson2, body) != null){
			selectedRegrexPlayerJson = 2;
			return RegexUtils.matchGroup(regexPlayerJson2, body);
		}
		else
		{
			selectedRegrexPlayerJson = 3;
			throw new ExtractorException("This Video is unavialable");
		}
	}




	/*independent function Used to parse urls for adaptive & muxed stream with cipher protection*/

	private List<YTMedia> parseUrls(YTMedia[] rawMedia)
	{
		List<YTMedia> links=new ArrayList<>();
		try
		{
			for (int x=0;x < rawMedia.length;x++)
			{
				YTMedia media=rawMedia[x];
				LogUtils.log(media.getSignatureCipher() != null ? media.getSignatureCipher(): "null cip");

				if (media.useCipher())
				{
					String tempUrl = "";
					String decodedSig = "";
					for (String partCipher:media.getSignatureCipher().split("&"))
					{



						if (partCipher.startsWith("s="))
						{
							decodedSig = CipherManager.dechiperSig(URLDecoder.decode(partCipher.replace("s=", "")), playerResponse.getPlayerJs());
						}

						if (partCipher.startsWith("url="))
						{
							tempUrl = URLDecoder.decode(partCipher.replace("url=", ""));

							for (String url_part:tempUrl.split("&"))
							{
								if (url_part.startsWith("s="))
								{
									decodedSig = CipherManager.dechiperSig(URLDecoder.decode(url_part.replace("s=", "")), playerResponse.getPlayerJs());
								}
							}
						}
					}

					String	FinalUrl= tempUrl + "&sig=" + decodedSig;

					media.setUrl(FinalUrl);
					links.add(media);


				}
				else
				{
					links.add(media);
				}
			}

		}
		catch (Exception e)
		{
			Ex = new ExtractorException(e.getMessage());
			this.cancel(true);
		}
		return links;
	}





	/*This funtion parse live youtube videos links from streaming data  */

	private void parseLiveUrls(StreamingData streamData) throws Exception
	{
		if (streamData.getHlsManifestUrl() == null)
		{
			throw new ExtractorException("No link for hls video");
		}
		String hlsPageSource=HTTPUtility.downloadPageSource(streamData.getHlsManifestUrl());
		String regexhlsLinks="(#EXT-X-STREAM-INF).*?(index.m3u8)";
		List<String> rawData= RegexUtils.getAllMatches(regexhlsLinks, hlsPageSource);
		for (String data:rawData)
		{
			YTMedia media=new YTMedia();
			String[] info_list=RegexUtils.matchGroup("(#).*?(?=https)", data).split(",");
			String live_url=RegexUtils.matchGroup("(https:).*?(index.m3u8)", data);
			media.setUrl(live_url);
			for (String info:info_list)
			{
				if (info.startsWith("BANDWIDTH"))
				{
					media.setBitrate(Integer.valueOf(info.replace("BANDWIDTH=", "")));
				}
				if (info.startsWith("CODECS"))
				{
					media.setMimeType((info.replace("CODECS=", "").replace("\"", "")));
				}
				if (info.startsWith("FRAME-RATE"))
				{
					media.setFps(Integer.valueOf((info.replace("FRAME-RATE=", ""))));
				}
				if (info.startsWith("RESOLUTION"))
				{
					String[] RESOLUTION= info.replace("RESOLUTION=", "").split("x");
					media.setWidth(Integer.valueOf(RESOLUTION[0]));
					media.setHeight(Integer.valueOf(RESOLUTION[1]));
					media.setQualityLabel(RESOLUTION[1] + "p");
				}
			}
			LogUtils.log(media.getUrl());
			muxedMedia.add(media);
		}


	}

	public interface ExtractorListner
	{
		void onExtractionGoesWrong(ExtractorException e)
		void onExtractionDone(List<YTMedia> adativeStream, List<YTMedia> muxedStream, List<YTSubtitles> subList, YoutubeMeta meta)
	}

}     

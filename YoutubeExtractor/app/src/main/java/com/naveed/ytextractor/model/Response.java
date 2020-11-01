package com.naveed.ytextractor.model;


import java.util.HashMap;
import java.util.Map;

public class Response
{

	private Args args;
	private String playerJs;

	public void setPlayerJs(String playerJs)
	{
		this.playerJs = playerJs;
	}

	public String getPlayerJs()
	{

		if (playerJs.startsWith("http") && playerJs.contains("youtube.com"))
		{
			return playerJs.replace("\\", "");
		}
		else return "https://www.youtube.com" + playerJs.replace("\\", "");


	}


	public Args getArgs()
	{
		return args;
	}

	public void setArgs(Args args)
	{
		this.args = args;
	}







	public class Args
	{

		private String adaptive_fmts;
		private String player_response;
		private String url_encoded_fmt_stream_map;

		public String getAdaptiveFmts()
		{
			return adaptive_fmts;
		}

		public void setAdaptiveFmts(String adaptiveFmts)
		{
			this.adaptive_fmts = adaptiveFmts;
		}

		public String getPlayerResponse()
		{
			return player_response;
		}

		public void setPlayerResponse(String playerResponse)
		{
			this.player_response = playerResponse;
		}

		public String getUrlEncodedFmtStreamMap()
		{
			return url_encoded_fmt_stream_map;
		}

		public void setUrlEncodedFmtStreamMap(String urlEncodedFmtStreamMap)
		{
			this.url_encoded_fmt_stream_map = urlEncodedFmtStreamMap;
		}




	}


	
}
	
	

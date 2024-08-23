package main.java;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class StockAPIWrapper {
	private final String propertiesFilePath = "properties.cfg";
	private SocketClient wsc;

	private String getApiKey(String key) {
		File file;
		Scanner sc;
		StringBuilder apiKeyData;

		try {
			file = new File(propertiesFilePath);
			sc = new Scanner(file);
			apiKeyData = new StringBuilder();
		} catch (FileNotFoundException f) {
			System.err.println("File not found.");
			f.printStackTrace();

			file = null;
			sc = null;
			apiKeyData = null;
		}
		if (sc.hasNextLine())
		{
			apiKeyData.append(sc.next());
		}

		return apiKeyData.substring(key.length()+1);
	}

	private void initializeWebSocket()
	{
		String apiKey = this.getApiKey("finnhubApiKey");
		URI endpoint;

		try {
			endpoint = new URI("wss://ws.finnhub.io?token="+apiKey);
			wsc = new SocketClient(endpoint);
		} catch (URISyntaxException uriSynEx) {
			System.err.println("URI Syntax Error");
			uriSynEx.printStackTrace();

			endpoint = null;
			return;
		}

		try {
			if (!wsc.connectBlocking())
			{
				throw new InterruptedException();
			}
		} catch (InterruptedException ie) {
			System.err.println("Could not successfully initialize the websocket.");
			ie.printStackTrace();
		}
	}

	private void fetchStockPrices(User u) {

		FileIOSQL fios = new FileIOSQL("jBullDB.db");
		HashMap<String,Integer> stockData = fios.readStockData(u);

		for (Map.Entry<String, Integer> entry : stockData.entrySet())
		{
			wsc.send("{\"type\":\"subscribe\",\"symbol\":\"" + entry.getKey() + "\"}");
			wsc.close();
		}
	}

	private static URI getApiEndpoint(String apiMapKey, String symbol)
	{
		StockAPIWrapper stockAPIWrapper = new StockAPIWrapper();

		URI uri = null;
		String apiKey;

        apiKey = stockAPIWrapper.getApiKey(apiMapKey);

        final String endpointString = "https://finnhub.io/api/v1/quote?symbol="+symbol+"&token="+apiKey;

		try {
			uri = new URI(endpointString);
		} catch (URISyntaxException uriEx)
		{
			System.err.println("EXCEPTION: Unable to construct URI...");
			uriEx.printStackTrace();
		}
        return uri;
    }

	public Double getStockPrice(String symbol)
	{
		URI uri = getApiEndpoint("finnhubApiKey", symbol);
		HttpClient client = HttpClient.newBuilder()
				.build();

		HttpRequest request = HttpRequest.newBuilder()
				.uri(uri)
				.build();


		CompletableFuture<HttpResponse<String>> asyncResponse;

		try {
			asyncResponse = client.sendAsync(request, BodyHandlers.ofString());
			String clientResultBody = asyncResponse.thenApply(HttpResponse::body).get();

			ObjectMapper objMapper = new ObjectMapper();
			JsonNode jsonNode = objMapper.readTree(clientResultBody);
			Double currentPrice = Double.valueOf(jsonNode.get("c").asDouble());

			//Checking for invalid stock ticker
			if (currentPrice == 0.0) {
				throw new Exception("EXCEPTION: Invalid stock ticker");
            } else {
				return currentPrice;
			}

		} catch (Exception e)
		{
			asyncResponse = null;

			System.err.println("EXCEPTION: Unable to parse JSON response body...");
			e.printStackTrace();
		}

		return null;
	}

//	public Double getStockPriceFromWebSocket(String symbol)
//	{
//		initializeWebSocket();
//
//
//		wsc.send("{\"type\":\"subscribe\",\"symbol\":\"" + symbol + "\"}");
//
//
//
//		wsc.close();
//
////		try {
////			asyncResponse = client.sendAsync(request, BodyHandlers.ofString());
////			String clientResultBody = asyncResponse.thenApply(HttpResponse::body).get();
////
////			ObjectMapper objMapper = new ObjectMapper();
////			JsonNode jsonNode = objMapper.readTree(clientResultBody);
////			Double currentPrice = Double.valueOf(jsonNode.get("c").asDouble());
////
////			//Checking for invalid stock ticker
////			if (currentPrice == 0.0) {
////				throw new Exception("EXCEPTION: Invalid stock ticker");
////			} else {
////				return currentPrice;
////			}
////
////		} catch (Exception e)
////		{
////			asyncResponse = null;
////
////			System.err.println("EXCEPTION: Unable to parse JSON response body...");
////			e.printStackTrace();
////		}
//
//		return null;
//	}
}
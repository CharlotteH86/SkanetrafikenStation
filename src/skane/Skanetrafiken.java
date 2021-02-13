package skane;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Servlet implementation class CheckSkanetrafiken
 */
@WebServlet("/Skanetrafiken")
public class Skanetrafiken extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private ArrayList<String> names = new ArrayList<String>();;

	public ArrayList<String> getNames() {
		return names;
	}

	

    public Skanetrafiken() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		

		 //String URL = "www.labs.skanetrafiken.se/v2.2/querystation.asp";
		
		
		// Check if the right info got sent
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				String cityStr = request.getParameter("cityFrom");
				out.print("<br>");
				out.print("City= " + cityStr);

				out.print("<br>");

				String destination = request.getParameter("cityTo");
				out.print("Destination= " + destination);
				out.print("<br>");

				// Build the API call by adding city+country into a URL
				String URLtoSend = "http://www.labs.skanetrafiken.se/v2.2/querystation.asp?inpPointfr="+cityStr+"&inpPointTo="+destination;

				out.print(URLtoSend+"Hej");
				System.out.println(URLtoSend);

				// Set the URL that will be sent
				URL line_api_url = new URL(URLtoSend);
				

				// Create a HTTP connection to sent the GET request over
				HttpURLConnection linec = (HttpURLConnection) line_api_url.openConnection();
				linec.connect();
				
				linec.setDoInput(true);
				linec.setDoOutput(true);
				linec.setRequestMethod("GET");

				// Make a Buffer to read the response from the API
				BufferedReader in = new BufferedReader(new InputStreamReader(linec.getInputStream()));

				// a String to temp save each line in the response
				String inputLine;

				// a String to save the full response to use later
				String ApiResponse = "";

				// loop through the whole response
				while ((inputLine = in.readLine()) != null) {
					
					//System.out.println(inputLine);
					// Save the temp line into the full response
					ApiResponse += inputLine;
				}
				in.close();
				System.out.println(ApiResponse);
				
				Document doc = convertStringToXMLDocument(ApiResponse);
		        doc.getDocumentElement().normalize();
		        System.out.println("Root ele:" + doc.getDocumentElement().getNodeName());

		        NodeList lineTypeNameTag = doc.getElementsByTagName("LineTypeName");
		        for (int i = 0; i < 10; i++) {
		            Node node = lineTypeNameTag.item(i);
		            if (node.getNodeType() == Node.ELEMENT_NODE) {
		                Element ele = (Element) node;
		                System.out.println(ele.getTextContent());
		                names.add(lineTypeNameTag.item(i).getTextContent());}}

		            
		        }
				
				/*
				Document doc = convertStringToXMLDocument(ApiResponse);

				doc.getDocumentElement().normalize();

				System.out.println("Root ele:" + doc.getDocumentElement().getNodeName());

				Node nodeBody = doc.getElementsByTagName("soap:Body").item(0);

				NodeList nodeResult = (NodeList) nodeBody.getFirstChild().getFirstChild();

				Node nodelines = nodeResult.item(2);

				NodeList listOflines = nodelines.getChildNodes();

				for (int i = 0; i < listOflines.getLength(); i++) {

					 System.out.println(listOflines.item(i).getFirstChild().getTextContent());

					NodeList allLine = listOflines.item(i).getChildNodes();

					for (int y = 0; y < allLine.getLength(); y++) {
						 System.out.println(allLine.item(y).getTextContent());

						if (allLine.item(y).getNodeName().equals("Name")) {

							System.out.println("Name " + allLine.item(y).getTextContent());

							// add xml result to list
							names.add(allLine.item(y).getTextContent());
						}

					}
				}

			}*/

			private Document convertStringToXMLDocument(String xmlString) {

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

				DocumentBuilder builder = null;

				try {

					builder = factory.newDocumentBuilder();

					Document doc = builder.parse(new InputSource(new StringReader(xmlString)));

					return doc;

				} catch (Exception e) {
					e.printStackTrace();
				}

				return null;
			}

		}
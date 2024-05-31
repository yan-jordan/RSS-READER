import org.jsoup.Jsoup;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class RssReader {
    public RssReader ()
    {
        try {
            FileReader reader = new FileReader(new File("data.txt"));
            BufferedReader reader1 = new BufferedReader(reader);
//            Scanner reader = new Scanner(new File("data.txt"));
            String line ;
            while ((line = reader1.readLine())!=null)
            {
                urlAdress.add(line);
            }

            reader1.close();
        }
        catch (IOException e)
        {
            System.out.println("error open");
        }


        welcoming();

    }

    Scanner scanner = new Scanner(System.in);
    private static final int Maximum = 5;

    ArrayList<String> urlAdress = new ArrayList<>();

    public void welcoming() {
        System.out.println("!!! WELCOME TO RSS READER !!!");
        System.out.println("TYPE A VALID NUMBER FOR YOUR DESIRED ACTION ");
        System.out.println(" [1] SHOW UPDATES ");
        System.out.println(" [2] ADD URL ");
        System.out.println(" [3] REMOVE URL ");
        System.out.println(" [4] EXIT ");
        System.out.print("YOUR NUMBER IS : ");
        int desiredNumber = scanner.nextInt();
        System.out.println("desired number is  : " + desiredNumber);

        if (desiredNumber == 1)
            showUpdate();
        else if (desiredNumber == 2)
            addURL();
        else if (desiredNumber == 3)
            removeURL();
        else if (desiredNumber == 4)
            try {
                FileWriter writer = new FileWriter("data.txt");
                BufferedWriter writer1 = new BufferedWriter(writer);
                for (String r : urlAdress) {
                    writer1.write(r + "\n");
                }
                writer1.close();
            } catch (IOException ee) {
                System.out.println("ERROR");
            }
        else {
            System.out.println("YOUR NUMBER IS OUT OF THE RANGE [1,4]\nPLEASE TRY AGAIN");
            welcoming();
        }
    }
    private void addURL ()
    {
        try {
            String URL = scanner.next();
            int flag = 1 ;
            String addUrl = extractPageTitle(fetchPageSource(URL)) + ";" + URL + ";" + extractRssUrl(URL) ;

            for (String r : urlAdress) {
                if(r.equals(addUrl))
                {
                    flag = 0;
                    break;
                }
            }
            if (flag == 1) {
                File file = new File("data.txt");
                FileWriter fileWriter = new FileWriter(file , true);
                BufferedWriter writer = new BufferedWriter(fileWriter);

                writer.write(addUrl + "\n");
                writer.close();
                urlAdress.add(addUrl);
                System.out.println("Added " + URL + " successfully.");

            }
            else
            {
                System.out.println(URL + " already exist.");
            }
        }
        catch (Exception e)
        {
            System.out.println("error to open file");
        }

        welcoming();
    }

    private void showUpdate ()
    {
        String [] a = new String[3];
        System.out.println("show update for :");
        int i = 0 ;
        System.out.println("[" + i + "]" + "all website" );
        for (String r :urlAdress)
        {
            i++;

            a = r.split(";");

            System.out.println("[" + i + "]" + a[0]);
        }

        int num = scanner.nextInt();

        if (num == 0) {
            for(String r : urlAdress)
            {
                a = r.split(";");

                System.out.println( a[0]);

                retrieveRssContent(a[2]);


            }
        }
        else
        {
            a = urlAdress.get(i - 1).split(";");

            System.out.println( a[0]);

            retrieveRssContent(a[2]);
        }
        welcoming();
    }
    private void removeURL ()
    {
        System.out.println("Please enter website URL to remove :");

        try {
            String URL = scanner.next();
            int flag = 1 ;
            String addUrl = extractPageTitle(fetchPageSource(URL)) + ";" + URL + ";" + extractRssUrl(URL) ;

            for (String r : urlAdress) {
                if(r.equals(addUrl))
                {
                    System.out.println("Removed " + URL + " successfully ");
                    urlAdress.remove(r);
                    flag = 0 ;
                    break;
                }
            }

            if (flag == 1)
            {
                System.out.println("Couldnt find " + URL);
            }

        }
        catch (Exception e)
        {
            System.out.println("error to open file");
        }


    }

    public static String extractPageTitle(String html)
    {
        try
        {
            org.jsoup.nodes.Document doc = Jsoup.parse(html);
            return doc.select("title").first().text();
        }
        catch (Exception e)
        {
            return "Error: no title tag found in page source!";
        }
    }

    public static void retrieveRssContent(String rssUrl)
    {
        try {
            String rssXml = fetchPageSource(rssUrl);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            StringBuilder xmlStringBuilder = new StringBuilder();
            xmlStringBuilder.append(rssXml);
            ByteArrayInputStream input = new ByteArrayInputStream(
                    xmlStringBuilder.toString().getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(input);
            NodeList itemNodes = doc.getElementsByTagName("item");

            for (int i = 0; i < Maximum; ++i) {
                Node itemNode = itemNodes.item(i);
                if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) itemNode;
                    System.out.println("Title: " + element.getElementsByTagName("title").item(0).getTextContent())
                    ;
                    System.out.println("Link: " + element.getElementsByTagName("link").item(0).getTextContent());
                    System.out.println("Description: " + element.getElementsByTagName("description").item(0).
                            getTextContent());
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("Error in retrieving RSS content for " + rssUrl + ": " + e.getMessage());
        }
    }
    public static String fetchPageSource(String urlString) throws Exception
    {
        URI uri = new URI(urlString);
        URL url = uri.toURL();
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML , like Gecko) Chrome/108.0.0.0 Safari/537.36");
        return toString(urlConnection.getInputStream());
    }
    private static String toString(InputStream inputStream) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream , "UTF-8"));
        String inputLine;
        StringBuilder stringBuilder = new StringBuilder();
        while ((inputLine = bufferedReader.readLine()) != null)
            stringBuilder.append(inputLine);

        return stringBuilder.toString();
    }

    public static String extractRssUrl(String url) throws IOException {
        org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
        return doc.select("[type='application/rss+xml']").attr("abs:href");
    }
}
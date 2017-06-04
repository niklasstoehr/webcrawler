import java.util.Stack;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedWriter;
import java.io.File;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.OutputStreamWriter;
import org.xml.sax.helpers.DefaultHandler;



// functionality in one class
public class MainClass extends DefaultHandler {
	
	
	//______________________________________________
	// Main Method at the very end
	// input XML file, same folder; "ndswiki.xml"
	// output
	// Kategorie
	
	
	// parameters
	StringBuilder builder1 = new StringBuilder();
	public static String Name = "output";

	
	
	// Save
	public static void SaverMethod (String Name,String Head,String []LinkParser, int length) {
		
		Writer outWriter = null;
		 
		if(length==0)
			 return;
		 
		try {
			
			outWriter = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(Name,true), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
			try {
				
				 
				Head = Head.replaceAll("Kategorie:", "");//
				
				outWriter.write(Head+"\t");
				
				for (  int k=0; k<length; k++) {
					
					LinkParser[k]=LinkParser[k].replaceAll("Category:", "");
					LinkParser[k]=LinkParser[k].replaceAll("Kategorie:", "");
					LinkParser[k]=LinkParser[k].replaceAll("| ","");
						outWriter.write(LinkParser[k]);
						outWriter.write(";");
				}
				
				outWriter.write("\n");
				
			} catch (IOException e) {
				
				e.printStackTrace();
				
			} finally {
				
			    try {
			    	
					outWriter.close();
				} catch (IOException e) {
					
					e.printStackTrace();
					
				}
			}
		
	}

	public static void ContentParser (String Head, String body){
		
		// Data
		String tempContent = body;
		
		// maximum array length
		String [] ArticleLink = new String[999999];
		String [] ArticleCategory = new String[999999];

		// Counts
		int start=0;
		int finish=0;
		int count1 = 0;
		int count2 = 0;
		
		while( start != -1 ){
			
			start=tempContent.indexOf("[[");
			finish=tempContent.indexOf("]]");
			
			if (start == -1 || finish == -1) {
				break;
			}
			
			if (start < finish) {	
				
				String tempContent2= tempContent.substring(start+2, finish);
					
				
					// Kategorie
					if (tempContent2.contains("Category:") || tempContent2.contains("Wàll")) {
						ArticleCategory[count2] = tempContent2;
						count2 ++;
					}
							
					else{
						
						ArticleLink[count1]=tempContent2;
						count1++;
					}
			}		
			tempContent = tempContent.substring(finish+2);
			
		}
 
	 
		String buildPath ="";
		
		// wowiki
		 if (Head.contains("Wàll") || Head.contains("Categorie") )
		 
		 {
			 buildPath="output/category-parent.txt";
			 
			 SaverMethod(buildPath,Head,ArticleCategory,count2);
		 
		 } else {
			 
			 buildPath="output/article-category.txt";
			 
			 SaverMethod(buildPath,Head,ArticleCategory,count2);
			 
			 SaverMethod("output/article-outlink.txt",Head,ArticleLink,count1);
			 
		 }
	}
	
	// Save
	public static void SaveInfoBox(String Name,int length,String Head, String []Box) {
		
		Writer outWriter = null;
		
		try {
			
			outWriter = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(Name,true), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
			try {
				
				 
				outWriter.write(Head+"\t");
				String temp3 = "";
				for (  int x = 0; x < length ;x++ ) {
					
					temp3 = Box[x].substring(0, Box[x].indexOf("\n")+1);
					Box[x]=Box[x].replaceAll("=","::::=");
					Box[x]=Box[x].replaceAll(" +"," ");
					Box[x]=Box[x].replaceAll(" :",":");
					Box[x]=Box[x].replace(temp3, "");
					Box[x]=Box[x].replace("\n", "::::;");
					Box[x]=Box[x].replace(";|", ";");
					Box[x]=Box[x].replace(":|", ":");
					Box[x]=Box[x].replace("</ref>", "");
					Box[x]=Box[x].replace("<ref>", "");
					Box[x]=Box[x].replace("<br>", "");				 
					Box[x]=Box[x].replaceAll("= ","=");
					Box[x]=Box[x].replace(" ;", ";");
					Box[x]=Box[x].replace("; ", ";");
					temp3 = temp3.replace("\n",":::::");
					
					// Write
					outWriter.write(temp3);
					outWriter.write(Box[x]);
					
					 
				}
				outWriter.write("\n");
				
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			    try {
			    	outWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}	
	
	
			// Infobox
			public static void ParserInfobox(String Head, String body) {
				
				int start = body.indexOf("{{Infobox");
				String [] InfoBoxes = new String[10000];
				int counter=0;

				if(start==-1)
					return;
				
				int endIndex=0;
				
				Stack<String> stack = new Stack<String>();
				stack.push("{");
				stack.push("{");
				 
				
				while( start!=-1 ){
					
					 for(int a = start+2; a < body.length(); a++){
						 
						if (Character.toString(body.charAt(a)).equals("{")) 
							stack.push("{");
						
						if (Character.toString(body.charAt(a)).equals("}")) 
							stack.pop();
						if(stack.isEmpty())
							{
								endIndex=a;
								break;
							}
						 		
					 }
		 			 
					 InfoBoxes [counter] = body.substring(start+2, endIndex-2);
					 
					 counter++;
					 
					 start=body.indexOf("{{Info",endIndex);
					 stack.push("{");
					 
					 stack.push("{");
					 
				}
		 
				SaveInfoBox("output/article-infobox.txt", counter, Head,InfoBoxes);

			}


			

			// Main Method
			//_______________________________________________________________________________________
			
			public static void main(String[] args) {
			
						// start program
						System.out.println("Wikipedia Dumbfile Parser" + "\n" + "The parsing process has been started" + "\n" + "Program uses SAX Parser" + "\n"+ "still parsing..." + "\n");
						
						// initiate new SAX Parser Factory
						SAXParserFactory saxParserFactory =SAXParserFactory.newInstance();
						
						// new SAX Parser
						SAXParser saxParser = null;
						
						try {	
							
							saxParser = saxParserFactory.newSAXParser();
							 
						} catch (ParserConfigurationException e1) {
							
						  e1.printStackTrace();
						  
						} catch (SAXException e1) {
							
						  e1.printStackTrace();
						  
						}
						
						// create output folder
						File directory = new File("output");
				       
						if (!directory.exists()) {
							
				            if (directory.mkdir()) {
				                System.out.println("Directory is created!");
				            } else {
				                System.out.println("Failed to create directory!");
				            }
				        }
					
						for(File file: directory.listFiles()) {
						    if (!file.isDirectory()) 
						        file.delete(); 
						}
				
						
				
				
				// _________________________________________________________________________________________
				
				// Methods of DefaultHandler
				
				DefaultHandler handler= new DefaultHandler(){
					
					boolean ArticleText =false;
					 String Tag="";
					
					 // first builder sb
					 StringBuilder sb = new StringBuilder();
					 boolean articleTitle =false;
					
					 
					 
					// Defaulthandler method; character 
					public void characters(char [] ch, int start, int length) throws SAXException{
						if (articleTitle){
							
							StringBuilder TempStr = new StringBuilder();
							System.out.println("article title = "+ new String (ch, start, length));
							
							for (int z =start; z<start+length;z++)
								TempStr.append(ch[z]);
							
							  Tag=TempStr.toString();					 
							  articleTitle=false;
					
						}
						
						if (ArticleText){
							
							for (int y =start;y<start+length;y++)
								
								{sb.append(ch[y]);
									 
								}
						 
						}
					}
			
			
			
					// Defaulthandler method; start  
					public void startElement (String uri,String localName, String TageName, Attributes attributes)throws SAXException{
						
							if (TageName.equalsIgnoreCase("text"))
								ArticleText=true;
							
							if (TageName.equalsIgnoreCase("title"))
								articleTitle=true;
							
						}
					
					
					
						// Defaulthandler method; end 
						public void endElement(String uri,String localName, String TagName)throws SAXException{
							
			 					if (TagName.equalsIgnoreCase("text")){
			 						
			 						/* Within text find further information
			 						 * (1) Links 
			 						 * (2) Infobox	 
			 						 */
			 						
			 						// (1) Link Parser 
			 						// create instance LinkParser
			 						ContentParser(Tag,sb.toString());
			 						
			 						// (2) Infobox Parser
			 						// create instance InfoboxParser
			 						ParserInfobox(Tag,sb.toString());
			 						
			 						
			 						// set parameters back again
			 						
			 						// close text
			 						ArticleText = false;
			 						sb.setLength(0);
			 						sb= new StringBuilder();
			 					}
			 						
						}
						
					};
					  
					
					// File wowiki
					
					try {
						
						saxParser.parse("wowiki.xml", handler);
						 
					} catch (SAXException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
			
					System.out.println("parsed");
			}	 
}

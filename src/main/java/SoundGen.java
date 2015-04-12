import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class SoundGen {

	public static void main(String[] args) {
        try
        {
            ZipFile zip = new ZipFile("F:/dev/code/old workspaces/forge-1.7.10-tropicraft/pkg/assets.zip");

            Enumeration<? extends ZipEntry> entries = zip.entries();

            ArrayList<String> soundNames = new ArrayList<String>();
            String initialPath = "assets/tropicraft/";

            while(entries.hasMoreElements())
            {
                ZipEntry entry = entries.nextElement();

                if(!entry.isDirectory() && entry.toString().endsWith(".ogg"))
                {
                    String name = entry.toString();
                    name = name.substring(initialPath.length()); //remove the file structure
                    if(name.startsWith("sounds"))
                    {
                        name = name.substring("sounds".length() + 1);
                    }
                    soundNames.add(name.substring(0, name.length() - 4)); //remove the OGG
                }
            }

            HashMap<String, ArrayList<String>> numbered = new HashMap<String, ArrayList<String>>();

            for(int i = 0; i < soundNames.size(); i++)
            {
                String name = soundNames.get(i);
                String oriName = name;
                boolean isNum = true;
                boolean isNumbered = false;
                while(isNum)
                {
                    try
                    {
                        Integer.parseInt(name.substring(name.length() - 1));
                        name = name.substring(0, name.length() - 1);
                        isNumbered = true;
                    }
                    catch(NumberFormatException e)
                    {
                        isNum = false;
                    }
                }
                if(isNumbered)
                {
                    ArrayList<String> numbers = numbered.get(name);
                    if(numbers == null)
                    {
                        numbers = new ArrayList<String>();
                        numbered.put(name, numbers);
                    }
                    numbers.add(oriName);
                    Collections.sort(numbers);
                }
            }

            for(int i = soundNames.size() - 1; i >= 0; i--)
            {
                String name = soundNames.get(i);
                for(Map.Entry<String, ArrayList<String>> e : numbered.entrySet())
                {
                    if(name.startsWith(e.getKey()) && !name.equals(e.getKey()))
                    {
                        soundNames.remove(i);
                        if(!soundNames.contains(e.getKey()))
                        {
                            soundNames.add(i, e.getKey());
                        }
                    }
                }
            }

            StringBuilder sb = new StringBuilder();
            sb.append("\n\n");
            sb.append("{\n");

            for(int i = 0; i < soundNames.size(); i++)
            {
                String name = soundNames.get(i);
                String oriName = name;
                boolean isRecord = name.startsWith("records");
                String category = isRecord ? "record" : name.contains("wpn_portal") ? "player" : "neutral"; //"ambient", "weather", "player", "neutral", "hostile", "block", "record", "music", and "master"
                String ingameName = name.replaceAll("\\/", ".");

                sb.append("  \"" + ingameName + "\": { \n");

                //ELEMENTS HERE

                sb.append("    \"category\": \"" + category + "\",\n");
                sb.append("    \"sounds\": [\n");
                if(isRecord)
                {
                    sb.append("      {\n");

                    sb.append("        \"name\": \"" + name + "\",\n        \"stream\": true\n");
                    sb.append("      }\n");
                }
                else
                {
                    ArrayList<String> soundPaths = numbered.get(oriName);
                    if(soundPaths == null)
                    {
                        soundPaths = new ArrayList<String>();
                        soundPaths.add(name);
                    }
                    for(int j = 0; j < soundPaths.size(); j++)
                    {
                        sb.append("      \"" + soundPaths.get(j) + "\"");
                        if(j != soundPaths.size() - 1)
                        {
                            sb.append(",\n");
                        }
                        else
                        {
                            sb.append("\n");
                        }
                    }
                }
                sb.append("    ]\n");
                sb.append("  }");
                if(i != soundNames.size() - 1)
                {
                    sb.append(",\n");
                }
                else
                {
                    sb.append("\n");
                }
            }

            sb.append("}");

            System.out.println(sb.toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

	}

}

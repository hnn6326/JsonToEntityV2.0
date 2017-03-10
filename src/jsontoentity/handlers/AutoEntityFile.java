package jsontoentity.handlers;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jsontoentity.handlers.JsonCheck.JSON_TYPE;
/**
 * 自动生成entity 文件，只支持jsonobject
 * @author zhang
 * @date 2015-7-13
 */
public class AutoEntityFile {
	@Deprecated
	public static String createFileFromJson(String json,String className,boolean isCreateFile)
	{
		return createFileFromJson(json, className,null,isCreateFile);
	}
	@Deprecated
	public static String createFileFromJson(String json,String className,String packName,boolean isCreateFile)
	{
		if(StringUtils.isEmpty(json))return "";
		JSON_TYPE jsonType= JsonCheck.getJSONType(json);
		System.out.println(jsonType);
		String entityContent="";
		if(!StringUtils.isEmpty(packName))
		{
			entityContent+=packName.startsWith("package")?packName:"package "+packName;
			entityContent+=";\n";
		}
		className=StringUtils.isEmpty(className)?"EntityClass":className;
		entityContent+="public class "+className+"{\n";
		List<String> keys=new ArrayList<String>();
		if(jsonType==JSON_TYPE.JSON_TYPE_OBJECT)
		{
			JsonObject jsonObject=new JsonParser().parse(json).getAsJsonObject();
			Set<Entry<String, JsonElement>>	fieldSet=jsonObject.entrySet();
			Iterator<Entry<String, JsonElement>> iterator= fieldSet.iterator();
			while (iterator.hasNext()) {
				Entry<String, JsonElement> entry = iterator.next();
				String key=entry.getKey();
				JsonElement element=entry.getValue();
				if(element.isJsonPrimitive()||element.isJsonNull())
				{
					keys.add(key);
					entityContent+="    private String "+key+";\n";
				}
			}
		}
		entityContent+="\n";
		for(String fieldName:keys)
		{
			entityContent+="    public void set"+(fieldName.charAt(0)+"").toUpperCase()+fieldName.substring(1)+"(String "+fieldName+"){\n";
			entityContent+="        this."+fieldName+"="+fieldName+";\n    }\n";
			entityContent+="\n";
			entityContent+="    public String get"+(fieldName.charAt(0)+"").toUpperCase()+fieldName.substring(1)+"(){\n";
			entityContent+="        return "+fieldName+";\n    }\n";
		}
		entityContent+="}";
		if(isCreateFile)
		{
			File file=new File(className+".java");
			try {
				if(!file.exists())
				{
					file.createNewFile();
				}
				FileOutputStream fileOutputStream=new FileOutputStream(file);
				fileOutputStream.write(entityContent.getBytes());
				fileOutputStream.flush();
				fileOutputStream.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return entityContent;
	}
	/**
	 * json 生成实体类内容
	 * @param json
	 * @param className
	 * @return
	 */
	public static String getEntityContent(String json,String className)
	{
		if(StringUtils.isEmpty(json))return "";
		JSON_TYPE jsonType= JsonCheck.getJSONType(json);
		System.out.println(jsonType);
		String entityContent="";
		className=StringUtils.isEmpty(className)?"EntityClass":className;
		entityContent+="public class "+className+"{\n";
		List<String> keys=new ArrayList<String>();
		if(jsonType==JSON_TYPE.JSON_TYPE_OBJECT)
		{
			JsonObject jsonObject=new JsonParser().parse(json).getAsJsonObject();
			Set<Entry<String, JsonElement>>	fieldSet=jsonObject.entrySet();
			Iterator<Entry<String, JsonElement>> iterator= fieldSet.iterator();
			while (iterator.hasNext()) {
				Entry<String, JsonElement> entry = iterator.next();
				String key=entry.getKey();
				JsonElement element=entry.getValue();
				if(element.isJsonPrimitive()||element.isJsonNull())
				{
					keys.add(key);
					entityContent+="    private String "+key+";\n";
				}else if(element.isJsonArray())
				{
					JsonArray jsonArray=element.getAsJsonArray();
					entityContent+=dealJsonArray(jsonArray,key);
				}
			}
		}else if(jsonType==JSON_TYPE.JSON_TYPE_ARRAY)
		{
			JsonArray jsonArray=new JsonParser().parse(json).getAsJsonArray();
			//entityContent=dealJsonArray(jsonArray,className);
			if(jsonArray.size()>0)
			{
				JsonElement temp=jsonArray.get(0);
				if(temp.isJsonPrimitive()||temp.isJsonNull())
				{
					//只有一个字符串，忽略了
				}else
				{
					return getEntityContent(temp.toString(),className);
				}
			}
		}
		entityContent+="\n";
		for(String fieldName:keys)
		{
			entityContent+=getSetterGetterStrs("String",fieldName);
		}
		entityContent+="}";
		return entityContent;
	}
	/**
	 * 处理jsonArray
	 * @param jsonArray
	 * @param key
	 * @return
	 */
	private static String dealJsonArray(JsonArray jsonArray,String key)
	{
		String entityContent="";
		if(jsonArray.size()>0)
		{
			JsonElement temp=jsonArray.get(0);
			if(temp.isJsonPrimitive()||temp.isJsonNull())
			{
				entityContent+="    private List<String> "+key+";\n";
				entityContent+=getSetterGetterStrs("List<String>",key);
			}else
			{
				String innerClassName=(key.charAt(0)+"").toUpperCase()+key.substring(1);
				entityContent+="    private List<"+innerClassName+"> "+key+";\n";
				entityContent+=getSetterGetterStrs("List<"+innerClassName+">",key);
				entityContent+=getEntityContent(temp.toString(),innerClassName);
			}
		}
		return entityContent;
	}
	/**
	 * 获取getter setter 方法
	 * @param returnTypeStr
	 * @param fieldName
	 * @return
	 */
	private static String getSetterGetterStrs(String returnTypeStr,String fieldName)
	{
		String content="";
		content+="    public void set"+(fieldName.charAt(0)+"").toUpperCase()+fieldName.substring(1)+"("+returnTypeStr+" "+fieldName+"){\n";
		content+="        this."+fieldName+"="+fieldName+";\n    }\n";
		content+="\n";
		content+="    public "+returnTypeStr+" get"+(fieldName.charAt(0)+"").toUpperCase()+fieldName.substring(1)+"(){\n";
		content+="        return "+fieldName+";\n    }\n";
		return content;
	}
	
}

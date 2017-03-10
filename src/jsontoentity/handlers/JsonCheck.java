package jsontoentity.handlers;

/**
 * 
 * @author zhang
 * @date 2015-6-8
 */
public class JsonCheck {
	/**
	 * json类型
	 */
	public enum JSON_TYPE {
		/** JSONObject */
		JSON_TYPE_OBJECT,
		/** JSONArray */
		JSON_TYPE_ARRAY,
		/** 不是JSON格式的字符串 */
		JSON_TYPE_ERROR
	}

	/***
	 * 
	 * 获取JSON类型 判断规则 判断第一个字母是否为{或[ 如果都不是则不是一个JSON格式的文本
	 * 
	 * @param str
	 * @return
	 */
	public static JSON_TYPE getJSONType(String jsonStr) {
		if (jsonStr == null || jsonStr.trim().equals("")) {
			return JSON_TYPE.JSON_TYPE_ERROR;
		}
		jsonStr=jsonStr.trim();
		final char[] strChar = jsonStr.substring(0, 1).toCharArray();
		final char firstChar = strChar[0];
		if (firstChar == '{') {
			return JSON_TYPE.JSON_TYPE_OBJECT;
		} else if (firstChar == '[') {
			return JSON_TYPE.JSON_TYPE_ARRAY;
		} else {
			return JSON_TYPE.JSON_TYPE_ERROR;
		}
	}

}

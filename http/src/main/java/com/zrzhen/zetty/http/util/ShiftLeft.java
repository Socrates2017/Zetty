package com.zrzhen.zetty.http.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 字符串左移,如abcdefghi左移4位后为efghiabcd
 * @author chenanlian
 */
public class ShiftLeft {
	static String reverStr(String s) {
		int low = 0;
		int high = s.length() - 1;
		char[] temp = new char[s.length()];
		while (low <= high) {
			temp[low] = s.charAt(high);
			temp[high] = s.charAt(low);
			low++;
			high--;
		}
		return String.valueOf(temp);
	}

	/**
	 * 
	 * @param s
	 *            要移位的字符串
	 * @param digits
	 *            向左移动的位数
	 */
	public static String shift(String s, int digits) {
		if (digits == 0) {
			return s;
		} else if (digits > 0) {
			digits = digits % s.length();
			String left = reverStr(s.substring(0, digits));
			String right = reverStr(s.substring(digits));
			String result = reverStr(left + right);
			return result;
		} else { // 此时变为向右移digits位,即向左移动s.length() - digits位
			digits = -digits;
			digits = digits % s.length();
			return shift(s, s.length() - digits);
		}
	}

	public static void main(String[] args) {
		String s2 = "abcdefghi";
		System.out.println(shift(s2, -4));
		String s = "/v1/user/info/get201407101234342f39d871a38a4841aab3be3837e39cf4";
		System.out.println(shift(s, -16));
		System.out.println(DigestUtils.md5Hex(shift(s, -16)));
		String s1 = "lentone";
		System.out.println(shift(s1, -5));
		System.out.println(DigestUtils.md5Hex(shift(s1, -5)));
		System.out.println(DigestUtils.md5Hex(ShiftLeft.shift("123456", -5)));
	}

}
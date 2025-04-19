package com.example.travalms.utils

import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination
import android.util.Log

/**
 * 拼音工具类，用于处理汉字转拼音功能
 */
object PinyinUtils {
    private const val TAG = "PinyinUtils"
    
    /**
     * 获取汉字的拼音首字母（大写）
     * 如果字符串中有多个汉字，只返回第一个汉字的拼音首字母
     * 如果不是汉字，则原样返回字符
     */
    fun getFirstLetter(chinese: String): String {
        if (chinese.isEmpty()) return ""
        
        // 先处理第一个字符
        val firstChar = chinese[0]
        
        // 检查是否为汉字（Unicode编码范围）
        if (firstChar.toInt() in 0x4E00..0x9FA5) {
            try {
                val format = HanyuPinyinOutputFormat().apply {
                    caseType = HanyuPinyinCaseType.UPPERCASE
                    toneType = HanyuPinyinToneType.WITHOUT_TONE
                }
                
                val pinyinArray = PinyinHelper.toHanyuPinyinStringArray(firstChar, format)
                if (pinyinArray != null && pinyinArray.isNotEmpty()) {
                    // 取第一个拼音的首字母
                    val firstLetter = pinyinArray[0][0].toString()
                    Log.d(TAG, "汉字 '$firstChar' 的拼音首字母: $firstLetter")
                    return firstLetter
                }
            } catch (e: BadHanyuPinyinOutputFormatCombination) {
                Log.e(TAG, "获取拼音时出错: ${e.message}")
            }
        }
        
        // 非汉字或处理出错时，返回字符本身的大写形式
        return firstChar.toString().uppercase()
    }
    
    /**
     * 判断一个字符是否是汉字
     */
    fun isChineseChar(c: Char): Boolean {
        return c.toInt() in 0x4E00..0x9FA5
    }
    
    /**
     * 判断一个字符串是否包含汉字
     */
    fun containsChinese(str: String): Boolean {
        return str.any { isChineseChar(it) }
    }
} 
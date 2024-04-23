import java.util.Stack;

/**
 * @author Me
 * @version 1.0
 * @date 2024/4/12 12:21
 * @Description:最长公共子序列
 */

public class ld_lcs_algorithm {
    public static String getLCS(String str1, String str2) {

        char[] s1 = str1.toCharArray();
        char[] s2 = str2.toCharArray();
        int[][] array = new int[str1.length() + 1][str2.length() + 1]; // 此处的棋盘长度要比字符串长度多加1

        for (int j = 0; j < array[0].length; j++) { // 第0行第j列全部赋值为0
            array[0][j] = 0;
        }
        for (int i = 0; i < array.length; i++) { // 第i行第0列全部赋值为0
            array[i][0] = 0;
        }

        for (int m = 1; m < array.length; m++) { // 利用动态规划将数组赋满值
            for (int n = 1; n < array[m].length; n++) {
                if (s1[m - 1] == s2[n - 1]) {
                    array[m][n] = array[m - 1][n - 1] + 1; // 动态规划公式一
                } else {
                    array[m][n] = max(array[m - 1][n], array[m][n - 1]); // 动态规划公式二
                }
            }
        }
        //  printArray(array);

        Stack<Character> stack = new Stack<Character>();
        int i = str1.length() - 1;
        int j = str2.length() - 1;

        while ((i >= 0) && (j >= 0)) {
            if (s1[i] == s2[j]) { // 字符串从后开始遍历，如若相等，则存入栈中
                stack.push(s1[i]);
                i--;
                j--;
            } else {
                // 如果字符串的字符不同，则在数组中找相同的字符
                // 注意：数组的行列要比字符串中字符的个数大1，因此i和j要各加1
                if (array[i + 1][j] > array[i][j + 1]) {
                    j--;
                } else {
                    i--;
                }
            }
        }

        // 打印输出栈正好是正向输出最大的公共子序列
        String result = "";
        while (!stack.isEmpty()) {
            result += stack.pop().toString();
        }
        return result;
    }

    /**
     * @description 比较(a,b)，输出大的值
     * @param a
     * @param b
     * @return
     */
    public static int max(int a, int b) {
        return (a > b) ? a : b;
    }
    public static int ld(String str1, String str2) {
        int d[][]; // 矩阵
        int n = str1.length();
        int m = str2.length();
        int i; // 遍历str1的
        int j; // 遍历str2的
        char ch1; // str1的
        char ch2; // str2的
        int temp; // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        d = new int[n + 1][m + 1];
        for (i = 0; i <= n; i++) { // 初始化第一列
            d[i][0] = i;
        }
        for (j = 0; j <= m; j++) { // 初始化第一行
            d[0][j] = j;
        }
        for (i = 1; i <= n; i++) { // 遍历str1
            ch1 = str1.charAt(i - 1);
            // 去匹配str2
            for (j = 1; j <= m; j++) {
                ch2 = str2.charAt(j - 1);
                if (ch1 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                // 左边+1,上边+1, 左上角+temp取最小
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }
        return d[n][m];
    }

    private static int min(int one, int two, int three) {
        int min = one;
        if (two < min) {
            min = two;
        }
        if (three < min) {
            min = three;
        }
        return min;
    }

    public static double sim(String str1, String str2) {
        int ld = ld(str1, str2);
        return 1 - (double) ld / Math.max(str1.length(), str2.length());
    }
}

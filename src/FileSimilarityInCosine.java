import java.util.HashMap;
import java.util.Map;
/**
 * @Description:利用文件余弦夹角度量算法实现代码及文本的相似度计算
 */

public class FileSimilarityInCosine {
    // 计算文件相似度
    public static double calculate(String content1, String content2) {
        // 获取文本内容的词频向量
        Map<String, Integer> vector1 = getTermFrequency(content1);
        Map<String, Integer> vector2 = getTermFrequency(content2);

        // 计算向量的点积
        double dotProduct = calculateDotProduct(vector1, vector2);
        // 计算向量的模
        double magnitude1 = calculateMagnitude(vector1);
        double magnitude2 = calculateMagnitude(vector2);

        // 计算相似度
        return dotProduct / (magnitude1 * magnitude2);
    }

    // 计算词频
    private static Map<String, Integer> getTermFrequency(String content) {
        Map<String, Integer> termFrequency = new HashMap<>();
        // 将文本内容按空白字符分割为单词数组
        String[] words = content.split("\\s+");

        // 统计每个单词的出现频率
        for (String word : words) {
            termFrequency.put(word, termFrequency.getOrDefault(word, 0) + 1);
        }

        return termFrequency;
    }

    // 计算向量的点积
    private static double calculateDotProduct(Map<String, Integer> vector1, Map<String, Integer> vector2) {
        double dotProduct = 0;

        // 遍历较小的向量
        Map<String, Integer> smallerVector = vector1.size() < vector2.size() ? vector1 : vector2;
        Map<String, Integer> largerVector = vector1.size() >= vector2.size() ? vector1 : vector2;

        // 计算点积
        for (Map.Entry<String, Integer> entry : smallerVector.entrySet()) {
            String term = entry.getKey();
            int frequency1 = entry.getValue();
            int frequency2 = largerVector.getOrDefault(term, 0);

            dotProduct += frequency1 * frequency2;
        }

        return dotProduct;
    }

    // 计算向量的模
    private static double calculateMagnitude(Map<String, Integer> vector) {
        double magnitudeSquared = 0;

        // 计算向量模的平方
        for (int frequency : vector.values()) {
            magnitudeSquared += frequency * frequency;
        }

        // 对模的平方进行开方，得到模的值
        return Math.sqrt(magnitudeSquared);
    }
}
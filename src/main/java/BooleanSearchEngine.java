import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {

    private Map<String, List<PageEntry>> listMap = new HashMap<>();
    private List<PageEntry> pageEntryResult = new ArrayList<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        for (File file : pdfsDir.listFiles()) {
            PdfDocument doc = new PdfDocument(new PdfReader(file));
            for (int i = 1; i <= doc.getNumberOfPages(); ++i) {
                var text = PdfTextExtractor.getTextFromPage(doc.getPage(i));
                String pdfName = file.getName();
                int page = i;
                var words = text.split("\\P{IsAlphabetic}+");
                Map<String, Integer> freq = new HashMap<>();
                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    freq.put(word, freq.getOrDefault(word, 0) + 1);
                }

                for (String word : freq.keySet()) {
                    PageEntry pageEntry = new PageEntry(file.getName(), page, freq.get(word));
                    if (!listMap.containsKey(word)) {
                        List<PageEntry> pageEntryList = new ArrayList<>();
                        pageEntryList.add(pageEntry);
                        listMap.put(word, pageEntryList);
                    } else {
                        listMap.get(word).add(pageEntry);
                    }

                    pageEntryResult = new ArrayList<>(listMap.get(word.toLowerCase()));
                    pageEntryResult.sort(Collections.reverseOrder());
                }
            }
        }

    }

    @Override
    public List<PageEntry> search(String word) {
        // тут реализуйте поиск по слову
        if ((word == null) || (word.isEmpty())) { // проверка на отсутствие искомого слова
            return Collections.emptyList();
        }

        // TODO: 19.12.2022 перенесено в цикл сортировки for (String word : freqs.keySet()) в методе BooleanSearchEngine(File pdfsDir)
//        pageEntryResult = new ArrayList<>(listMap.get(word.toLowerCase()));
//        pageEntryResult.sort(Collections.reverseOrder()); //метод sort(), который сортирует массив в порядке возрастания.
        // Для сортировки массива в порядке убывания метод reverseOrder() класса Collections.
        return pageEntryResult;
    }
}
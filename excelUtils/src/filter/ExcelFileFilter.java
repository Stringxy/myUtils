package filter;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Created by xy on 2017/6/28.
 */
public class ExcelFileFilter extends FileFilter {
    public String getDescription() {
        return "*.xls;*.xlsx";
    }

    public boolean accept(File file) {
        String name = file.getName();
        return file.isDirectory() || name.toLowerCase().endsWith(".xls") || name.toLowerCase().endsWith(".xlsx");  // 仅显示目录和xls、xlsx文件
    }
}

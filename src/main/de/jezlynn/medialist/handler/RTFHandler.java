package main.de.jezlynn.medialist.handler;

import com.tutego.jrtf.*;
import main.de.jezlynn.medialist.exceptions.NoXMLFile;
import main.de.jezlynn.medialist.helper.LogHelper;
import main.de.jezlynn.medialist.helper.XMLHelper;
import main.de.jezlynn.medialist.reference.Reference;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static com.tutego.jrtf.RtfPara.p;
import static com.tutego.jrtf.RtfPara.row;
import static com.tutego.jrtf.RtfText.picture;

/**
 * Created by Michael on 24.08.2014.
 */
public class RTFHandler {

    private final Rtf rtf = Rtf.rtf();

    public RTFHandler() {
        this.initFile();
    }

    private void initFile() {
        rtf.info(RtfInfo.author("Michael Schlittenbauer"), RtfInfo.title("Liste aller Filme"));
    }

    public void createDocument(Map<String, File> directorys) {
        List<RtfPara> rows = new LinkedList<RtfPara>();
        rows.add(row("", "Filmtitel", "Beschreibung", "FSK"));
        FileHandler fileHandler;
        Map<String, File> files;
        for (String fileName : directorys.keySet()) {
            fileHandler = new FileHandler(directorys.get(fileName).getPath());
            files = fileHandler.getFiles();
            HashMap<String, String> data = readXML(files);
            rows.add(row(addImage(files), fileName, data.get(Reference.TAG_DESCRIPTION), data.get(Reference.TAG_FSK)));
        }
        rows.add(endFile());
        rtf.section(rows);
    }

    private RtfPara addImage(Map<String, File> files) {
        for (String fileName : files.keySet())
            if (fileName.contains(Reference.IMAGE_IDENTIFIER)) {
                LogHelper.debug(fileName);
                LogHelper.debug(files.get(fileName).getAbsolutePath());
                try {
                    InputStream is = new FileInputStream(files.get(fileName));
                    Dimension dimension = this.getImageDimensions(files.get(fileName));
                    LogHelper.debug("Width of the image: " + dimension.getWidth() + " Height of the image: " + dimension.getHeight());
                    return p(picture(is).size(dimension.getWidth(), dimension.getHeight(), RtfUnit.CM).type(RtfPicture.PictureType.AUTOMATIC));
                } catch (Exception e) {
                    LogHelper.error(e);
                }
            }
        return null;
    }

    private HashMap<String, String> readXML(Map<String, File> files) {
        for (String fileName : files.keySet())
            if (fileName.contains(Reference.INFO_IDENTIFIER)) {
                try {
                    XMLHelper xmlHelper = new XMLHelper(files.get(fileName).getAbsolutePath());
                    return xmlHelper.getData();
                } catch (NoXMLFile e) {
                    LogHelper.error(e);
                }
            }
        return new HashMap<String, String>();
    }

    private RtfPara endFile() {
        Date date = new Date(System.currentTimeMillis());
        DateFormat formatterDate = new SimpleDateFormat("dd.MM.yyyy");
        DateFormat formatterTime = new SimpleDateFormat("HH:mm:ss");
        return p("Zuletzt aktualisiert am: " + formatterDate.format(date) + " um " + formatterTime.format(date));
    }

    public void writeFile() throws IOException {
        rtf.out(new FileWriter("out.rtf"));
        LogHelper.info("Successfully wrote file");
    }

    private Dimension getImageDimensions(File resourceFile) throws IOException {
        ImageInputStream in = ImageIO.createImageInputStream(resourceFile);
        try {
            final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                try {
                    reader.setInput(in);
                    Dimension imageDimensions = new Dimension(reader.getWidth(0), reader.getHeight(0));
                    double scale = 1;
                    if (imageDimensions.getHeight() > imageDimensions.getWidth()) {
                        scale = imageDimensions.getHeight() / 3;
                    } else {
                        scale = imageDimensions.getWidth() / 3;
                    }
                    return new Dimension((int) (imageDimensions.getWidth() / scale), (int) (imageDimensions.getHeight() / scale));
                } finally {
                    reader.dispose();
                }
            }
        } finally {
            if (in != null) in.close();
        }
        return new Dimension(3, 3);
    }
}

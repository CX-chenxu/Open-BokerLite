package cn.BokerLite.utils;

import cn.BokerLite.Client;
import cn.BokerLite.gui.alt.gui.Alt;
import cn.BokerLite.gui.alt.gui.AltManager;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static cn.BokerLite.utils.Utils.mc;

@SuppressWarnings("ignored")
public class FileManager {
    private static File ALT;
    private static File LASTALT;
    public static File dir;

    public static void init() {
        final File mcDataDir = mc.mcDataDir;

        FileManager.dir = new File(mcDataDir, "BokerLite");
        ALT = getConfigFile("Alts");
        LASTALT = getConfigFile("LastAlt");
    }


    public static void loadLastAlt() {
        try {
            if (!FileManager.LASTALT.exists()) {
                final PrintWriter printWriter = new PrintWriter(new FileWriter(FileManager.LASTALT));
                printWriter.println();
                printWriter.close();
            } else if (FileManager.LASTALT.exists()) {
                final BufferedReader bufferedReader = new BufferedReader(new FileReader(FileManager.LASTALT));
                String s;
                while ((s = bufferedReader.readLine()) != null) {
                    if (s.contains("\t")) {
                        s = s.replace("\t", "    ");
                    }
                    if (s.contains("    ")) {
                        final String[] parts = s.split("    ");
                        final String[] account = parts[1].split(":");
                        if (account.length == 2) {
                            AltManager.setLastAlt(new Alt(account[0], account[1], parts[0]));
                        } else {
                            StringBuilder pw = new StringBuilder(account[1]);
                            for (int i = 2; i < account.length; ++i) {
                                pw.append(":").append(account[i]);
                            }
                            AltManager.setLastAlt(new Alt(account[0], pw.toString(), parts[0]));
                        }
                    } else {
                        final String[] account2 = s.split(":");
                        if (account2.length == 1) {
                            AltManager.setLastAlt(new Alt(account2[0], ""));
                        } else if (account2.length == 2) {
                            AltManager.setLastAlt(new Alt(account2[0], account2[1]));
                        } else {
                            StringBuilder pw2 = new StringBuilder(account2[1]);
                            for (int j = 2; j < account2.length; ++j) {
                                pw2.append(":").append(account2[j]);
                            }
                            AltManager.setLastAlt(new Alt(account2[0], pw2.toString()));
                        }
                    }
                }
                bufferedReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveLastAlt() {
        try {
            final PrintWriter printWriter = new PrintWriter(FileManager.LASTALT);
            final Alt alt = AltManager.getLastAlt();
            if (alt != null) {
                if (alt.getMask().equals("")) {
                    printWriter.println(alt.getUsername() + ":" + alt.getPassword());
                } else {
                    printWriter.println(
                            alt.getMask() + "    " + alt.getUsername() + ":" + alt.getPassword());
                }
            }
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void loadAlts() {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new FileReader(FileManager.ALT));
            if (!FileManager.ALT.exists()) {
                final PrintWriter printWriter = new PrintWriter(new FileWriter(FileManager.ALT));
                printWriter.println();
                printWriter.close();
            } else if (FileManager.ALT.exists()) {
                String s;
                while ((s = bufferedReader.readLine()) != null) {
                    if (s.contains("\t")) {
                        s = s.replace("\t", "    ");
                    }
                    if (s.contains("    ")) {
                        final String[] parts = s.split("    ");
                        final String[] account = parts[1].split(":");
                        if (account.length == 2) {

                            AltManager.getAlts().add(new Alt(account[0], account[1], parts[0]));
                        } else {
                            StringBuilder pw = new StringBuilder(account[1]);
                            for (int i = 2; i < account.length; ++i) {
                                pw.append(":").append(account[i]);
                            }

                            AltManager.getAlts().add(new Alt(account[0], pw.toString(), parts[0]));
                        }
                    } else {
                        final String[] account2 = s.split(":");
                        if (account2.length == 1) {

                            AltManager.getAlts().add(new Alt(account2[0], ""));
                        } else if (account2.length == 2) {
                            try {

                                AltManager.getAlts().add(new Alt(account2[0], account2[1]));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            StringBuilder pw2 = new StringBuilder(account2[1]);
                            for (int j = 2; j < account2.length; ++j) {
                                pw2.append(":").append(account2[j]);
                            }

                            AltManager.getAlts().add(new Alt(account2[0], pw2.toString()));
                        }
                    }
                }
            }
            bufferedReader.close();
        } catch (Exception ignored) {
        }
    }

    public static void saveAlts() {
        try {
            final PrintWriter printWriter = new PrintWriter(FileManager.ALT);
            for (final Alt alt : AltManager.getAlts()) {
                if (alt.getMask().equals("")) {
                    printWriter.println(alt.getUsername() + ":" + alt.getPassword());
                } else {
                    printWriter.println(
                            alt.getMask() + "    " + alt.getUsername() + ":" + alt.getPassword());
                }
            }
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static File getConfigFile(final String name) {
        final File file = new File(FileManager.dir, String.format("%s.txt", name));
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                //
            }
        }
        return file;
    }


    public static void save(final String file, final String content, final boolean append) {
        try {
            final File f = new File(FileManager.dir, file);
            if (!f.exists()) {
                f.createNewFile();
            }
            Throwable t = null;
            try {
                final FileWriter writer = new FileWriter(f, append);
                try {
                    writer.write(content);
                }
                finally {
                    if (writer != null) {
                        writer.close();
                    }
                }
            }
            finally {
                if (t == null) {
                    final Throwable t2 = null;
                    t = t2;
                }
                else {
                    final Throwable t2 = null;
                    if (t != t2) {
                        t.addSuppressed(t2);
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static List<String> read(final String file) {
        final List<String> out = new ArrayList<String>();
        try {
            if (!FileManager.dir.exists()) {
                FileManager.dir.mkdir();
            }
            final File f = new File(FileManager.dir, file);
            if (!f.exists()) {
                f.createNewFile();
            }
            Throwable t = null;
            try {
                final FileInputStream fis = new FileInputStream(f);
                try {
                    final InputStreamReader isr = new InputStreamReader(fis);
                    try {
                        final BufferedReader br = new BufferedReader(isr);
                        try {
                            String line = "";
                            while ((line = br.readLine()) != null) {
                                out.add(line);
                            }
                        }
                        finally {
                            if (br != null) {
                                br.close();
                            }
                        }
                        if (isr != null) {
                            isr.close();
                        }
                    }
                    finally {
                        if (t == null) {
                            final Throwable t2 = null;
                            t = t2;
                        }
                        else {
                            final Throwable t2 = null;
                            if (t != t2) {
                                t.addSuppressed(t2);
                            }
                        }
                        if (isr != null) {
                            isr.close();
                        }
                    }
                    if (fis != null) {
                        fis.close();
                        return out;
                    }
                }
                finally {
                    if (t == null) {
                        final Throwable t3 = null;
                        t = t3;
                    }
                    else {
                        final Throwable t3 = null;
                        if (t != t3) {
                            t.addSuppressed(t3);
                        }
                    }
                    if (fis != null) {
                        fis.close();
                    }
                }
            }
            finally {
                if (t == null) {
                    final Throwable t4 = null;
                    t = t4;
                }
                else {
                    final Throwable t4 = null;
                    if (t != t4) {
                        t.addSuppressed(t4);
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

}

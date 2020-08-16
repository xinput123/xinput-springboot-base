package com.xinput.bootbase.util;

import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleProperties {

    private static final Logger logger = Logs.get();

    private static ClassLoader defaultClassLoader;

    private static final Pattern PATTERN = Pattern.compile("^%([a-zA-Z0-9_\\-]+)\\.(.*)$");

    private static final Pattern PATTERN_2 = Pattern.compile("\\$\\{([^}]+)}");

    /**
     * The loaded configuration files
     */
    private Set<File> confs = new HashSet<>(1);

    private Properties props = new Properties();


    /**
     * Read application.conf and resolve overridden key using the play id mechanism.
     */
    public static SimpleProperties readConfiguration(String filename) {
        SimpleProperties simpleProperties = new SimpleProperties();
        simpleProperties.props = simpleProperties.readOneConfigurationFile(filename);
        return simpleProperties;
    }

    private Properties readOneConfigurationFile(String filename) {
        File conf = new File(filename);
        InputStream is = null;
        Properties propsFromFile = null;

        // 如果是系统的绝对路径
        if (filename.contains("/") || filename.contains("\\")) {
            try {
                is = new FileInputStream(conf);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(filename + " file load error", e);
            }
        } else {
            try {
                is = getResourceAsStream(filename);
            } catch (IOException e) {
                logger.error("Cannot read file: [{}].", filename);
                System.err.println("Cannot read " + filename);
            }
        }
        if (confs.contains(conf)) {
            throw new RuntimeException("Detected recursive @include usage. Have seen the file " + filename + " before");
        }

        try {
            propsFromFile = readUtf8Properties(is);
        } catch (Exception e) {
            if (e.getCause() instanceof IOException) {
                System.err.println("Cannot read " + filename);
            }
        }
        confs.add(conf);

        // OK, check for instance specifics configuration
        Properties newConfiguration = new OrderSafeProperties();
        for (Object key : propsFromFile.keySet()) {
            Matcher matcher = PATTERN.matcher(key + "");
            if (!matcher.matches()) {
                newConfiguration.put(key, propsFromFile.get(key).toString().trim());
            }
        }

        propsFromFile = newConfiguration;
        // Resolve ${..}
        for (Object key : propsFromFile.keySet()) {
            String value = propsFromFile.getProperty(key.toString());
            Matcher matcher = PATTERN_2.matcher(value);
            StringBuffer newValue = new StringBuffer(100);
            while (matcher.find()) {
                String jp = matcher.group(1);
                String r = System.getProperty(jp);
                if (r == null) {
                    r = System.getenv(jp);
                }
                if (r == null) {
                    System.err.println("Cannot replace " + jp + " in configuration (" + key + "=" + value + ")");
                    continue;
                }
                matcher.appendReplacement(newValue, r.replaceAll("\\\\", "\\\\\\\\"));
            }
            matcher.appendTail(newValue);
            propsFromFile.setProperty(key.toString(), newValue.toString());
        }

        // Include
        Map<Object, Object> toInclude = new HashMap<>(16);
        for (Object key : propsFromFile.keySet()) {
            if (key.toString().startsWith("@include.")) {
                try {
                    String filenameToInclude = propsFromFile.getProperty(key.toString());
                    toInclude.putAll(readOneConfigurationFile(filenameToInclude));
                } catch (Exception ex) {
                    System.err.println("Missing include: " + key + " Cause:" + ex);
                }
            }
        }
        propsFromFile.putAll(toInclude);
        return propsFromFile;
    }

    /**
     * Read a properties file with the utf-8 encoding
     *
     * @param is Stream to properties file
     * @return The Properties object
     */
    private static Properties readUtf8Properties(InputStream is) {
        Properties properties = new OrderSafeProperties();
        try {
            properties.load(is);
            return properties;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(is);
        }
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Interface.
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public Properties getUnderlyingProperties() {
        return props;
    }

    /**
     * Get the trimmed String value of the property with the given
     * <code>name</code>. If the value the empty String (after trimming), then
     * it returns null.
     */
    public String getStringProperty(String name) {
        return getStringProperty(name, null);
    }

    /**
     * Get the trimmed String value of the property with the given
     * <code>name</code> or the given default value if the value is null or
     * empty after trimming.
     */
    public String getStringProperty(String name, String def) {
        String val = props.getProperty(name, def);
        if (val == null) {
            return def;
        }

        val = val.trim();

        return (val.length() == 0) ? def : val;
    }

    public String[] getStringArrayProperty(String name) {
        return getStringArrayProperty(name, null);
    }

    public String[] getStringArrayProperty(String name, String[] def) {
        String vals = getStringProperty(name);
        if (vals == null) {
            return def;
        }

        StringTokenizer stok = new StringTokenizer(vals, ",");
        ArrayList<String> strs = new ArrayList<String>();
        try {
            while (stok.hasMoreTokens()) {
                strs.add(stok.nextToken().trim());
            }

            return strs.toArray(new String[strs.size()]);
        } catch (Exception e) {
            return def;
        }
    }

    public boolean getBooleanProperty(String name) {
        return getBooleanProperty(name, false);
    }

    public boolean getBooleanProperty(String name, boolean def) {
        String val = getStringProperty(name);

        return (val == null) ? def : Boolean.valueOf(val).booleanValue();
    }

    public byte getByteProperty(String name) throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) {
            throw new NumberFormatException(" null string");
        }

        try {
            return Byte.parseByte(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    public byte getByteProperty(String name, byte def)
            throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) {
            return def;
        }

        try {
            return Byte.parseByte(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    public char getCharProperty(String name) {
        return getCharProperty(name, '\0');
    }

    public char getCharProperty(String name, char def) {
        String param = getStringProperty(name);
        return (param == null) ? def : param.charAt(0);
    }

    public double getDoubleProperty(String name) throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) {
            throw new NumberFormatException(" null string");
        }

        try {
            return Double.parseDouble(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    public double getDoubleProperty(String name, double def)
            throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) {
            return def;
        }

        try {
            return Double.parseDouble(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    public float getFloatProperty(String name) throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) {
            throw new NumberFormatException(" null string");
        }

        try {
            return Float.parseFloat(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    public float getFloatProperty(String name, float def)
            throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) {
            return def;
        }

        try {
            return Float.parseFloat(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    public int getIntProperty(String name) throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) {
            throw new NumberFormatException(" null string");
        }

        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    public int getIntProperty(String name, int def)
            throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) {
            return def;
        }

        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    public int[] getIntArrayProperty(String name) throws NumberFormatException {
        return getIntArrayProperty(name, null);
    }

    public int[] getIntArrayProperty(String name, int[] def)
            throws NumberFormatException {
        String vals = getStringProperty(name);
        if (vals == null) {
            return def;
        }

        StringTokenizer stok = new StringTokenizer(vals, ",");
        ArrayList<Integer> ints = new ArrayList<Integer>();
        try {
            while (stok.hasMoreTokens()) {
                try {
                    ints.add(new Integer(stok.nextToken().trim()));
                } catch (NumberFormatException nfe) {
                    throw new NumberFormatException(" '" + vals + "'");
                }
            }

            int[] outInts = new int[ints.size()];
            for (int i = 0; i < ints.size(); i++) {
                outInts[i] = (ints.get(i)).intValue();
            }
            return outInts;
        } catch (Exception e) {
            return def;
        }
    }

    public long getLongProperty(String name) throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) {
            throw new NumberFormatException(" null string");
        }

        try {
            return Long.parseLong(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    public long getLongProperty(String name, long def)
            throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) {
            return def;
        }

        try {
            return Long.parseLong(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    public short getShortProperty(String name) throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) {
            throw new NumberFormatException(" null string");
        }

        try {
            return Short.parseShort(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    public short getShortProperty(String name, short def)
            throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) {
            return def;
        }

        try {
            return Short.parseShort(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    public String[] getPropertyGroups(String prefix) {
        Enumeration<?> keys = props.propertyNames();
        HashSet<String> groups = new HashSet<String>(10);

        if (!prefix.endsWith(".")) {
            prefix += ".";
        }

        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            if (key.startsWith(prefix)) {
                String groupName = key.substring(prefix.length(),
                        key.indexOf('.', prefix.length()));
                groups.add(groupName);
            }
        }

        return groups.toArray(new String[groups.size()]);
    }

    public Properties getPropertyGroup(String prefix) {
        return getPropertyGroup(prefix, false, null);
    }

    public Properties getPropertyGroup(String prefix, boolean stripPrefix) {
        return getPropertyGroup(prefix, stripPrefix, null);
    }

    /**
     * Get all properties that start with the given prefix.
     *
     * @param prefix           The prefix for which to search. If it does not end in a "."
     *                         then one will be added to it for search purposes.
     * @param stripPrefix      Whether to strip off the given <code>prefix</code> in the
     *                         result's keys.
     * @param excludedPrefixes Optional array of fully qualified prefixes to exclude. For
     *                         example if <code>prefix</code> is "a.b.c", then
     *                         <code>excludedPrefixes</code> might be "a.b.c.ignore".
     * @return Group of <code>Properties</code> that start with the given
     * prefix, optionally have that prefix removed, and do not include
     * properties that start with one of the given excluded prefixes.
     */
    public Properties getPropertyGroup(String prefix, boolean stripPrefix,
                                       String[] excludedPrefixes) {
        Enumeration<?> keys = props.propertyNames();
        Properties group = new Properties();

        if (!prefix.endsWith(".")) {
            prefix += ".";
        }

        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            if (key.startsWith(prefix)) {

                boolean exclude = false;
                if (excludedPrefixes != null) {
                    for (int i = 0; (i < excludedPrefixes.length)
                            && (!(exclude)); i++) {
                        exclude = key.startsWith(excludedPrefixes[i]);
                    }
                }

                if (!(exclude)) {
                    String value = getStringProperty(key, "");

                    if (stripPrefix) {
                        group.put(key.substring(prefix.length()), value);
                    } else {
                        group.put(key, value);
                    }
                }
            }
        }

        return group;
    }


    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Help method.
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static String escapeJava(String str) {
        return escapeJavaStyleString(str, false, false);
    }


    private static String escapeJavaStyleString(String str, boolean escapeSingleQuotes, boolean escapeForwardSlash) {
        if (str == null) {
            return null;
        }
        try {
            StringWriter writer = new StringWriter(str.length() * 2);
            escapeJavaStyleString(writer, str, escapeSingleQuotes, escapeForwardSlash);
            return writer.toString();
        } catch (IOException ioe) {
            // this should never ever happen while writing to a StringWriter
            throw new RuntimeException(ioe);
        }
    }


    private static void escapeJavaStyleString(Writer out, String str, boolean escapeSingleQuote,
                                              boolean escapeForwardSlash) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        }
        if (str == null) {
            return;
        }
        int sz;
        sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);

            // handle unicode
            if (ch > 0xfff) {
                out.write("\\u" + hex(ch));
            } else if (ch > 0xff) {
                out.write("\\u0" + hex(ch));
            } else if (ch > 0x7f) {
                out.write("\\u00" + hex(ch));
            } else if (ch < 32) {
                switch (ch) {
                    case '\b':
                        out.write('\\');
                        out.write('b');
                        break;
                    case '\n':
                        out.write('\\');
                        out.write('n');
                        break;
                    case '\t':
                        out.write('\\');
                        out.write('t');
                        break;
                    case '\f':
                        out.write('\\');
                        out.write('f');
                        break;
                    case '\r':
                        out.write('\\');
                        out.write('r');
                        break;
                    default:
                        if (ch > 0xf) {
                            out.write("\\u00" + hex(ch));
                        } else {
                            out.write("\\u000" + hex(ch));
                        }
                        break;
                }
            } else {
                switch (ch) {
                    case '\'':
                        if (escapeSingleQuote) {
                            out.write('\\');
                        }
                        out.write('\'');
                        break;
                    case '"':
                        out.write('\\');
                        out.write('"');
                        break;
                    case '\\':
                        out.write('\\');
                        out.write('\\');
                        break;
                    case '/':
                        if (escapeForwardSlash) {
                            out.write('\\');
                        }
                        out.write('/');
                        break;
                    default:
                        out.write(ch);
                        break;
                }
            }
        }
    }

    private static String hex(char ch) {
        return Integer.toHexString(ch).toUpperCase(Locale.ENGLISH);
    }

    private static List<String> readLines(final InputStream input, final String encoding) throws IOException {
        return readLines(input, Charset.forName(encoding));
    }

    private static List<String> readLines(final InputStream input, final Charset encoding) throws IOException {
        final InputStreamReader reader = new InputStreamReader(input, encoding);
        return readLines(reader);
    }

    private static List<String> readLines(final Reader input) throws IOException {
        final BufferedReader reader = toBufferedReader(input);
        final List<String> list = new ArrayList<>();
        String line = reader.readLine();
        while (line != null) {
            list.add(line);
            line = reader.readLine();
        }
        return list;
    }

    private static BufferedReader toBufferedReader(final Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

    private static void closeQuietly(final Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (final IOException ioe) {
            // ignore
        }
    }
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Resource method.
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * Returns a resource on the classpath as a Stream object
     *
     * @param resource The resource to find
     * @return The resource
     * @throws IOException If the resource cannot be found or read
     */
    public static InputStream getResourceAsStream(String resource)
            throws IOException {
        return getResourceAsStream(getClassLoader(), resource);
    }

    /**
     * Returns a resource on the classpath as a Stream object
     *
     * @param loader   The classloader used to load the resource
     * @param resource The resource to find
     * @return The resource
     * @throws IOException If the resource cannot be found or read
     */
    public static InputStream getResourceAsStream(ClassLoader loader,
                                                  String resource) throws IOException {
        InputStream in = null;
        if (loader != null) {
            in = loader.getResourceAsStream(resource);
        }
        if (in == null) {
            in = ClassLoader.getSystemResourceAsStream(resource);
        }
        if (in == null) {
            throw new IOException("Could not find resource " + resource);
        }
        return in;
    }


    private static ClassLoader getClassLoader() {
        if (defaultClassLoader != null) {
            return defaultClassLoader;
        } else {
            return Thread.currentThread().getContextClassLoader();
        }
    }

    private static class OrderSafeProperties extends Properties {

        // set used to preserve key order
        private final LinkedHashSet<Object> keys = new LinkedHashSet<>();

        @Override
        public void load(InputStream inputStream) throws IOException {

            // read all lines from file as utf-8
            List<String> lines = readLines(inputStream, "utf-8");
            closeQuietly(inputStream);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // escape "special-chars" (to utf-16 on the format \\uxxxx) in lines and store as iso-8859-1
            // see info about escaping - http://download.oracle.com/javase/1.5.0/docs/api/java/util/Properties.html - "public void load(InputStream inStream)"
            for (String line : lines) {

                // due to "...by the rule above, single and double quote characters preceded
                // by a backslash still yield single and double quote characters, respectively."
                // we must transform \" => " and \' => ' before escaping to prevent escaping the backslash
                line = line.replaceAll("\\\\\"", "\"").replaceAll("(^|[^\\\\])(\\\\')", "$1'");

                String escapedLine = escapeJava(line) + "\n";
                // remove escaped backslashes
                escapedLine = escapedLine.replaceAll("\\\\\\\\", "\\\\");
                out.write(escapedLine.getBytes("iso-8859-1"));
            }

            // read properties-file with regular java.util.Properties impl
            super.load(new ByteArrayInputStream(out.toByteArray()));

        }

        @Override
        public Enumeration<Object> keys() {
            return Collections.<Object>enumeration(keys);
        }

        @Override
        public Set<Object> keySet() {
            return keys;
        }

        @Override
        public Object put(Object key, Object value) {
            keys.add(key);
            return super.put(key, value);
        }

        @Override
        public Object remove(Object o) {
            keys.remove(o);
            return super.remove(o);
        }

        @Override
        public void clear() {
            keys.clear();
            super.clear();
        }

        @Override
        public void putAll(Map<? extends Object, ? extends Object> map) {
            keys.addAll(map.keySet());
            super.putAll(map);
        }

        @Override
        public Set<Map.Entry<Object, Object>> entrySet() {
            Set<Map.Entry<Object, Object>> entrySet = new LinkedHashSet<>(keys.size());
            for (Object key : keys) {
                entrySet.add(new Entry(key, get(key)));
            }

            return entrySet;
        }

        class Entry implements Map.Entry<Object, Object> {
            private final Object key;
            private final Object value;

            private Entry(Object key, Object value) {
                this.key = key;
                this.value = value;
            }

            @Override
            public Object getKey() {
                return key;
            }

            @Override
            public Object getValue() {
                return value;
            }

            @Override
            public Object setValue(Object o) {
                throw new IllegalStateException("not implemented");
            }
        }


    }
}

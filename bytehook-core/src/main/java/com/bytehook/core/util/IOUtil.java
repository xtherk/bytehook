package com.bytehook.core.util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

/**
 * IO utilities.
 *
 * @author xDark
 */
public final class IOUtil {
	public static final int BUFFER_SIZE = 16384;

	/**
	 * Deny all constructions.
	 */
	private IOUtil() {
	}

	/**
	 * Quietly closes a resource.
	 *
	 * @param closeable
	 *        {@link Closeable} to close.
	 */
	public static void closeQuietly(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException ignored) {
				// no-op
			}
		}
	}

	/**
	 * Quietly closes a resource.
	 *
	 * @param closeable
	 *        {@link Closeable} to close.
	 */
	public static void closeQuietly(AutoCloseable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception ignored) {
				// no-op
			}
		}
	}

	/**
	 * Allocates new byte buffer.
	 *
	 * @return New byte buffer.
	 *
	 * @see IOUtil#BUFFER_SIZE
	 */
	public static byte[] newByteBuffer() {
		return new byte[BUFFER_SIZE];
	}

	/**
	 * Allocates new char buffer.
	 *
	 * @return New char buffer.
	 *
	 * @see IOUtil#BUFFER_SIZE
	 */
	public static char[] newCharBuffer() {
		return new char[BUFFER_SIZE];
	}

	/**
	 * Returns content of {@link InputStream} as a byte array.
	 *
	 * @param in
	 * 		Stream to transfer content from.
	 * @param buf
	 * 		Buffer that is used to hold data.
	 *
	 * @return content of {@link InputStream}.
	 *
	 * @throws IOException
	 * 		When any I/O error occurs.
	 */
	public static byte[] toByteArray(InputStream in, byte[] buf) throws IOException {
		int length;
		try {
			length = in.available();
		} catch (IOException ignored) {
			// Cannot get length from stream, fallback to default.
			length = BUFFER_SIZE;
		}
		OptimizedByteArrayOutputStream baos = new OptimizedByteArrayOutputStream(length);
		copy(in, baos, buf);
		return baos.getBytes();
	}

	/**
	 * Returns content of {@link InputStream} as a byte array.
	 *
	 * @param in
	 * 		Stream to transfer content from.
	 *
	 * @return Content of {@link InputStream}.
	 *
	 * @throws IOException
	 * 		When any I/O error occurs.
	 * @see IOUtil#newByteBuffer()
	 */
	public static byte[] toByteArray(InputStream in) throws IOException {
		return toByteArray(in, newByteBuffer());
	}

	/**
	 * Get the extension from a path.
	 *
	 * @param path
	 * 		Path to get extension from.
	 *
	 * @return Name of extension of the path if present, {@code null} otherwise.
	 */
	public static String getExtension(String path) {
		int dotIndex = path.lastIndexOf('.');
		if (dotIndex < path.length() - 1) {
			return path.substring(dotIndex + 1);
		} else {
			return null;
		}
	}

	/**
	 * Get the extension from a path.
	 *
	 * @param path
	 *        {@link Path} to get extension from.
	 *
	 * @return Name of extension of the path if present, {@code null} otherwise.
	 */
	public static String getExtension(Path path) {
		return getExtension(path.getFileName().toString());
	}


	/**
	 * Transfers content of {@link InputStream} into {@link OutputStream}.
	 *
	 * @param in
	 * 		Stream to transfer content from.
	 * @param out
	 * 		Stream to transfer content to.
	 * @param buf
	 * 		Buffer that is used to hold data.
	 *
	 * @throws IOException
	 * 		When any I/O error occurs.
	 */
	public static void copy(InputStream in, OutputStream out, byte[] buf) throws IOException {
		int r;
		while ((r = in.read(buf)) != -1) {
			out.write(buf, 0, r);
		}
	}

	/**
	 * Transfers content of {@link InputStream} into {@link OutputStream}.
	 *
	 * @param in
	 * 		Stream to transfer content from.
	 * @param out
	 * 		Stream to transfer content to.
	 *
	 * @throws IOException
	 * 		When any I/O error occurs.
	 * @see IOUtil#newByteBuffer()
	 */
	public static void copy(InputStream in, OutputStream out) throws IOException {
		copy(in, out, newByteBuffer());
	}

	/**
	 * Transfers content of {@link Reader} into {@link Writer}.
	 *
	 * @param reader
	 * 		Reader to transfer content from.
	 * @param writer
	 * 		Writer to transfer content to.
	 * @param buf
	 * 		Buffer that is used to hold data.
	 *
	 * @throws IOException
	 * 		When any I/O error occurs.
	 */
	public static void copy(Reader reader, Writer writer, char[] buf) throws IOException {
		int r;
		while ((r = reader.read(buf)) != -1) {
			writer.write(buf, 0, r);
		}
	}

	/**
	 * Transfers content of {@link Reader} into {@link Writer}.
	 *
	 * @param reader
	 * 		Reader to transfer content from.
	 * @param writer
	 * 		Writer to transfer content to.
	 *
	 * @throws IOException
	 * 		When any I/O error occurs.
	 * @see IOUtil#newCharBuffer()
	 */
	public static void copy(Reader reader, Writer writer) throws IOException {
		copy(reader, writer, newCharBuffer());
	}

	/**
	 * Transfers content of {@link InputStream} into {@link Writer}.
	 *
	 * @param in
	 * 		Stream to transfer content from.
	 * @param writer
	 * 		Writer to transfer content to.
	 * @param charset
	 * 		Charset that will be used to read data.
	 * @param buf
	 * 		Buffer that is used to hold data.
	 *
	 * @throws IOException
	 * 		When any I/O error occurs.
	 */
	public static void copy(InputStream in, Writer writer, Charset charset, char[] buf) throws IOException {
		copy(new InputStreamReader(in, charset), writer, buf);
	}

	/**
	 * Transfers content of {@link InputStream} into {@link Writer}.
	 *
	 * @param in
	 * 		Stream to transfer content from.
	 * @param writer
	 * 		Writer to transfer content to.
	 * @param charset
	 * 		Charset that will be used to read data.
	 *
	 * @throws IOException
	 * 		When any I/O error occurs.
	 * @see IOUtil#newCharBuffer()
	 */
	public static void copy(InputStream in, Charset charset, Writer writer) throws IOException {
		copy(in, writer, charset, newCharBuffer());
	}

	/**
	 * Transfers content of {@link InputStream} into {@link Writer}.
	 * Uses {@link StandardCharsets#UTF_8} as default charset.
	 *
	 * @param in
	 * 		Stream to transfer content from.
	 * @param writer
	 * 		Writer to transfer content to.
	 *
	 * @throws IOException
	 * 		When any I/O error occurs.
	 * @see IOUtil#newCharBuffer()
	 */
	public static void copy(InputStream in, Writer writer) throws IOException {
		copy(in, writer, StandardCharsets.UTF_8, newCharBuffer());
	}

	/**
	 * Transfers content of {@link InputStream} into {@link Writer}.
	 * Uses {@link StandardCharsets#UTF_8} as default charset.
	 *
	 * @param in
	 * 		Stream to transfer content from.
	 * @param writer
	 * 		Writer to transfer content to.
	 * @param buf
	 * 		Buffer that is used to hold data.
	 *
	 * @throws IOException
	 * 		When any I/O error occurs.
	 * @see IOUtil#newCharBuffer()
	 */
	public static void copy(InputStream in, Writer writer, char[] buf) throws IOException {
		copy(in, writer, StandardCharsets.UTF_8, buf);
	}

	/**
	 * Transfer contents of {@link URL} into {@link OutputStream}.
	 *
	 * @param url
	 *        {@link URL} to transfer content from.
	 * @param out
	 * 		stream to transfer content to.
	 * @param buf
	 * 		buffer that is used to hold data.
	 * @param connectionTimeoutMillis
	 * 		Number of milliseconds until this method will timeout if no connection could
	 * 		be established to the {@code url}
	 * @param readTimeoutMillis
	 * 		Number of milliseconds until this method will timeout if no data could be read from
	 * 		the {@code url}
	 *
	 * @throws IOException
	 * 		When any I/O error occurs.
	 */
	public static void copy(URL url, OutputStream out,
							byte[] buf,
							int connectionTimeoutMillis,
							int readTimeoutMillis)
			throws IOException {
		URLConnection connection = url.openConnection();
		connection.setDoInput(true);
		connection.setConnectTimeout(connectionTimeoutMillis);
		connection.setReadTimeout(readTimeoutMillis);
		try (InputStream in = connection.getInputStream()) {
			copy(in, out, buf);
		}
	}

	/**
	 * Transfer contents of {@link URL} into {@link OutputStream}.
	 *
	 * @param url
	 *        {@link URL} to transfer content from.
	 * @param out
	 * 		Stream to transfer content to.
	 * @param connectionTimeoutMillis
	 * 		Number of milliseconds until this method will timeout if no connection could
	 * 		be established to the {@code url}
	 * @param readTimeoutMillis
	 * 		Number of milliseconds until this method will timeout if no data could be read from
	 * 		the {@code url}
	 *
	 * @throws IOException
	 * 		When any I/O error occurs.
	 * @see IOUtil#newByteBuffer()
	 */
	public static void copy(URL url, OutputStream out,
							int connectionTimeoutMillis,
							int readTimeoutMillis)
			throws IOException {
		copy(url, out, newByteBuffer(), connectionTimeoutMillis, readTimeoutMillis);
	}

	/**
	 * Transfer contents of {@link URL} into {@link OutputStream} of {@link Path}.
	 *
	 * @param url
	 *        {@link URL} to transfer content from.
	 * @param path
	 * 		Path to transfer content to.
	 * @param buf
	 * 		Buffer that is used to hold data.
	 * @param connectionTimeoutMillis
	 * 		Number of milliseconds until this method will timeout if no connection could
	 * 		be established to the {@code url}
	 * @param readTimeoutMillis
	 * 		Number of milliseconds until this method will timeout if no data could be read from
	 * 		the {@code url}
	 *
	 * @throws IOException
	 * 		When any I/O error occurs.
	 */
	public static void copy(URL url, Path path,
							byte[] buf,
							int connectionTimeoutMillis,
							int readTimeoutMillis)
			throws IOException {
		try (OutputStream os = Files.newOutputStream(path)) {
			copy(url, os, buf, connectionTimeoutMillis, readTimeoutMillis);
		}
	}

	/**
	 * Transfer contents of {@link URL} into {@link OutputStream} of {@link Path}.
	 *
	 * @param url
	 *        {@link URL} to transfer content from.
	 * @param path
	 * 		Path to transfer content to.
	 * @param connectionTimeoutMillis
	 * 		Number of milliseconds until this method will timeout if no connection could
	 * 		be established to the {@code url}
	 * @param readTimeoutMillis
	 * 		Number of milliseconds until this method will timeout if no data could be read from the {@code url}
	 *
	 * @throws IOException
	 * 		When any I/O error occurs.
	 * @see IOUtil#newByteBuffer()
	 */
	public static void copy(URL url, Path path,
							int connectionTimeoutMillis,
							int readTimeoutMillis)
			throws IOException {
		copy(url, path, newByteBuffer(), connectionTimeoutMillis, readTimeoutMillis);
	}

	/**
	 * @param path
	 *        {@link Path} to check against.
	 *
	 * @return {@code true} if a @link Path} belongs to default file system.
	 * Otherwise {@code false}.
	 */
	public static boolean isOnDefaultFileSystem(Path path) {
		return path.getFileSystem() == FileSystems.getDefault();
	}

	/**
	 * Cleans a directory.
	 *
	 * @param path
	 * 		Directory to clean.
	 *
	 * @throws IOException
	 * 		Whenn any I/O error occurs.
	 */
	public static void cleanDirectory(Path path) throws IOException {
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	/**
	 * Performs deletion quietly.
	 *
	 * @param path
	 *        {@link Path} to delete.
	 */
	public static void deleteQuietly(Path path) {
		try {
			if (isOnDefaultFileSystem(path)) {
				deleteQuietly(path.toFile());
			} else {
				if (Files.isDirectory(path)) {
					cleanDirectory(path);
				}
				Files.deleteIfExists(path);
			}
		} catch (IOException ignored) {
			// no-op
		}
	}

	/**
	 * Performs deletion quietly.
	 *
	 * @param file
	 *        {@link File} to delete.
	 */
	public static void deleteQuietly(File file) {
		if (file.isDirectory()) {
			File[] list = file.listFiles();
			if (list != null) {
				for (File f : list) {
					deleteQuietly(f);
				}
			}
		}
		file.delete();
	}

	/**
	 * Tests whether the file denoted by a path is a normal file.
	 *
	 * @param path
	 * 		Path to check.
	 *
	 * @return {@code true} if file is a regular file.
	 */
	public static boolean isRegularFile(Path path) {
		// Actually fallback to java.io package if possible,
		// because IO is faster than NIO when for file status checking.
		return isOnDefaultFileSystem(path) ? path.toFile().isFile() : Files.isRegularFile(path);
	}

	private static final class OptimizedByteArrayOutputStream extends ByteArrayOutputStream {
		/**
		 * @param size
		 * 		Initial size.
		 */
		OptimizedByteArrayOutputStream(int size) {
			super(size);
		}

		/**
		 * Non thread-safe alternative to {@link #toByteArray()} that skips array copying when possible.
		 *
		 * @return Content of this stream.
		 */
		byte[] getBytes() {
			byte[] buf = this.buf;
			int count = this.count;
			// If the size is a match we do not need to do a copy operation
			if (count == buf.length) {
				return buf;
			}
			// Cut buffer down to expected size
			return Arrays.copyOfRange(buf, 0, count);
		}
	}
}

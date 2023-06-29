import java.util.List;

// Top-level classes are either public or package-private
public class TopLevelClassPublic {
	// Inner classes
	class InnerClass {}
	public  class InnerClassPublic {

		public int number;
		public List<Integer> text;

		public InnerClassPublic(int number, List<Integer> text) {
			this.number = number;
			this.text = text;
		}

		public void varargsMethod(int... number, String[] texts) {}

		public int getNumber() {
			return number;
		}

		public int setNumber(int number) {

			this.number = number;
			return 7;

		}

		public String setText(String text) {
			this.text = text;
		}
	}
	protected class InnerClassProtected {
		public List<String> exceptions;

		public List<Integer> processList(List<T> list, List<Integer> strings) {

			return strings;
		}
		public void throwExceptions() throws IOException, InterruptedException {

			throw new IOException("An I/O exception occurred.");

		}
	}
	private class InnerClassPrivate {}

	// Nested classes
	static class NestedClass {}
	public static class NestedClassPublic {}
	protected static class NestedClassProtected {}
	private static class NestedClassPrivate {}

	// Nested interfaces are implicitly static (no inner interfaces)
	interface NestedInterface {}
	public interface NestedInterfacePublic {}
	protected interface NestedInterfaceProtected {}
	private interface NestedInterfacePrivate {}

	// Nested records are implicitly static (no inner records)
	record NestedRecord() {}
	public record NestedRecordPublic() {}
	protected record NestedRecordProtected() {}
	private record NestedRecordPrivate() {}

	// Nested enums are implicitly static (no inner enums)
	enum NestedEnum {}
	public enum NestedEnumPublic {}
	protected enum NestedEnumProtected {}
	private enum NestedEnumPrivate {}

	// Annotations are interfaces so nested annotations are implicitly static (no inner annotations)
	@interface NestedAnnotation {}
	public @interface NestedAnnotationPublic {}
	protected @interface NestedAnnotationProtected {}
	private @interface NestedAnnotationPrivate {}
}
